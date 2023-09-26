package com.zerobase.foodlier.module.comment.comment.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class CommentException extends BaseException {
    private final CommentErrorCode errorCode;
    private final String description;

    public CommentException(CommentErrorCode commentErrorCode) {
        this.errorCode = commentErrorCode;
        this.description = commentErrorCode.getDescription();
    }
}
