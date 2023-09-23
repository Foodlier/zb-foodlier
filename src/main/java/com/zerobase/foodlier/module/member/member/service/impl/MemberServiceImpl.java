package com.zerobase.foodlier.module.member.member.service.impl;

import com.zerobase.foodlier.common.security.provider.jwt.JwtTokenProvider;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .orElseThrow();

        return new SignInResponse(
                tokenProvider.createAccessToken(member.getEmail(), member.getId()),
                tokenProvider.createRefreshToken(member.getEmail(), member.getId()));
    }
}
