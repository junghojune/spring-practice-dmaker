package com.dmaker.service;

import com.dmaker.code.StatusCode;
import com.dmaker.dto.CreateDeveloper;
import com.dmaker.dto.DeveloperDetailDto;
import com.dmaker.entity.Developer;
import com.dmaker.exception.DMakerException;
import com.dmaker.repository.DeveloperRepository;
import com.dmaker.repository.RetiredDeveloperRepository;
import com.dmaker.type.DeveloperLevel;
import com.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;


    @InjectMocks
    private DMakerService dMakerService;

    private Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(12)
            .build();

    private final CreateDeveloper.Request defualtCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    public void testSomething() {
        //mocking 하는 방법
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given : mocking , 지역변수 들은 만들어 놓음
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        // 데이터가 날라갈때 어떻게 날라가는지 확인하고 싶을때때
       ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        //when : 테스트 하고자 하는 동작, 동작으로 만들어진 결과값을 받아오는 것
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defualtCreateRequest);

        //then : 예상한 동작들이 동작하는지 검증
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given : mocking , 지역변수 들은 만들어 놓음

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when : 테스트 하고자 하는 동작, 동작으로 만들어진 결과값을 받아오는 것
        //then : 예상한 동작들이 동작하는지 검증
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(defualtCreateRequest)
        );

        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

}