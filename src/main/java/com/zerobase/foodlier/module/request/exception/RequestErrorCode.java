package com.zerobase.foodlier.module.request.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestErrorCode {
    REQUEST_NOT_FOUND("존재하지 않는 요청서입니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
