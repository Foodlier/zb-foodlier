package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.*;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.social.dto.KakaoInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.NaverInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.MALFORMED_JWT_REQUEST;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.REFRESH_TOKEN_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.*;
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
                .registrationType(DOMAIN)
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
                        .registrationType(DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));
        given(tokenProvider.createToken(any(), any()))
                .willReturn(new TokenDto("accessToken",
                        "refreshToken"));

        //when
        TokenDto tokenDto = memberService.signIn(SignInForm.builder()
                .email("test@test.com")
                .password("password")
                .build(), new Date());

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
                        .build(), new Date()));


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
                        .registrationType(DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //when
        MemberException passwordWrong = assertThrows(MemberException.class,
                () -> memberService.signIn(SignInForm.builder()
                        .email("test@test.com")
                        .password("password1")
                        .build(), new Date()));

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
                        .point(1000)
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
                        .registrationType(DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //when
        MemberPrivateProfileResponse privateProfile =
                memberService.getPrivateProfile("test@test.com");

        //then
        assertAll(
                () -> assertEquals("test",
                        privateProfile.getNickName()),
                () -> assertEquals(1000,
                        privateProfile.getPoint()),
                () -> assertEquals(false,
                        privateProfile.getIsChef()),
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
                .registrationType(DOMAIN)
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
                        .registrationType(DOMAIN)
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
                        .registrationType(DOMAIN)
                        .roles(List.of(RoleType.ROLE_USER.name()))
                        .build()));

        //then
        assertEquals(PHONE_NUMBER_IS_ALREADY_EXIST,
                memberException.getErrorCode());
    }

    @Test
    @DisplayName("주변 요청자 조회 성공 - 가까운 거리순 정렬")
    void success_getRequestedMemberList_order_by_distance() {
        //given
        ChefMember chefMember = ChefMember.builder().build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(3L)
                        .chefMember(chefMember)
                        .address(Address.builder()
                                .lat(37.5)
                                .lnt(127.5)
                                .build())
                        .build()));

        RequestedMemberDto memberOne = getMemberDtoOne();
        RequestedMemberDto memberTwo = getMemberDtoTwo();

        given(memberRepository.getRequestedMemberListOrderByType(
                any(), anyDouble(), anyDouble(), any(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        memberTwo,
                                        memberOne
                                )
                        )
                )
        );

        //when
        ListResponse<RequestedMemberDto> responseList = memberService
                .getRequestedMemberList(3L,
                        RequestedOrderingType.DISTANCE, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(memberTwo.getMemberId(), responseList.getContent().get(0).getMemberId()),
                () -> assertEquals(memberTwo.getProfileUrl(), responseList.getContent().get(0).getProfileUrl()),
                () -> assertEquals(memberTwo.getNickname(), responseList.getContent().get(0).getNickname()),
                () -> assertEquals(memberTwo.getDistance(), responseList.getContent().get(0).getDistance()),
                () -> assertEquals(memberTwo.getLat(), responseList.getContent().get(0).getLat()),
                () -> assertEquals(memberTwo.getLnt(), responseList.getContent().get(0).getLnt()),
                () -> assertEquals(memberTwo.getRequestId(), responseList.getContent().get(0).getRequestId()),
                () -> assertEquals(memberTwo.getTitle(), responseList.getContent().get(0).getTitle()),
                () -> assertEquals(memberTwo.getContent(), responseList.getContent().get(0).getContent()),
                () -> assertEquals(memberTwo.getMainImageUrl(), responseList.getContent().get(0).getMainImageUrl()),

                () -> assertEquals(memberOne.getMemberId(), responseList.getContent().get(1).getMemberId()),
                () -> assertEquals(memberOne.getProfileUrl(), responseList.getContent().get(1).getProfileUrl()),
                () -> assertEquals(memberOne.getNickname(), responseList.getContent().get(1).getNickname()),
                () -> assertEquals(memberOne.getDistance(), responseList.getContent().get(1).getDistance()),
                () -> assertEquals(memberOne.getLat(), responseList.getContent().get(1).getLat()),
                () -> assertEquals(memberOne.getLnt(), responseList.getContent().get(1).getLnt()),
                () -> assertEquals(memberOne.getRequestId(), responseList.getContent().get(1).getRequestId()),
                () -> assertEquals(memberOne.getTitle(), responseList.getContent().get(1).getTitle()),
                () -> assertEquals(memberOne.getContent(), responseList.getContent().get(1).getContent()),
                () -> assertEquals(memberOne.getMainImageUrl(), responseList.getContent().get(1).getMainImageUrl())
        );
    }

    @Test
    @DisplayName("주변 요청자 조회 성공 - 적은 희망가격순 정렬")
    void success_getRequestedMemberList_order_by_expected_price() {
        //given
        ChefMember chefMember = ChefMember.builder().build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(3L)
                        .chefMember(chefMember)
                        .address(Address.builder()
                                .lat(37.5)
                                .lnt(127.5)
                                .build())
                        .build()));

        RequestedMemberDto memberOne = getMemberDtoOne();
        RequestedMemberDto memberTwo = getMemberDtoTwo();

        given(memberRepository.getRequestedMemberListOrderByType(
                any(), anyDouble(), anyDouble(), any(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        memberOne,
                                        memberTwo
                                )
                        )
                )
        );

        //when
        ListResponse<RequestedMemberDto> responseList = memberService
                .getRequestedMemberList(3L,
                        RequestedOrderingType.PRICE, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(memberOne.getMemberId(), responseList.getContent().get(0).getMemberId()),
                () -> assertEquals(memberOne.getProfileUrl(), responseList.getContent().get(0).getProfileUrl()),
                () -> assertEquals(memberOne.getNickname(), responseList.getContent().get(0).getNickname()),
                () -> assertEquals(memberOne.getDistance(), responseList.getContent().get(0).getDistance()),
                () -> assertEquals(memberOne.getLat(), responseList.getContent().get(0).getLat()),
                () -> assertEquals(memberOne.getLnt(), responseList.getContent().get(0).getLnt()),
                () -> assertEquals(memberOne.getRequestId(), responseList.getContent().get(0).getRequestId()),
                () -> assertEquals(memberOne.getTitle(), responseList.getContent().get(0).getTitle()),
                () -> assertEquals(memberTwo.getContent(), responseList.getContent().get(0).getContent()),
                () -> assertEquals(memberOne.getMainImageUrl(), responseList.getContent().get(0).getMainImageUrl()),

                () -> assertEquals(memberTwo.getMemberId(), responseList.getContent().get(1).getMemberId()),
                () -> assertEquals(memberTwo.getProfileUrl(), responseList.getContent().get(1).getProfileUrl()),
                () -> assertEquals(memberTwo.getNickname(), responseList.getContent().get(1).getNickname()),
                () -> assertEquals(memberTwo.getDistance(), responseList.getContent().get(1).getDistance()),
                () -> assertEquals(memberTwo.getLat(), responseList.getContent().get(1).getLat()),
                () -> assertEquals(memberTwo.getLnt(), responseList.getContent().get(1).getLnt()),
                () -> assertEquals(memberTwo.getRequestId(), responseList.getContent().get(1).getRequestId()),
                () -> assertEquals(memberTwo.getTitle(), responseList.getContent().get(1).getTitle()),
                () -> assertEquals(memberOne.getContent(), responseList.getContent().get(1).getContent()),
                () -> assertEquals(memberTwo.getMainImageUrl(), responseList.getContent().get(1).getMainImageUrl())
        );
    }

    @Test
    @DisplayName("주변 요청자 조회 실패 - 회원 X")
    void fail_getRequestedMemberList_member_not_found() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService
                        .getRequestedMemberList(3L,
                                RequestedOrderingType.PRICE,
                                PageRequest.of(0, 10)));
        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("주변 요청자 조회 실패 - 요리사가 아님")
    void fail_getRequestedMemberList_member_is_not_chef() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(3L)
                        .address(Address.builder()
                                .lat(37.5)
                                .lnt(127.5)
                                .build())
                        .build()));

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService
                        .getRequestedMemberList(3L,
                                RequestedOrderingType.PRICE,
                                PageRequest.of(0, 10)));
        //then
        assertEquals(MEMBER_IS_NOT_CHEF, exception.getErrorCode());
    }

    // 테스트에 사용될 객체 정의
    private RequestedMemberDto getMemberDtoOne() {
        return RequestedMemberDto.builder()
                .memberId(1L)
                .profileUrl("https://s3.test.com/image1.png")
                .nickname("person1")
                .distance(0.5)
                .lat(37.1)
                .lnt(127.1)
                .requestId(1L)
                .expectedPrice(12000L)
                .title("살려주세요")
                .content("야호")
                .mainImageUrl("https://s3.test.com/main1.png")
                .build();
    }

    private RequestedMemberDto getMemberDtoTwo() {
        return RequestedMemberDto.builder()
                .memberId(2L)
                .profileUrl("https://s3.test.com/image2.png")
                .nickname("person2")
                .distance(0.1)
                .lat(37.2)
                .lnt(127.2)
                .requestId(2L)
                .expectedPrice(30000L)
                .title("살려주세요?")
                .content("야호")
                .mainImageUrl("https://s3.test.com/main2.png")
                .build();
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

    @Test
    @DisplayName("비밀번호 재설정 성공")
    void success_updateRandomPassword() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .id(1L)
                        .email("test@test.com")
                        .phoneNumber("01012345678")
                        .build()));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        //when
        memberService.updateRandomPassword(PasswordFindForm.builder()
                .email("test@test.com")
                .phoneNumber("01012345678")
                .build(), "newPassword");

        //then
        verify(memberRepository, times(1))
                .save(captor.capture());

        assertTrue(passwordEncoder.matches("newPassword",
                captor.getValue().getPassword()));
    }

    @Test
    @DisplayName("비밀번호 재설정 실패 - 회원을 찾을 수 없음")
    void fail_updateRandomPassword_memberNotFound() {
        //given
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.updateRandomPassword(PasswordFindForm.builder()
                        .email("test@test.com")
                        .phoneNumber("01012345678")
                        .build(), "newPassword"));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void success_withdraw() {
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("test")
                .phoneNumber("01012345678")
                .email("test@test.com")
                .isDeleted(false)
                .build();
        String delPreFix = "DEL";

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        //when
        memberService.withdraw(MemberAuthDto.builder()
                .id(1L)
                .email("test@test.com")
                .build());

        //then
        verify(memberRepository, times(1))
                .save(captor.capture());
        verify(tokenProvider, times(1))
                .deleteRefreshToken(Objects.requireNonNull(member).getEmail());

        Member value = captor.getValue();
        assertAll(
                () -> assertEquals(1L, value.getId()),
                () -> assertTrue(value.getEmail().startsWith(delPreFix)),
                () -> assertTrue(value.getNickname().startsWith(delPreFix)),
                () -> assertTrue(value.getPhoneNumber().startsWith(delPreFix)),
                () -> assertTrue(value.isDeleted())
        );
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 회원을 찾을 수 없음")
    void fail_withdraw_memberNotFound() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.withdraw(MemberAuthDto.builder()
                        .id(1L)
                        .email("test@test.com")
                        .build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("접속 토큰 재발급 성공")
    void success_reissue() {

        // given

        Member member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.toString()))
                .build();
        String expectedAccessToken = "expectedAccessToken";
        String refreshToken = "refreshToken";
        given(tokenProvider.getEmail(anyString()))
                .willReturn(member.getEmail());
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        given(tokenProvider.reissue(anyString(), any(), any()))
                .willReturn(expectedAccessToken);
        // when
        String reissuedAccessToken = memberService.reissue(refreshToken);

        // then
        verify(tokenProvider, times(1)).getEmail(anyString());
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(tokenProvider, times(1)).reissue(anyString(), any(), any());
        assertEquals(reissuedAccessToken, expectedAccessToken);
    }

    @Test
    @DisplayName("접속 토큰 재발급 실패 - 올바르지 않은 토큰인 경우")
    void fail_reissue_invalid_token() {

        // given

        String refreshToken = "refreshToken";
        given(tokenProvider.getEmail(anyString()))
                .willThrow(new MalformedJwtException(MALFORMED_JWT_REQUEST.getDescription()));

        // when
        MalformedJwtException malformedJwtException = assertThrows(MalformedJwtException.class,
                () -> memberService.reissue(refreshToken));

        // then
        verify(tokenProvider, times(1)).getEmail(anyString());
        assertEquals(malformedJwtException.getMessage(), MALFORMED_JWT_REQUEST.getDescription());
    }

    @Test
    @DisplayName("접속 토큰 재발급 실패 - 존재하지 않는 회원인 경우")
    void fail_reissue_no_such_member() {

        // given

        String refreshToken = "refreshToken";
        given(tokenProvider.getEmail(anyString()))
                .willReturn("test@gmail.com");
        given(memberRepository.findByEmail(anyString()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));
        // when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberService.reissue(refreshToken));

        // then
        verify(tokenProvider, times(1)).getEmail(anyString());
        verify(memberRepository, times(1)).findByEmail(anyString());
        assertEquals(memberException.getErrorCode(), MEMBER_NOT_FOUND);
        assertEquals(memberException.getDescription(), MEMBER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("접속 토큰 재발급 실패 - 재발급 토큰이 redis 저장소에 존재하지 않는 경우")
    void fail_reissue_refresh_token_not_found() {

        // given

        Member member = Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.toString()))
                .build();

        String refreshToken = "refreshToken";
        given(tokenProvider.getEmail(anyString()))
                .willReturn(member.getEmail());
        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));
        given(tokenProvider.reissue(anyString(), any(), any()))
                .willThrow(new JwtException(REFRESH_TOKEN_NOT_FOUND));

        // when
        JwtException jwtException = assertThrows(JwtException.class,
                () -> memberService.reissue(refreshToken));

        // then
        verify(tokenProvider, times(1)).getEmail(anyString());
        verify(memberRepository, times(1)).findByEmail(anyString());
        verify(tokenProvider, times(1)).reissue(anyString(), any(), any());
        assertEquals(jwtException.getErrorCode(), REFRESH_TOKEN_NOT_FOUND);
        assertEquals(jwtException.getDescription(), REFRESH_TOKEN_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("기본 공개 프로필 가져오기 성공")
    void success_getDefaultProfile() {

        //given
        DefaultProfileDtoResponse defaultProfileDtoResponse =
                DefaultProfileDtoResponse.builder()
                        .memberId(1L)
                        .nickname("요리의신")
                        .profileUrl("http://s3.test.com")
                        .receivedHeart(300)
                        .isChef(true)
                        .chefMemberId(1L)
                        .build();

        given(memberRepository.getDefaultProfile(anyLong()))
                .willReturn(defaultProfileDtoResponse);

        //when
        DefaultProfileDtoResponse response = memberService.getDefaultProfile(1L);

        //then
        assertAll(
                () -> assertEquals(defaultProfileDtoResponse.getMemberId(), response.getMemberId()),
                () -> assertEquals(defaultProfileDtoResponse.getNickname(), response.getNickname()),
                () -> assertEquals(defaultProfileDtoResponse.getProfileUrl(), response.getProfileUrl()),
                () -> assertEquals(defaultProfileDtoResponse.getReceivedHeart(), response.getReceivedHeart()),
                () -> assertEquals(defaultProfileDtoResponse.getIsChef(), response.getIsChef()),
                () -> assertEquals(defaultProfileDtoResponse.getChefMemberId(), response.getChefMemberId())
        );

    }

    @Test
    @DisplayName("카카오 소셜회원 정보찾기 성공")
    void success_kakao_find_member() {

        //given
        Member expectedMember = Member.builder()
                .nickname("nickname")
                .email("email")
                .registrationType(KAKAO)
                .build();
        String nickname = "nickname";
        String email = "email";
        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse oAuthInfoResponse = new KakaoInfoResponse(kakaoAccount);

        given(memberRepository.findByEmail(oAuthInfoResponse.getEmail()))
                .willReturn(Optional.ofNullable(expectedMember));

        //when
        Member member = memberService.findOrCreateMember(oAuthInfoResponse);

        //then
        assertAll(
                () -> assertEquals(expectedMember.getNickname(), member.getNickname()),
                () -> assertEquals(expectedMember.getEmail(), member.getEmail()),
                () -> assertEquals(expectedMember.getRegistrationType(),
                        member.getRegistrationType())
        );
    }

    @Test
    @DisplayName("네이버 소셜회원 정보찾기 성공")
    void success_naver_find_member() {

        //given
        Member expectedMember = Member.builder()
                .nickname("nickname")
                .email("email")
                .registrationType(NAVER)
                .build();
        String nickname = "nickname";
        String email = "email";
        NaverInfoResponse.Response response =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse oAuthInfoResponse = new NaverInfoResponse(response);

        given(memberRepository.findByEmail(oAuthInfoResponse.getEmail()))
                .willReturn(Optional.ofNullable(expectedMember));

        //when
        Member member = memberService.findOrCreateMember(oAuthInfoResponse);

        //then
        assertAll(
                () -> assertEquals(expectedMember.getNickname(), member.getNickname()),
                () -> assertEquals(expectedMember.getEmail(), member.getEmail()),
                () -> assertEquals(expectedMember.getRegistrationType(),
                        member.getRegistrationType())
        );
    }

    @Test
    @DisplayName("카카오 회원 생성 성공")
    void success_kakao_create_member() {

        //given
        Member expectedMember = Member.builder()
                .email("email")
                .registrationType(KAKAO)
                .build();
        String nickname = "nickname";
        String email = "email";
        KakaoInfoResponse.KakaoProfile kakaoProfile =
                new KakaoInfoResponse.KakaoProfile(nickname);
        KakaoInfoResponse.KakaoAccount kakaoAccount =
                new KakaoInfoResponse.KakaoAccount(kakaoProfile, email);
        OAuthInfoResponse oAuthInfoResponse = new KakaoInfoResponse(kakaoAccount);


        given(memberRepository.findByEmail(oAuthInfoResponse.getEmail()))
                .willReturn(Optional.empty());

        //when
        memberService.findOrCreateMember(oAuthInfoResponse);

        //then
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(captor.capture());
        Member member = captor.getValue();

        assertAll(
                () -> assertEquals(expectedMember.getEmail(), member.getEmail()),
                () -> assertEquals(expectedMember.getRegistrationType(),
                        member.getRegistrationType())
        );

    }

    @Test
    @DisplayName("네이버 회원 생성 성공")
    void success_naver_create_member() {

        //given
        Member expectedMember = Member.builder()
                .email("email")
                .registrationType(NAVER)
                .build();
        String nickname = "nickname";
        String email = "email";
        NaverInfoResponse.Response response =
                new NaverInfoResponse.Response(email, nickname);
        OAuthInfoResponse oAuthInfoResponse = new NaverInfoResponse(response);


        given(memberRepository.findByEmail(oAuthInfoResponse.getEmail()))
                .willReturn(Optional.empty());

        //when
        memberService.findOrCreateMember(oAuthInfoResponse);

        //then
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(captor.capture());
        Member member = captor.getValue();

        assertAll(
                () -> assertEquals(expectedMember.getEmail(), member.getEmail()),
                () -> assertEquals(expectedMember.getRegistrationType(),
                        member.getRegistrationType())
        );

    }

    @Test
    @DisplayName("닉네임 중복 체크 - 사용가능")
    void success_checkNickname(){
        //given
        given(memberRepository.existsByNickname(eq("nickname")))
                .willReturn(false);

        //when
        memberService.checkNickname("nickname");

        //then
        verify(memberRepository, times(1))
                .existsByNickname(eq("nickname"));
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 이미 사용중")
    void fail_checkNickname_nickname_is_already_exists(){
        //given
        given(memberRepository.existsByNickname(eq("nickname")))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.checkNickname("nickname"));

        //then
        assertEquals(NICKNAME_IS_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("핸드폰 번호 중복 체크 - 사용가능")
    void success_checkphoneNumber(){
        //given
        given(memberRepository.existsByPhoneNumber(eq("01011112222")))
                .willReturn(false);

        //when
        memberService.checkPhoneNumber("01011112222");

        //then
        verify(memberRepository, times(1))
                .existsByPhoneNumber(eq("01011112222"));
    }

    @Test
    @DisplayName("핸드폰 번호 중복 체크 - 이미 사용중")
    void fail_checkPhoneNumber_phone_number_is_already_exists(){
        //given
        given(memberRepository.existsByPhoneNumber(eq("01011112222")))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.checkPhoneNumber("01011112222"));

        //then
        assertEquals(PHONE_NUMBER_IS_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 중복 체크 - 사용가능")
    void success_checkEmail(){
        //given
        given(memberRepository.existsByEmail(eq("test@test.com")))
                .willReturn(false);

        //when
        memberService.checkEmail("test@test.com");

        //then
        verify(memberRepository, times(1))
                .existsByEmail(eq("test@test.com"));
    }

    @Test
    @DisplayName("이메일 중복 체크 - 이미 사용중")
    void fail_checkEmail_email_is_already_exists(){
        //given
        given(memberRepository.existsByEmail(eq("test@test.com")))
                .willReturn(true);

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> memberService.checkEmail("test@test.com"));

        //then
        assertEquals(EMAIL_IS_ALREADY_EXIST, exception.getErrorCode());
    }

}