package com.zerobase.foodlier.module.comment.reply.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReplyErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
