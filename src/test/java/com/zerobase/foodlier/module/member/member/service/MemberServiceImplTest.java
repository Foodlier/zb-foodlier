package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider tokenProvider;
    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원 가입 성공")
    void success_register() {

        //given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(false);
        given(memberRepository.existsByPhoneNumber(anyString()))
                .willReturn(false);
        given(memberRepository.save(any()))
                .willReturn(Member.builder().build());

        //when
        memberService.register(MemberRegisterDto.builder()
                        .nickname("nickname")
                        .profileUrl("/image/default.png")
                        .email("test@test.com")
                        .password("password")
                        .phoneNumber("010-1111-1111")
                        .roadAddress("경기도 성남시 분당구 판교역로 9999")
                        .addressDetail("상세 주소")
                        .lat(37.1)
                        .lnt(128.1)
                        .registrationType(RegistrationType.DOMAIN)
                .build());

        //then
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(captor.capture());
        Member member = captor.getValue();

        assertAll(
                () -> assertEquals("nickname", member.getNickname()),
                () -> assertEquals("/image/default.png", member.getProfileUrl()),
                () -> assertEquals("test@test.com", member.getEmail()),
                () -> assertEquals("경기도 성남시 분당구 판교역로 9999", member.getAddress().getRoadAddress()),
                () -> assertEquals("상세 주소", member.getAddress().getAddressDetail()),
                () -> assertEquals(37.1, member.getAddress().getLat()),
                () -> assertEquals(128.1, member.getAddress().getLnt())
        );

    }

    @Test
    @DisplayName("회원 가입 실패 - 동일 이메일 존재")
    void fail_register_email_is_already_exist() {

        //given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.register(
                        MemberRegisterDto.builder()
                                .nickname("nickname")
                                .email("test@test.com")
                                .password("password")
                                .phoneNumber("010-1111-1111")
                                .build())
                );

        //then
        assertEquals(EMAIL_IS_ALREADY_EXIST, exception.getErrorCode());

    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 닉네임이 존재")
    void fail_register_nickname_is_already_exist() {

        //given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.register(
                        MemberRegisterDto.builder()
                                .nickname("nickname")
                                .email("test@test.com")
                                .password("password")
                                .phoneNumber("010-1111-1111")
                                .build())
        );

        //then
        assertEquals(NICKNAME_IS_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 핸드폰 번호가 존재")
    void fail_register_phoneNumber_is_already_exist() {

        //given
        given(memberRepository.existsByEmail(anyString()))
                .willReturn(false);
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(false);
        given(memberRepository.existsByPhoneNumber(anyString()))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.register(
                        MemberRegisterDto.builder()
                                .nickname("nickname")
                                .email("test@test.com")
                                .password("password")
                                .phoneNumber("010-1111-1111")
                                .build())
        );

        //then
        assertEquals(PHONE_NUMBER_IS_ALREADY_EXIST, exception.getErrorCode());

    }
}