package com.dmaker.service;

import com.dmaker.dto.CreateDeveloper;
import com.dmaker.entity.Developer;
import com.dmaker.exception.DMakerErrorCode;
import com.dmaker.exception.DMakerException;
import com.dmaker.repository.DeveloperRepository;
import com.dmaker.type.DeveloperLevel;
import com.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);


        Developer developer = Developer.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(2)
                .name("Olaf")
                .age(5)
                .build();

        developerRepository.save(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        if (request.getDeveloperLevel() == DeveloperLevel.SENIOR
                && request.getExperienceYear() < 10) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (request.getDeveloperLevel() == DeveloperLevel.JUNIOR
                && (request.getExperienceYear() < 4 || request.getExperienceYear() > 10)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (request.getDeveloperLevel() == DeveloperLevel.JUNIOR
                && request.getExperienceYear() > 4) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        developerRepository.findbyMemberID(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                }));


    }
}
