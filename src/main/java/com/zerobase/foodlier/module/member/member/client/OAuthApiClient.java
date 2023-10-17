package com.zerobase.foodlier.module.member.member.client;

import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthLoginParams;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;

public interface OAuthApiClient {
    RegistrationType registrationType();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}