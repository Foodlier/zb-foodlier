package com.zerobase.foodlier.global.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import com.zerobase.foodlier.global.member.oAuth.facade.OAuthFacade;
import com.zerobase.foodlier.global.member.password.facade.PasswordFindFacade;
import com.zerobase.foodlier.global.member.regiser.facade.MemberRegisterFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.social.dto.KakaoLoginParams;
import com.zerobase.foodlier.module.member.member.social.dto.NaverLoginParams;
import com.zerobase.foodlier.module.member.member.social.dto.SocialLoginResponse;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class AuthControllerTest {
    @MockBean
    private EmailVerificationFacade emailVerificationFacade;
    @MockBean
    private EmailVerificationService emailVerificationService;
    @MockBean
    private MemberRegisterFacade memberRegisterFacade;
    @MockBean
    private MemberService memberService;
    @MockBean
    private PasswordFindFacade passwordFindFacade;
    @MockBean
    private OAuthFacade oAuthFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("인증 코드 이메일 전송 성공")
    void success_sendVerificationCode() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(post("/auth/verification/send/test@test.com")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("인증 코드 전송 완료")
                );

        verify(emailVerificationFacade, times(1))
                .sendMailAndCreateVerification(anyString(), any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("인증 코드 인증 성공")
    void success_verifyEmail() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(post("/auth/verify?email=test@test.com&verificationCode=verificationCode")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("인증 완료")
                );

        verify(emailVerificationService, times(1))
                .verify(anyString(), anyString(), any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 가입 성공")
    void success_signUp() throws Exception {
        //given
        String profileImage = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");
        MemberInputDto memberInputDto = MemberInputDto.builder()
                .nickname("test")
                .profileImage(new MockMultipartFile(profileImage,
                        profileImage, MediaType.IMAGE_PNG_VALUE, new FileInputStream(file)))
                .email("test@test.com")
                .password("Aa12345!")
                .phoneNumber("01012345678")
                .roadAddress("서울특별시 강남구 테헤란로 131")
                .addressDetail("토스 본사")
                .build();

        //when
        ResultActions perform = mockMvc.perform(multipart("/auth/signup")
                .file("profileImage",
                        memberInputDto.getProfileImage().getBytes())
                .param("nickname", memberInputDto.getNickname())
                .param("email", memberInputDto.getEmail())
                .param("password", memberInputDto.getPassword())
                .param("phoneNumber", memberInputDto.getPhoneNumber())
                .param("roadAddress", memberInputDto.getRoadAddress())
                .param("addressDetail", memberInputDto.getAddressDetail())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("회원가입 완료")
                );

        verify(memberRegisterFacade, times(1))
                .domainRegister(any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그인 성공")
    void success_signIn() throws Exception {
        //given
        SignInForm signInForm = SignInForm.builder()
                .email("test@test.com")
                .password("Aa12345!")
                .build();
        TokenDto tokenDto = new TokenDto("accessToken", "refreshToken");


        given(memberService.signIn(any(), any()))
                .willReturn(tokenDto);

        //when
        ResultActions perform = mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInForm))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.accessToken")
                                .value(tokenDto.getAccessToken()),
                        jsonPath("$.refreshToken")
                                .value(tokenDto.getRefreshToken())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("로그아웃 성공")
    void success_signOut() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(post("/auth/signout")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("로그아웃 되었습니다.")
                );

        verify(memberService, times(1))
                .signOut(anyString());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("비밀번호 찾기 성공")
    void success_passwordFind() throws Exception {
        //given
        PasswordFindForm passwordFindForm = PasswordFindForm.builder()
                .email("test@test.com")
                .phoneNumber("01012345678")
                .build();

        given(passwordFindFacade.sendMailAndUpdateNewPassword(any()))
                .willReturn("newPassword");

        //when
        ResultActions perform = mockMvc.perform(post("/auth/findPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordFindForm))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("newPassword")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("토큰 재발급 성공")
    void success_reissue() throws Exception {
        //given
        given(memberService.reissue(anyString()))
                .willReturn("accessToken");

        //when
        ResultActions perform = mockMvc.perform(post("/auth/reissue")
                .header("RefreshToken", "refreshToken")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("accessToken")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 탈퇴 성공")
    void success_withdraw() throws Exception {
        //given
        given(memberService.withdraw(any()))
                .willReturn("회원탈퇴 완료");

        //when
        ResultActions perform = mockMvc.perform(delete("/auth/withdraw")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("회원탈퇴 완료")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("카카오 소셜 로그인 성공")
    void success_loginKakao() throws Exception {
        //given
        KakaoLoginParams kakaoLoginParams =
                new KakaoLoginParams("authorizationCode");
        SocialLoginResponse socialLoginResponse = SocialLoginResponse.builder()
                .registrationType(RegistrationType.KAKAO)
                .email("test@test.com")
                .isTemp(true)
                .tokenDto(new TokenDto("accessToken", "refreshToken"))
                .build();

        given(oAuthFacade.login(any()))
                .willReturn(socialLoginResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/auth/oauth2/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kakaoLoginParams))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.registrationType")
                                .value(socialLoginResponse.getRegistrationType().name()),
                        jsonPath("$.temp")
                                .value(socialLoginResponse.isTemp()),
                        jsonPath("$.email")
                                .value(socialLoginResponse.getEmail()),
                        jsonPath("$.tokenDto.accessToken")
                                .value(socialLoginResponse.getTokenDto().getAccessToken()),
                        jsonPath("$.tokenDto.refreshToken")
                                .value(socialLoginResponse.getTokenDto().getRefreshToken())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("네이버 소셜 로그인 성공")
    void success_loginNaver() throws Exception {
        //given
        NaverLoginParams naverLoginParams = new NaverLoginParams("authorizationCode", "state");
        SocialLoginResponse socialLoginResponse = SocialLoginResponse.builder()
                .registrationType(RegistrationType.NAVER)
                .email("test@test.com")
                .isTemp(true)
                .tokenDto(new TokenDto("accessToken", "refreshToken"))
                .build();

        given(oAuthFacade.login(any()))
                .willReturn(socialLoginResponse);

        //when
        ResultActions perform = mockMvc.perform(post("/auth/oauth2/naver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(naverLoginParams))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.registrationType")
                                .value(socialLoginResponse.getRegistrationType().name()),
                        jsonPath("$.temp")
                                .value(socialLoginResponse.isTemp()),
                        jsonPath("$.email")
                                .value(socialLoginResponse.getEmail()),
                        jsonPath("$.tokenDto.accessToken")
                                .value(socialLoginResponse.getTokenDto().getAccessToken()),
                        jsonPath("$.tokenDto.refreshToken")
                                .value(socialLoginResponse.getTokenDto().getRefreshToken())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("닉네임 중복 확인 성공")
    void success_checkNickname() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(get("/auth/check/nickname?nickname=nickname"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("사용가능한 닉네임 입니다.")
                );

        verify(memberService, times(1))
                .checkNickname(anyString());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("전화번호 중복 확인 성공")
    void success_checkPhoneNumber() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(get("/auth/check/phone?phoneNumber=01012345678"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("사용가능한 전화번호 입니다.")
                );

        verify(memberService, times(1))
                .checkPhoneNumber(anyString());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("이메일 중복 확인 성공")
    void success_checkEmail() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(get("/auth/check/email?email=test@test.com"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("사용가능한 이메일 입니다.")
                );

        verify(memberService, times(1))
                .checkEmail(anyString());
    }
}