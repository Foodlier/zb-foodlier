package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.client.OAuthApiClient;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthLoginParams;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthInfoServiceImpl implements OAuthInfoService {
    private final Map<RegistrationType, OAuthApiClient> clients;

    public OAuthInfoServiceImpl(List<OAuthApiClient> clients) {
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