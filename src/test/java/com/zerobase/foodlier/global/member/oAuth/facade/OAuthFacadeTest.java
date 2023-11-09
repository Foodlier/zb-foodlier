package com.zerobase.foodlier.global.member.oAuth.facade;

import com.zerobase.foodlier.common.security.provider.JwtProvider;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.OAuthException;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.service.OAuthInfoService;
import com.zerobase.foodlier.module.member.member.social.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.OAuthErrorCode.FAILED_AUTH;
import static com.zerobase.foodlier.module.member.member.exception.OAuthErrorCode.INVALID_TOKEN;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OAuthFacadeTest {

    @Mock
    private OAuthInfoService oAuthInfoService;
    @Mock
    private MemberService memberService;
    @Mock
    private JwtProvider tokenProvider;
    @InjectMocks
    private OAuthFacade oAuthFacade;

    @Test
    @DisplayName("카카오 첫 로그인시 임시가입 후 토큰발급 성공")
    void success_first_kakao_login() {
        //given
        String authorization = "authorization";
        OAuthLoginParams params = new KakaoLoginParams(authorization);
        String nickname = "nickname";
        String email = "email";

        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse oAuthInfoResponse = new KakaoInfoResponse(kakaoAccount);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(KAKAO)
                .isTemp(true)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);
        given(tokenProvider.createToken(any(), any())).willReturn(
                new TokenDto("accessToken", "refreshToken")
        );

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(KAKAO, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp()),
                () -> assertEquals("accessToken",
                        socialLoginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals("refreshToken",
                        socialLoginResponse.getTokenDto().getRefreshToken())
        );

    }

    @Test
    @DisplayName("네이버 첫 로그인시 임시가입 후 토큰발급 성공")
    void success_first_naver_login() {
        //given
        String authorization = "authorization";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorization, state);
        String nickname = "nickname";
        String email = "email";

        NaverInfoResponse.Response response =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse oAuthInfoResponse = new NaverInfoResponse(response);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(NAVER)
                .isTemp(true)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);
        given(tokenProvider.createToken(any(), any())).willReturn(
                new TokenDto("accessToken", "refreshToken")
        );

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(NAVER, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp()),
                () -> assertEquals("accessToken",
                        socialLoginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals("refreshToken",
                        socialLoginResponse.getTokenDto().getRefreshToken())
        );

    }

    @Test
    @DisplayName("카카오 로그인 성공 후 토큰발급 성공")
    void success_kakao_login() {
        //given
        String authorization = "authorization";
        OAuthLoginParams params = new KakaoLoginParams(authorization);
        String nickname = "nickname";
        String email = "email";

        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse oAuthInfoResponse = new KakaoInfoResponse(kakaoAccount);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(KAKAO)
                .isTemp(false)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);
        given(tokenProvider.createToken(any(), any())).willReturn(
                new TokenDto("accessToken", "refreshToken")
        );

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(KAKAO, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp()),
                () -> assertEquals("accessToken",
                        socialLoginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals("refreshToken",
                        socialLoginResponse.getTokenDto().getRefreshToken())
        );

    }

    @Test
    @DisplayName("네이버 로그인 성공 후 토큰발급 성공")
    void success_naver_login() {
        //given
        String authorization = "authorization";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorization, state);
        String nickname = "nickname";
        String email = "email";

        NaverInfoResponse.Response response =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse oAuthInfoResponse = new NaverInfoResponse(response);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(NAVER)
                .isTemp(false)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);
        given(tokenProvider.createToken(any(), any())).willReturn(
                new TokenDto("accessToken", "refreshToken")
        );

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(NAVER, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp()),
                () -> assertEquals("accessToken",
                        socialLoginResponse.getTokenDto().getAccessToken()),
                () -> assertEquals("refreshToken",
                        socialLoginResponse.getTokenDto().getRefreshToken())
        );

    }

    @Test
    @DisplayName("카카오 로그인 시 기존 가입 이메일 존재")
    void fail_kakao_login() {
        //given
        String authorization = "authorization";
        OAuthLoginParams params = new KakaoLoginParams(authorization);
        String nickname = "nickname";
        String email = "email";

        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse oAuthInfoResponse = new KakaoInfoResponse(kakaoAccount);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(DOMAIN)
                .isTemp(false)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(DOMAIN, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp())
        );

    }

    @Test
    @DisplayName("네이버 로그인 시 기존 가입 이메일 존재")
    void fail_naver_login() {
        //given
        String authorization = "authorization";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorization, state);
        String nickname = "nickname";
        String email = "email";

        NaverInfoResponse.Response response =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse oAuthInfoResponse = new NaverInfoResponse(response);

        Member member = Member.builder()
                .id(1L)
                .email(email)
                .roles(new ArrayList<>(List.of("USER")))
                .registrationType(DOMAIN)
                .isTemp(false)
                .build();

        given(oAuthInfoService.request(params)).willReturn(oAuthInfoResponse);
        given(memberService.findOrCreateMember(oAuthInfoResponse)).willReturn(member);

        //when
        SocialLoginResponse socialLoginResponse = oAuthFacade.login(params);

        //then
        assertAll(
                () -> assertEquals(DOMAIN, socialLoginResponse.getRegistrationType()),
                () -> assertEquals(member.getEmail(), socialLoginResponse.getEmail()),
                () -> assertEquals(member.isTemp(), socialLoginResponse.isTemp())
        );

    }

    @Test
    @DisplayName("카카오 로그인 시 소셜인증 실패")
    void fail_kakao_login_failed_auth() {
        //given
        String authorization = "authorization";
        OAuthLoginParams params = new KakaoLoginParams(authorization);

        given(oAuthInfoService.request(params))
                .willThrow(new OAuthException(FAILED_AUTH));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthFacade.login(params));

        //then
        assertEquals(FAILED_AUTH, oAuthException.getErrorCode());

    }

    @Test
    @DisplayName("네이버 로그인 시 소셜인증 실패")
    void fail_naver_login_failed_auth() {
        //given
        String authorization = "authorization";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorization, state);

        given(oAuthInfoService.request(params))
                .willThrow(new OAuthException(FAILED_AUTH));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthFacade.login(params));

        //then
        assertEquals(FAILED_AUTH, oAuthException.getErrorCode());

    }

    @Test
    @DisplayName("카카오 로그인 시 유효하지 않은 토큰")
    void fail_kakao_login_invalid_token() {
        //given
        String authorization = "authorization";
        OAuthLoginParams params = new KakaoLoginParams(authorization);

        given(oAuthInfoService.request(params))
                .willThrow(new OAuthException(INVALID_TOKEN));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthFacade.login(params));

        //then
        assertEquals(INVALID_TOKEN, oAuthException.getErrorCode());

    }

    @Test
    @DisplayName("네이버 로그인 시 유효하지 않은 토큰")
    void fail_naver_login_invalid_token() {
        //given
        String authorization = "authorization";
        String state = "state";
        OAuthLoginParams params = new NaverLoginParams(authorization, state);

        given(oAuthInfoService.request(params))
                .willThrow(new OAuthException(INVALID_TOKEN));

        //when
        OAuthException oAuthException = assertThrows(OAuthException.class,
                () -> oAuthFacade.login(params));

        //then
        assertEquals(INVALID_TOKEN, oAuthException.getErrorCode());

    }
}