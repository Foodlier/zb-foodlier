package com.zerobase.foodlier.module.heart.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class HeartException extends BaseException {
    private final HeartErrorCode errorCode;
    private final String description;

    public HeartException(HeartErrorCode heartErrorCode) {
        this.errorCode = heartErrorCode;
        this.description = heartErrorCode.getDescription();
    }
}
