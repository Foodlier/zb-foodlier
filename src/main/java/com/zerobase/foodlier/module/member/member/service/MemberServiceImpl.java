package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.*;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;
import static com.zerobase.foodlier.module.member.member.type.RegistrationType.DOMAIN;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Object NOT_CHEF_MEMBER = null;
    private static final String SOCIAL_MEMBER = "소셜회원";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private static final String DEL_PREFIX = "DEL";

    @Override
    public void register(MemberRegisterDto memberRegisterDto) {
        validateRegister(memberRegisterDto);
        memberRepository.save(
                Member.builder()
                        .nickname(memberRegisterDto.getNickname())
                        .email(memberRegisterDto.getEmail())
                        .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                        .phoneNumber(memberRegisterDto.getPhoneNumber())
                        .profileUrl(memberRegisterDto.getProfileUrl())
                        .address(
                                Address.builder()
                                        .roadAddress(memberRegisterDto.getRoadAddress())
                                        .addressDetail(memberRegisterDto.getAddressDetail())
                                        .lat(memberRegisterDto.getLat())
                                        .lnt(memberRegisterDto.getLnt())
                                        .build()
                        )
                        .registrationType(memberRegisterDto.getRegistrationType())
                        .roles(new ArrayList<>(
                                List.of(RoleType.ROLE_USER.name())
                        ))
                        .build()
        );
    }

    /**
     * 작성자 : 이승현, 이종욱
     * 작성일 : 2023-09-24(2023-09-25, 2023-09-26)
     * 이메일과 비밀번호를 받아와서 access token과 refresh token값을 반환해줍니다.
     */
    @Override
    public TokenDto signIn(SignInForm form, Date nowDateTime) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
                .filter(m -> passwordEncoder.matches(form.getPassword(), m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return tokenProvider.createToken(MemberAuthDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .roles(member.getRoles())
                        .build(),
                nowDateTime);
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-24
     * access token의 정보를 통해 redis 서버에 refresh token이 있다면 삭제해줍니다.
     */
    @Override
    public void signOut(String email) {
        tokenProvider.deleteRefreshToken(email);
    }

    @Override
    public String reissue(String refreshToken) {
        String email = tokenProvider.getEmail(refreshToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        return tokenProvider.reissue(refreshToken, member.getRoles(), new Date());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-24
     * 유저 개인 정보를 가져옵니다.
     */
    @Override
    public MemberPrivateProfileResponse getPrivateProfile(String email) {
        Member member = findByEmail(email);

        return MemberPrivateProfileResponse.builder()
                .nickName(member.getNickname())
                .point(member.getPoint())
                .isChef(!Objects.isNull(member.getChefMember()))
                .email(member.getEmail())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    /**
     * 작성자 : 이승현(황태원)
     * 작성일 : 2023-09-24(2023-10-15)
     * 프로필 정보를 수정합니다.
     */
    @Override
    public void updatePrivateProfile(MemberUpdateDto memberUpdateDto, Member member) {
        validateUpdateProfile(memberUpdateDto);

        member.updateNickname(memberUpdateDto.getNickName());
        member.updatePhoneNumber(memberUpdateDto.getPhoneNumber());
        member.updateTemp();
        member.updateAddress(memberUpdateDto);
        member.updateProfileUrl(memberUpdateDto.getProfileUrl());

        memberRepository.save(member);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-04
     * 냉장고를 부탁해 페이지에서, 요리사 입장에서 요청한 주변 요리사를 페이징하여 조회함.
     * 정렬 조건 확장 가능성을 위해서, switch문으로 구현함.
     * 값이 없으면 기본적으로 가까운 거리순으로 반환함.
     */
    @Override
    public ListResponse<RequestedMemberDto> getRequestedMemberList(Long memberId,
                                                                   RequestedOrderingType type,
                                                                   Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        validateGetRequestedMemberList(member);

        return ListResponse.from(memberRepository
                .getRequestedMemberListOrderByType(member.getChefMember().getId(),
                        member.getAddress().getLat(),
                        member.getAddress().getLnt(), pageable, type));
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-08
     * 현재 비밀번호 일치하는지 확인한 후 비밀번호를 새로 입력한 것으로 변경합니다.
     * redis에 존재하는 refresh 토큰을 제거합니다.
     */
    @Override
    public String updatePassword(MemberAuthDto memberAuthDto,
                                 PasswordChangeForm form) {
        Member member = memberRepository.findById(memberAuthDto.getId()).stream()
                .filter(m -> passwordEncoder
                        .matches(form.getCurrentPassword(), m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(form.getNewPassword()));
        memberRepository.save(member);

        tokenProvider.deleteRefreshToken(member.getEmail());

        return "비밀번호 변경 완료";
    }

    @Override
    public String updateRandomPassword(PasswordFindForm form, String newPassword) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
                .filter(m -> m.getPhoneNumber().equals(form.getPhoneNumber()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "비밀번호 재설정 완료.";
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-10
     * 회원 탈퇴 시 닉네임, 이메일, 핸드폰 번호를 랜덤값으로 변경합니다.
     * 멤버의 delete 상태를 true로 변경합니다.
     * refresh 토큰이 존재한다면 토큰을 제거합니다.
     */
    @Override
    public String withdraw(MemberAuthDto memberAuthDto) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String randomCode = generateRandomCode();

        String delNickName = DEL_PREFIX + randomCode;
        String delEmail = DEL_PREFIX + randomCode;
        String delPhoneNumber = DEL_PREFIX + randomCode;
        member.updateNickname(delNickName);
        member.updateEmail(delEmail);
        member.updatePhoneNumber(delPhoneNumber);
        member.deleteMember();
        memberRepository.save(member);

        tokenProvider.deleteRefreshToken(member.getEmail());

        return "회원탈퇴 완료";
    }

    @Override
    public DefaultProfileDtoResponse getDefaultProfile(Long memberId) {
        return memberRepository.getDefaultProfile(memberId);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-12
     * 소셜로그인시 사용자 정보를 찾아 반환합니다. 사용자가 없을 시 임시가입을 진행합니다.
     */
    @Override
    public Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .orElseGet(() -> registerSocialMember(oAuthInfoResponse));
    }

    private Member registerSocialMember(OAuthInfoResponse oAuthInfoResponse) {
        String randomCode = generateRandomCode();
        return memberRepository.save(
                Member.builder()
                        .nickname(SOCIAL_MEMBER + randomCode)
                        .email(oAuthInfoResponse.getEmail())
                        .password(passwordEncoder.encode(randomCode))
                        .registrationType(oAuthInfoResponse.getRegistrationType())
                        .address(Address.builder().build())
                        .phoneNumber(randomCode)
                        .profileUrl(randomCode)
                        .isTemp(true)
                        .roles(new ArrayList<>(
                                List.of(RoleType.ROLE_USER.name())
                        ))
                        .build());
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void checkNickname(String nickname){
        if(memberRepository.existsByNickname(nickname)){
            throw new MemberException(NICKNAME_IS_ALREADY_EXIST);
        }
    }

    @Override
    public void checkPhoneNumber(String phoneNumber){
        if(memberRepository.existsByPhoneNumber(phoneNumber)){
            throw new MemberException(PHONE_NUMBER_IS_ALREADY_EXIST);
        }
    }

    @Override
    public void checkEmail(String email){
        if(memberRepository.existsByEmail(email)){
            throw new MemberException(EMAIL_IS_ALREADY_EXIST);
        }
    }


    //======================= Validates =========================

    private void validateRegister(MemberRegisterDto memberRegisterDto) {
        if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
            throw new MemberException(EMAIL_IS_ALREADY_EXIST);
        }
        if (memberRepository.existsByNickname(memberRegisterDto.getNickname())) {
            throw new MemberException(NICKNAME_IS_ALREADY_EXIST);
        }
        if (memberRepository.existsByPhoneNumber(memberRegisterDto.getPhoneNumber())) {
            throw new MemberException(PHONE_NUMBER_IS_ALREADY_EXIST);
        }
    }

    private void validateUpdateProfile(MemberUpdateDto memberUpdateDto) {
        if (StringUtils.hasText(memberUpdateDto.getNickName())
                && memberRepository.existsByNickname(memberUpdateDto.getNickName())) {
            throw new MemberException(NICKNAME_IS_ALREADY_EXIST);
        }
        if (StringUtils.hasText(memberUpdateDto.getPhoneNumber())
                && memberRepository.existsByPhoneNumber(memberUpdateDto.getPhoneNumber())) {
            throw new MemberException(PHONE_NUMBER_IS_ALREADY_EXIST);
        }
    }

    private void validateGetRequestedMemberList(Member member) {
        if (member.getChefMember() == NOT_CHEF_MEMBER) {
            throw new MemberException(MEMBER_IS_NOT_CHEF);
        }
    }
}