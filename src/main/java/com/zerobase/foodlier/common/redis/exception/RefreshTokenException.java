package com.zerobase.foodlier.common.redis.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RefreshTokenException extends BaseException {

    private final RefreshTokenErrorCode errorCode;
    private final String description;

    public RefreshTokenException(RefreshTokenErrorCode memberErrorCode) {
        this.errorCode = memberErrorCode;
        this.description = memberErrorCode.getDescription();
    }

}
