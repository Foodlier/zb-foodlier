package com.zerobase.foodlier.module.heart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeartErrorCode {
    HEART_NOT_FOUND("좋아요를 찾을 수 없습니다."),
    ALREADY_PUSH_HEART("좋아요를 이미 누르셨습니다");
    private final String description;
}
