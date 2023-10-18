package com.zerobase.foodlier.module.member.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthErrorCode {
    FAILED_AUTH("소셜 인증에 실패하였습니다."),
    INVALID_TOKEN("토큰이 유효하지 않습니다.");
    private final String description;
}
