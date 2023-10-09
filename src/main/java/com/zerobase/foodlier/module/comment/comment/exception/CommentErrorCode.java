package com.zerobase.foodlier.module.comment.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {

    NO_SUCH_COMMENT("댓글을 찾을 수 없습니다."),
    ALREADY_DELETED("댓글이 이미 삭제되었습니다."),
    ;

    private final String description;
}
