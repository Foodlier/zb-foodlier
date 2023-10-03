package com.zerobase.foodlier.module.heart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeartErrorCode {
    ALREADY_HEART_CANCEL("좋아요를 이미 취소했습니다."),
    ALREADY_HEART("좋아요를 이미 눌렀습니다."),
    HEART_NOT_FOUND("좋아요를 찾을 수 없습니다.");
    private final String description;
}
