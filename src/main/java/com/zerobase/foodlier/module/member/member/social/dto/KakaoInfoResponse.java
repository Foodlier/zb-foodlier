package com.zerobase.foodlier.module.member.member.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.Getter;

import static com.zerobase.foodlier.module.member.member.type.RegistrationType.*;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public RegistrationType getRegistrationType() {
        return KAKAO;
    }
}