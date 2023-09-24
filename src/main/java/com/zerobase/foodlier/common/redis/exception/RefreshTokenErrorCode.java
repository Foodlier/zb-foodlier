package com.zerobase.foodlier.common.redis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefreshTokenErrorCode {
    NEW_ENUM("enum 추가");

    private final String description;
}
