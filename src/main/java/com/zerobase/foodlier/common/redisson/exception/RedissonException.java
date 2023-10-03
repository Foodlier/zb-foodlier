package com.zerobase.foodlier.common.redisson.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RedissonException extends BaseException {
    private final RedissonErrorCode errorCode;
    private final String description;

    public RedissonException(RedissonErrorCode redissonErrorCode) {
        this.errorCode = redissonErrorCode;
        this.description = redissonErrorCode.getDescription();
    }
}
