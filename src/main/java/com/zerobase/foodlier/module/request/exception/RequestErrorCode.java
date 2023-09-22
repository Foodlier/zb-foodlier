package com.zerobase.foodlier.module.request.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
