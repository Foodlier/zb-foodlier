package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

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
     * 작성자 : 이승현
     * 작성일 : 2023-09-24(2023-09-25)
     * 이메일과 비밀번호를 받아와서 access token과 refresh token값을 반환해줍니다.
     */
    @Override
    public TokenDto signIn(SignInForm form) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
                .filter(m-> passwordEncoder.matches(form.getPassword(), m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return tokenProvider.createToken(MemberAuthDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .build());
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
}
