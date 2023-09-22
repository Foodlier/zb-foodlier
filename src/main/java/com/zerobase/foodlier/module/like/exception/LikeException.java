package com.zerobase.foodlier.module.like.exception;

import lombok.Getter;

@Getter
public class LikeException {
    private final LikeErrorCode errorCode;
    private final String description;

    public LikeException(LikeErrorCode likeErrorCode) {
        this.errorCode = likeErrorCode;
        this.description = likeErrorCode.getDescription();
    }
}
