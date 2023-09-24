package com.zerobase.foodlier.global.request.facade;

import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenFacade {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    public SignInResponse createToken(SignInForm form){
        SignInResponse signInResponse = memberService.signIn(form);
        refreshTokenService.createRefreshToken(signInResponse.getRefreshToken(),
                form.getEmail());

        return signInResponse;
    }
}
