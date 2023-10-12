package com.zerobase.foodlier.common.socialLogin.dto;

import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.Builder;

@Builder
public class SocialLoginResponse {

    private RegistrationType registrationType;

    private TokenDto tokenDto;
}
