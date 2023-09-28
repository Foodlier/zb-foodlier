package com.zerobase.foodlier.module.requestform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestFormErrorCode {
    REQUEST_FORM_NOT_FOUND("요청서를 찾을 수 없습니다.");

    private final String description;
}
