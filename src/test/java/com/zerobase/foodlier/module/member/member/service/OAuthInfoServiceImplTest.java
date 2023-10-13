package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.client.OAuthApiClient;
import com.zerobase.foodlier.module.member.member.exception.OAuthException;
import com.zerobase.foodlier.module.member.member.social.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.OAuthErrorCode.FAILED_AUTH;
import static com.zerobase.foodlier.module.member.member.exception.OAuthErrorCode.INVALID_TOKEN;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.KAKAO;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.NAVER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OAuthInfoServiceImplTest {

    @Mock
    private OAuthApiClient kakaoApiClient;

    @Mock
    private OAuthApiClient naverApiClient;

    private OAuthInfoServiceImpl oAuthInfoService;

    @BeforeEach
    void before() {
        List<OAuthApiClient> mockClients = Arrays.asList(kakaoApiClient, naverApiClient);
        given(kakaoApiClient.registrationType()).willReturn(KAKAO);
        given(naverApiClient.registrationType()).willReturn(NAVER);
        oAuthInfoService = new OAuthInfoServiceImpl(mockClients);
    }

    @Test
    @DisplayName("카카오 소셜 사용자 정보 받아오기 성공")
    void success_kakao_request() {
        //given
        String authorizationCode = "authorizationCode";
        String nickname = "nickname";
        String email = "email";
        String token = "token";
        String expectedEmail = "email";

        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse kakaoInfoResponse = new KakaoInfoResponse(kakaoAccount);
        OAuthLoginParams params = new KakaoLoginParams(authorizationCode);

        given(kakaoApiClient.requestAccessToken(params)).willReturn(token);
        given(kakaoApiClient.requestOauthInfo(token)).willReturn(kakaoInfoResponse);

        //when
        OAuthInfoResponse response = oAuthInfoService.request(params);

        //then
        assertEquals(expectedEmail, response.getEmail());
    }

    @Test
    @DisplayName("카카오 토큰 받아오기 실패 - 소셜인증 실패")
    void fail_kakao_request_failed_auth() {
        //given
        String authorizationCode = "authorizationCode";
        OAuthLoginParams params = new KakaoLoginParams(authorizationCode);

        given(kakaoApiClient.requestAccessToken(params))
                .willThrow(new OAuthException(FAILED_AUTH));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthInfoService.request(params));

        //then
        assertEquals(FAILED_AUTH, oAuthException.getErrorCode());
    }

    @Test
    @DisplayName("카카오 사용자 정보 받아오기 실패 - 토큰인증 실패")
    void fail_kakao_request_invalid_token() {
        //given
        String authorizationCode = "authorizationCode";
        String token = "token";

        OAuthLoginParams params = new KakaoLoginParams(authorizationCode);

        given(kakaoApiClient.requestAccessToken(params)).willReturn(token);
        given(kakaoApiClient.requestOauthInfo(token))
                .willThrow(new OAuthException(INVALID_TOKEN));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthInfoService.request(params));

        //then
        assertEquals(INVALID_TOKEN, oAuthException.getErrorCode());
    }

    @Test
    @DisplayName("네이버 소셜 사용자 정보 받아오기 성공")
    void success_naver_request() {
        //given
        String authorizationCode = "authorizationCode";
        String state = "state";
        String nickname = "nickname";
        String email = "email";
        String token = "token";
        String expectedEmail = "email";
        String expectedNickname = "nickname";

        NaverInfoResponse.Response naverProfile =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse naverInfoResponse = new NaverInfoResponse(naverProfile);
        OAuthLoginParams params = new NaverLoginParams(authorizationCode, state);

        given(naverApiClient.requestAccessToken(params)).willReturn(token);
        given(naverApiClient.requestOauthInfo(token)).willReturn(naverInfoResponse);

        //when
        OAuthInfoResponse response = oAuthInfoService.request(params);

        //then
        assertEquals(expectedEmail, response.getEmail());
        assertEquals(expectedNickname, response.getNickname());
    }

    @Test
    @DisplayName("네이버 토큰 받아오기 실패 - 소셜인증 실패")
    void fail_naver_request_failed_auth() {
        //given
        String authorizationCode = "authorizationCode";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorizationCode, state);

        given(naverApiClient.requestAccessToken(params))
                .willThrow(new OAuthException(FAILED_AUTH));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthInfoService.request(params));

        //then
        assertEquals(FAILED_AUTH, oAuthException.getErrorCode());
    }

    @Test
    @DisplayName("네이버 사용자 정보 받아오기 실패 - 토큰인증 실패")
    void fail_naver_request_invalid_token() {
        //given
        String authorizationCode = "authorizationCode";
        String token = "token";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorizationCode, state);

        given(naverApiClient.requestAccessToken(params)).willReturn(token);
        given(naverApiClient.requestOauthInfo(token))
                .willThrow(new OAuthException(INVALID_TOKEN));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthInfoService.request(params));

        //then
        assertEquals(INVALID_TOKEN, oAuthException.getErrorCode());
    }
}