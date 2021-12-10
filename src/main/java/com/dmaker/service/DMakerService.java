package com.dmaker.service;

import com.dmaker.code.StatusCode;
import com.dmaker.dto.CreateDeveloper;
import com.dmaker.dto.DeveloperDetailDto;
import com.dmaker.dto.DeveloperDto;
import com.dmaker.dto.EditDeveloper;
import com.dmaker.entity.Developer;
import com.dmaker.entity.RetiredDeveloper;
import com.dmaker.exception.DMakerErrorCode;
import com.dmaker.exception.DMakerException;
import com.dmaker.repository.DeveloperRepository;
import com.dmaker.repository.RetiredDeveloperRepository;
import com.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    // ACID
    // Atomic
    // Consistency
    // Isolation
    // Durability

    @Transactional // AOP(transactionall 안에 있는 invoke로 joinpoint로
    // AOP기능을 사용)
    // 기능으로 AICD를 구현할 수 있음
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);


        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .statusCode(StatusCode.EMPLOYED)
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                }));


    }

    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

    }

    @Transactional
    public DeveloperDetailDto editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(request, memberId);

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(DMakerErrorCode.NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(developer);
    }

    private void validateEditDeveloperRequest(
            EditDeveloper.Request request,
            String memberId
    ) {

        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );

        developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(DMakerErrorCode.NO_DEVELOPER)
        );
    }

    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears > 10)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR
                && experienceYears > 4) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYED -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new DMakerException(DMakerErrorCode.NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);
        // 2. save into RetiredDeveloper

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);

        return DeveloperDetailDto.fromEntity(developer);
    }


}
