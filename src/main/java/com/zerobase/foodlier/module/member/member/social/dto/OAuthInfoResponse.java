package com.zerobase.foodlier.module.member.member.social.dto;

import com.zerobase.foodlier.module.member.member.type.RegistrationType;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    RegistrationType getRegistrationType();
}