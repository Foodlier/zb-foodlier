package com.zerobase.foodlier.common.socialLogin.service;

import com.zerobase.foodlier.common.socialLogin.client.OAuthApiClient;
import com.zerobase.foodlier.common.socialLogin.dto.OAuthInfoResponse;
import com.zerobase.foodlier.common.socialLogin.dto.OAuthLoginParams;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestOAuthInfoService {
    private final Map<RegistrationType, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::registrationType, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.registrationType());
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}