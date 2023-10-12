package com.zerobase.foodlier.common.socialLogin.dto;

import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.zerobase.foodlier.module.member.member.type.RegistrationType.KAKAO;

@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuthLoginParams {
    private String authorizationCode;

    @Override
    public RegistrationType registrationType() {
        return KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}