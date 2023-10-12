package com.zerobase.foodlier.common.socialLogin.client;

import com.zerobase.foodlier.common.socialLogin.dto.OAuthInfoResponse;
import com.zerobase.foodlier.common.socialLogin.dto.OAuthLoginParams;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;

public interface OAuthApiClient {
    RegistrationType registrationType();
    String requestAccessToken(OAuthLoginParams params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
}