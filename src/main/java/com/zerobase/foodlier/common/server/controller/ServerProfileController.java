package com.zerobase.foodlier.common.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/env")
@RequiredArgsConstructor
public class ServerProfileController {

    private final Environment environment;

    @GetMapping("/profile")
    public String profile(){

        List<String> profiles= Arrays.asList(environment.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("deploy1", "deploy2");

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream().filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);

    }


}
