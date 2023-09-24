package com.zerobase.foodlier.common.redis.exeption;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class EmailVerificationException extends BaseException {
    private final EmailVerificationErrorCode errorCode;
    private final String description;

    public EmailVerificationException(EmailVerificationErrorCode emailVerificationErrorCode){
        this.errorCode = emailVerificationErrorCode;
        this.description = emailVerificationErrorCode.getDescription();
    }
}
