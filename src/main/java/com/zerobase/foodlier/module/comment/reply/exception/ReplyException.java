package com.zerobase.foodlier.module.comment.reply.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class ReplyException extends BaseException {
    private final ReplyErrorCode errorCode;
    private final String description;

    public ReplyException(ReplyErrorCode replyErrorCode) {
        this.errorCode = replyErrorCode;
        this.description = replyErrorCode.getDescription();
    }
}
