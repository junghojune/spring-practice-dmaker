package com.dmaker.controller;


import com.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j // 간단하게 로그를 찍어주는 기능
@RestController // controller, responsebody 두개와 같은것 (A convenience annotation that is itself annotated with @Controller and @ResponseBody.)
@RequiredArgsConstructor
public class DmakerController {

    private final DMakerService dMakerService;


    @GetMapping("/developers")
    public List<String> getAllDeverlopers() {

        // GET /developeres HTTP/1.1
        log.info("GET .developers HTTP/1.1");

        return Arrays.asList("snow", "Elsa", "Olaf");
    }

    @GetMapping("/create-developer")
    public List<String> createDeverlopers() {

        // GET /developeres HTTP/1.1
        log.info("GET .developers HTTP/1.1");
        dMakerService.createDeveloper();
        return Collections.singletonList("Olaf");
    }
}
