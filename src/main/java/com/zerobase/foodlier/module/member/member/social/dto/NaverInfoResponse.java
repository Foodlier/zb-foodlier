package com.zerobase.foodlier.module.member.member.social.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.Getter;

import static com.zerobase.foodlier.module.member.member.type.RegistrationType.NAVER;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {
        private String email;
        private String nickname;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getNickname() {
        return response.nickname;
    }

    @Override
    public RegistrationType getRegistrationType() {
        return NAVER;
    }
}