package com.zerobase.foodlier.module.member.member.local.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class LocalException extends BaseException {
    private final LocalErrorCode errorCode;
    private final String description;

    public LocalException(LocalErrorCode localErrorCode) {
        this.errorCode = localErrorCode;
        this.description = localErrorCode.getDescription();
    }
}
