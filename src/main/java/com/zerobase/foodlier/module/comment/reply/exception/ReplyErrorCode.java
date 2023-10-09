package com.zerobase.foodlier.module.comment.reply.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReplyErrorCode {

    NO_SUCH_REPLY("해당 답글을 찾을 수 없습니다.");

    private final String description;
}
