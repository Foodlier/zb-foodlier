package com.zerobase.foodlier.module.member.member.social.dto;

import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.zerobase.foodlier.module.member.member.constants.OAuthConstants.AUTHORIZATION_CODE;
import static com.zerobase.foodlier.module.member.member.constants.OAuthConstants.STATE;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.NAVER;

@Getter
@NoArgsConstructor
public class NaverLoginParams implements OAuthLoginParams {
    private String authorizationCode;
    private String state;

    @Override
    public RegistrationType registrationType() {
        return NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(AUTHORIZATION_CODE, authorizationCode);
        body.add(STATE, state);
        return body;
    }
}