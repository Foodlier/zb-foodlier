package com.zerobase.foodlier.module.request.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestErrorCode {
    REQUEST_NOT_FOUND("존재하지 않는 요청서입니다."),
    MEMBER_REQUEST_NOT_MATCH("요청서의 작성자와 일치하지 않습니다."),
    CHEF_MEMBER_REQUEST_NOT_MATCH("요청서의 요리사와 일치하지 않습니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
