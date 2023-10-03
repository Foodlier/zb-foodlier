package com.zerobase.foodlier.common.redisson.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedissonErrorCode {

    LOCK_ERROR("lock 오류가 발생했습니다.");
    private final String description;
}
