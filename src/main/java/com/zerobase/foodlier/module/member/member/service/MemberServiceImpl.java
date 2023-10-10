package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Object NOT_CHEF_MEMBER = null;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

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
    public TokenDto signIn(SignInForm form) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
                .filter(m -> passwordEncoder.matches(form.getPassword(), m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return tokenProvider.createToken(MemberAuthDto.builder()
                        .id(member.getId())
                        .email(member.getEmail())
                        .roles(member.getRoles())
                        .build(),
                form.getCurrentDate());
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
                .email(member.getEmail())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-24(2023-09-25)
     * 프로필 정보를 수정합니다.
     */
    @Override
    public void updatePrivateProfile(MemberUpdateDto memberUpdateDto, Member member) {
        validateUpdateProfile(memberUpdateDto);
        if (StringUtils.hasText(memberUpdateDto.getNickName())) {
            member.setNickname(memberUpdateDto.getNickName());
        }

        if (StringUtils.hasText(memberUpdateDto.getPhoneNumber())) {
            member.setPhoneNumber(memberUpdateDto.getPhoneNumber());
        }

        member.setAddress(Address.builder()
                .roadAddress(memberUpdateDto.getRoadAddress())
                .addressDetail(memberUpdateDto.getAddressDetail() != null ?
                        memberUpdateDto.getAddressDetail() :
                        member.getAddress().getAddressDetail())
                .lat(memberUpdateDto.getLat())
                .lnt(memberUpdateDto.getLnt())
                .build());


        member.setProfileUrl(memberUpdateDto.getProfileUrl());

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
    public List<RequestedMemberDto> getRequestedMemberList(Long memberId,
                                                           RequestedOrderingType type,
                                                           Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        validateGetRequestedMemberList(member);

        switch (type) {
            case PRICE:
                return memberRepository.getRequestedMemberListOrderByPrice(
                        member.getChefMember().getId(), member.getAddress().getLat(),
                        member.getAddress().getLnt(),
                        pageable
                ).getContent();
            case DISTANCE:
                return memberRepository.getRequestedMemberListOrderByDistance(
                        member.getChefMember().getId(), member.getAddress().getLat(),
                        member.getAddress().getLnt(),
                        pageable
                ).getContent();

        }
        return new ArrayList<>();
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

        member.setPassword(passwordEncoder.encode(form.getNewPassword()));
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

        member.setPassword(passwordEncoder.encode(newPassword));

        return "비밀번호 재설정 완료.";
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
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