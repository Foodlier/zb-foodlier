package com.zerobase.foodlier.common.socialLogin.facade;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.common.socialLogin.dto.OAuthInfoResponse;
import com.zerobase.foodlier.common.socialLogin.dto.OAuthLoginParams;
import com.zerobase.foodlier.common.socialLogin.dto.SocialLoginResponse;
import com.zerobase.foodlier.common.socialLogin.service.RequestOAuthInfoService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.zerobase.foodlier.module.member.member.type.RegistrationType.DOMAIN;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthFacade {
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public SocialLoginResponse login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Member member = memberService.findOrCreateMember(oAuthInfoResponse); // 회원정보가 있다면 가져오면서 회원가입도 진행함

        if (member.getRegistrationType() == DOMAIN) {
            log.info("이미 사이트에 가입된 회원입니다.");
            return SocialLoginResponse.builder()
                    .registrationType(DOMAIN).build();
        }

        TokenDto tokenDto = tokenProvider.createToken(MemberAuthDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .build(), java.sql.Timestamp.valueOf(LocalDateTime.now()));

        return SocialLoginResponse.builder()
                .registrationType(oAuthInfoResponse.getRegistrationType())
                .tokenDto(tokenDto)
                .build();
    }
}
