package com.zerobase.foodlier.global.member.oAuth.facade;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.service.OAuthInfoService;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthLoginParams;
import com.zerobase.foodlier.module.member.member.social.dto.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuthFacade {
    private final OAuthInfoService oAuthInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-12
     * 소셜 로그인을 진행합니다.
     * 이미 자체 사이트 가입이 된 경우라면 가입타입과 email을 반환합니다.
     */
    public SocialLoginResponse login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = oAuthInfoService.request(params);
        Member member = memberService.findOrCreateMember(oAuthInfoResponse);

        if (params.registrationType() != member.getRegistrationType()) {
            return SocialLoginResponse.builder()
                    .email(member.getEmail())
                    .isTemp(member.isTemp())
                    .registrationType(member.getRegistrationType()).build();
        }

        TokenDto tokenDto = tokenProvider.createToken(MemberAuthDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .build(), java.sql.Timestamp.valueOf(LocalDateTime.now()));

        return SocialLoginResponse.builder()
                .registrationType(oAuthInfoResponse.getRegistrationType())
                .email(member.getEmail())
                .isTemp(member.isTemp())
                .tokenDto(tokenDto)
                .build();
    }
}
