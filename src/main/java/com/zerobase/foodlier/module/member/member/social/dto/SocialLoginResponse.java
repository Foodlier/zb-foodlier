package com.zerobase.foodlier.module.member.member.social.dto;

import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginResponse {

    private RegistrationType registrationType;
    private boolean isTemp;
    private String email;
    private TokenDto tokenDto;
}
