package com.zerobase.foodlier.module.heart.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeartErrorCode {

    HEART_NOT_FOUND("하트를 찾을 수 없습니다.");
    private final String description;
}
