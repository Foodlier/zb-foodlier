package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    private MemberServiceImpl memberService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberServiceImpl(memberRepository,
                passwordEncoder,
                tokenProvider);
    }

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

    @Test
    @DisplayName("로그인 성공")
    void success_signIn() {
        //given
        String password = passwordEncoder.encode("password");

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password(password)
                        .phoneNumber("010-1234-5678")
                        .profileUrl("/image/default.png")
                        .address(
                                Address.builder()
                                        .roadAddress("경기도 성남시 분당구 판교역로 99")
                                        .addressDetail("상세 주소")
                                        .lat(37.1)
                                        .lnt(128.1)
                                        .build()
                        )
                        .registrationType(RegistrationType.DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));
        given(tokenProvider.createToken(any(), any()))
                .willReturn(new TokenDto("accessToken",
                        "refreshToken"));

        //when
        TokenDto tokenDto = memberService.signIn(SignInForm.builder()
                .email("test@test.com")
                .password("password")
                .currentDate(LocalDateTime.now())
                .build());

        //then
        assertAll(
                () -> assertEquals("accessToken",
                        tokenDto.getAccessToken()),
                () -> assertEquals("refreshToken",
                        tokenDto.getRefreshToken())
        );
    }

    @Test
    @DisplayName("로그인 실패 - 이메일에 해당하는 멤버 없음")
    void fail_signIn_email_is_wrong() {
        //given
        given(memberRepository.findByEmail("test2@test.com"))
                .willReturn(Optional.empty());

        //when
        MemberException emailWrong = assertThrows(MemberException.class,
                () -> memberService.signIn(SignInForm.builder()
                        .email("test2@test.com")
                        .password("password")
                        .currentDate(LocalDateTime.now())
                        .build()));


        //then
        assertEquals(MEMBER_NOT_FOUND, emailWrong.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 - 패스워드 불일치")
    void fail_signIn_password_is_wrong() {
        //given
        String password = passwordEncoder.encode("password");

        given(memberRepository.findByEmail("test@test.com"))
                .willReturn(Optional.ofNullable(Member.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password(password)
                        .phoneNumber("010-1234-5678")
                        .profileUrl("/image/default.png")
                        .address(
                                Address.builder()
                                        .roadAddress("경기도 성남시 분당구 판교역로 99")
                                        .addressDetail("상세 주소")
                                        .lat(37.1)
                                        .lnt(128.1)
                                        .build()
                        )
                        .registrationType(RegistrationType.DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //when
        MemberException passwordWrong = assertThrows(MemberException.class,
                () -> memberService.signIn(SignInForm.builder()
                        .email("test@test.com")
                        .password("password1")
                        .currentDate(LocalDateTime.now())
                        .build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, passwordWrong.getErrorCode());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void success_signOut() {
        //given

        //when
        memberService.signOut("test@test.com");

        //then
        verify(tokenProvider, times(1))
                .deleteRefreshToken("test@test.com");
    }

    @Test
    @DisplayName("회원정보 조회 성공")
    void success_get_private_profile() {
        //given
        String password = passwordEncoder.encode("password");

        given(memberRepository.findByEmail("test@test.com"))
                .willReturn(Optional.ofNullable(Member.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password(password)
                        .phoneNumber("010-1234-5678")
                        .profileUrl("/image/default.png")
                        .address(
                                Address.builder()
                                        .roadAddress("경기도 성남시 분당구 판교역로 99")
                                        .addressDetail("상세 주소")
                                        .lat(37.1)
                                        .lnt(128.1)
                                        .build()
                        )
                        .registrationType(RegistrationType.DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //when
        MemberPrivateProfileResponse privateProfile =
                memberService.getPrivateProfile("test@test.com");

        //then
        assertAll(
                () -> assertEquals("test",
                        privateProfile.getNickName()),
                () -> assertEquals("test@test.com",
                        privateProfile.getEmail()),
                () -> assertEquals("010-1234-5678",
                        privateProfile.getPhoneNumber()),
                () -> assertEquals("경기도 성남시 분당구 판교역로 99",
                        privateProfile.getAddress().getRoadAddress()),
                () -> assertEquals("상세 주소",
                        privateProfile.getAddress().getAddressDetail()),
                () -> assertEquals(37.1,
                        privateProfile.getAddress().getLat()),
                () -> assertEquals(128.1,
                        privateProfile.getAddress().getLnt()),
                () -> assertEquals("/image/default.png",
                        privateProfile.getProfileUrl())
        );
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void success_update_private_profile() {
        //given
        String password = passwordEncoder.encode("password");

        Member member = Member.builder()
                .nickname("test")
                .email("test@test.com")
                .password(password)
                .phoneNumber("010-1234-5678")
                .profileUrl("/image/default.png")
                .address(
                        Address.builder()
                                .roadAddress("경기도 성남시 분당구 판교역로 99")
                                .addressDetail("상세 주소")
                                .lat(37.1)
                                .lnt(128.1)
                                .build()
                )
                .registrationType(RegistrationType.DOMAIN)
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();

        given(memberRepository.existsByNickname(anyString()))
                .willReturn(false);
        given(memberRepository.existsByPhoneNumber(anyString()))
                .willReturn(false);
        given(memberRepository.save(any()))
                .willReturn(member);

        //when
        memberService.updatePrivateProfile(MemberUpdateDto.builder()
                .nickName("test2")
                .profileUrl("https://test.s3/s3Image.png")
                .phoneNumber("010-8765-4321")
                .roadAddress("경기도 연천군 청산면 청창로 471")
                .addressDetail("신교대")
                .lat(38.0)
                .lnt(127.1)
                .build(), member);

        //then
        verify(memberRepository, times(1))
                .save(member);

        assertAll(
                () -> assertEquals("test2",
                        member.getNickname()),
                () -> assertEquals("https://test.s3/s3Image.png",
                        member.getProfileUrl()),
                () -> assertEquals("010-8765-4321",
                        member.getPhoneNumber()),
                () -> assertEquals("경기도 연천군 청산면 청창로 471",
                        member.getAddress().getRoadAddress()),
                () -> assertEquals("신교대",
                        member.getAddress().getAddressDetail()),
                () -> assertEquals(38.0,
                        member.getAddress().getLat()),
                () -> assertEquals(127.1,
                        member.getAddress().getLnt())
        );
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 닉네임 중복")
    void fail_update_private_profile_nickname_is_already_exist() {
        //given
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(true);

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updatePrivateProfile(MemberUpdateDto.builder()
                        .nickName("test")
                        .profileUrl("https://test.s3/s3Image.png")
                        .phoneNumber("010-8765-4321")
                        .roadAddress("경기도 연천군 청산면 청창로 471")
                        .addressDetail("신교대")
                        .lat(38.0)
                        .lnt(127.1)
                        .build(), Member.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password("password")
                        .phoneNumber("010-1234-5678")
                        .profileUrl("/image/default.png")
                        .address(
                                Address.builder()
                                        .roadAddress("경기도 성남시 분당구 판교역로 99")
                                        .addressDetail("상세 주소")
                                        .lat(37.1)
                                        .lnt(128.1)
                                        .build()
                        )
                        .registrationType(RegistrationType.DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //then
        assertEquals(NICKNAME_IS_ALREADY_EXIST,
                memberException.getErrorCode());
    }

    @Test
    @DisplayName("회원정보 수정 실패 - 전화번호 중복")
    void fail_update_private_profile_phoneNumber_is_already_exist() {
        //given
        given(memberRepository.existsByNickname(anyString()))
                .willReturn(false);
        given(memberRepository.existsByPhoneNumber(anyString()))
                .willReturn(true);

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updatePrivateProfile(MemberUpdateDto.builder()
                        .nickName("test2")
                        .profileUrl("https://test.s3/s3Image.png")
                        .phoneNumber("010-1234-5678")
                        .roadAddress("경기도 연천군 청산면 청창로 471")
                        .addressDetail("신교대")
                        .lat(38.0)
                        .lnt(127.1)
                        .build(), Member.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password("password")
                        .phoneNumber("010-1234-5678")
                        .profileUrl("/image/default.png")
                        .address(
                                Address.builder()
                                        .roadAddress("경기도 성남시 분당구 판교역로 99")
                                        .addressDetail("상세 주소")
                                        .lat(37.1)
                                        .lnt(128.1)
                                        .build()
                        )
                        .registrationType(RegistrationType.DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //then
        assertEquals(PHONE_NUMBER_IS_ALREADY_EXIST,
                memberException.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void success_updatePassword() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .id(1L)
                        .email("test@test.com")
                        .password(passwordEncoder.encode("1"))
                        .build()));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        //when
        memberService.updatePassword(MemberAuthDto.builder()
                .id(1L)
                .email("test@test.com")
                .build(), PasswordChangeForm.builder()
                .currentPassword("1")
                .newPassword("123")
                .build());

        //then
        verify(tokenProvider, times(1))
                .deleteRefreshToken("test@test.com");
        verify(memberRepository, times(1))
                .save(captor.capture());

        assertTrue(passwordEncoder
                .matches("123", captor.getValue().getPassword()));
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 회원을 찾을 수 없음")
    void fail_updatePassword_memberNotFound() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updatePassword(MemberAuthDto.builder()
                        .id(1L)
                        .email("test@test.com")
                        .build(), PasswordChangeForm.builder()
                        .currentPassword("1")
                        .newPassword("123")
                        .build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }
}