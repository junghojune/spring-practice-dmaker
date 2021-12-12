package com.dmaker.controller;

import com.dmaker.dto.CreateDeveloper;
import com.dmaker.dto.DeveloperDto;
import com.dmaker.service.DMakerService;
import com.dmaker.type.DeveloperLevel;
import com.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 내가 원하는 controller만 테스트를 할 수 있음
@WebMvcTest(DmakerController.class)
class DmakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);
    @Test
    void getAllDevelopers() throws Exception{
        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .memberId("memberID1").build();
        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .memberId("memberID2").build();
        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto,seniorDeveloperDto ));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(
                        jsonPath("$.[0].developerSkillType",
                                is(DeveloperSkillType.BACK_END.name()))
                )
                .andExpect(
                jsonPath("$.[0].developerLevel",
                        is(DeveloperLevel.JUNIOR.name()))
                )
                .andExpect(
                        jsonPath("$.[1].developerSkillType",
                                is(DeveloperSkillType.FRONT_END.name()))
                )
                .andExpect(
                        jsonPath("$.[1].developerLevel",
                                is(DeveloperLevel.SENIOR.name()))
                );
    }


}