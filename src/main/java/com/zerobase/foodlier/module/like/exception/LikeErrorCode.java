package com.zerobase.foodlier.module.like.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    LIKE_NOT_FOUND("좋아요를 찾을 수 없습니다.");
    private final String description;
}
