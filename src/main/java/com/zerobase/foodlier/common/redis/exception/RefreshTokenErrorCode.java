package com.zerobase.foodlier.common.redis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenErrorCode {
    REFRESH_NOT_FOUND("재발급 토큰을 찾을 수 없습니다."),
    TOKEN_EXPIRED("재발급 토큰이 만료되었습니다.");

    private final String description;
}
