package com.dmaker.controller;


import com.dmaker.dto.CreateDeveloper;
import com.dmaker.dto.DeveloperDetailDto;
import com.dmaker.dto.DeveloperDto;
import com.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j // 간단하게 로그를 찍어주는 기능
@RestController
// controller, responsebody 두개와 같은것 (A convenience annotation that is itself annotated with @Controller and @ResponseBody.)
@RequiredArgsConstructor
public class DmakerController {

    private final DMakerService dMakerService;


    @GetMapping("/developers")
    public List<DeveloperDto> getAllDeverlopers() {

        // GET /developeres HTTP/1.1
        log.info("GET .developers HTTP/1.1");

        return dMakerService.getAllDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeverloperDetail(
            @PathVariable String memberId
    ) {

        // GET /developeres HTTP/1.1
        log.info("GET .developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(
            @Valid @RequestBody CreateDeveloper.Request request

    ) {
        // GET /developeres HTTP/1.1
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }


}
