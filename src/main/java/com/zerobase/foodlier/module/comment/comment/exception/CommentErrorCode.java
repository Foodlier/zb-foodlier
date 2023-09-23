package com.zerobase.foodlier.module.comment.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
