package com.dmaker.service;

import com.dmaker.dto.CreateDeveloper;
import com.dmaker.dto.DeveloperDetailDto;
import com.dmaker.dto.DeveloperDto;
import com.dmaker.entity.Developer;
import com.dmaker.exception.DMakerErrorCode;
import com.dmaker.exception.DMakerException;
import com.dmaker.repository.DeveloperRepository;
import com.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;


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
                .name(request.getName())
                .age(request.getAge())
                .build();

        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        if (request.getDeveloperLevel() == DeveloperLevel.SENIOR
                && request.getExperienceYears() < 10) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (request.getDeveloperLevel() == DeveloperLevel.JUNGNIOR
                && (request.getExperienceYears() < 4 || request.getExperienceYears() > 10)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (request.getDeveloperLevel() == DeveloperLevel.JUNIOR
                && request.getExperienceYears() > 4) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                }));


    }

    public List<DeveloperDto> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDto::fromEntity)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

    }
}
