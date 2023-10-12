package com.zerobase.foodlier.module.member.member.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.module.member.member.exception.OAuthException;
import com.zerobase.foodlier.module.member.member.social.dto.*;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_HEADER;
import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_PREFIX;
import static com.zerobase.foodlier.module.member.member.exception.OAuthErrorCode.FAILED_AUTH;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.NAVER;

@Component
@RequiredArgsConstructor
public class NaverApiClient implements OAuthApiClient {

    public static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.naver.url.auth}")
    private String authUrl;

    @Value("${oauth.naver.url.api}")
    private String apiUrl;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public RegistrationType registrationType() {
        return NAVER;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.setAll(new ObjectMapper().convertValue(
                new RequestBodyNaver(GRANT_TYPE, clientId, clientSecret),
                new TypeReference<>() {
                }));

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        try {
            NaverTokens response = restTemplate
                    .postForObject(url, request, NaverTokens.class);
            return Objects.requireNonNull(response).getAccessToken();
        } catch (Exception e) {
            throw new OAuthException(FAILED_AUTH);
        }
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(TOKEN_HEADER, TOKEN_PREFIX + " " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        try {
            return restTemplate.postForObject(url, request, NaverInfoResponse.class);
        } catch (Exception e) {
            throw new OAuthException(FAILED_AUTH);
        }
    }
}