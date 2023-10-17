package com.zerobase.foodlier.module.member.member.social.dto;

import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    RegistrationType registrationType();
    MultiValueMap<String, String> makeBody();
}
