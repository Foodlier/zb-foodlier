package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.jwt.JwtTokenProvider;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public SignInResponse signIn(SignInForm form) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
//                .filter(m-> passwordEncoder.matches(form.getPassword(), m.getPassword()))
                .filter(m -> form.getPassword().equals(m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return new SignInResponse(
                tokenProvider.createAccessToken(member.getEmail(), member.getId()),
                tokenProvider.createRefreshToken(member.getEmail(), member.getId()));
    }

    @Override
    public MemberPrivateProfileResponse getPrivateProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new MemberException(MEMBER_NOT_FOUND));

        return MemberPrivateProfileResponse.builder()
                .nickName(member.getNickname())
                .email(member.getEmail())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    @Override
    public void updatePrivateProfile(Long memberId,MemberPrivateProfileForm form) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        member.setNickname(form.getNickName());
        member.setPhoneNumber(form.getPhoneNumber());
        member.setAddress(form.getAddress());
        member.setProfileUrl(form.getProfileUrl());

        memberRepository.save(member);
    }
}
