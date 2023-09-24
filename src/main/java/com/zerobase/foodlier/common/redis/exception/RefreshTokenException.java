package com.zerobase.foodlier.common.redis.exception;

import lombok.Getter;

@Getter
public class RefreshTokenException {
    private final RefreshTokenErrorCode errorCode;
    private final String description;

    public RefreshTokenException(RefreshTokenErrorCode refreshTokenErrorCode) {
        this.errorCode = refreshTokenErrorCode;
        this.description = refreshTokenErrorCode.getDescription();
    }
}
