package com.zerobase.foodlier.module.heart.exception;

import lombok.Getter;

@Getter
public class HeartException {
    private final HeartErrorCode errorCode;
    private final String description;

    public HeartException(HeartErrorCode heartErrorCode) {
        this.errorCode = heartErrorCode;
        this.description = heartErrorCode.getDescription();
    }
}
