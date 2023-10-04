package com.zerobase.foodlier.module.transaction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionErrorCode {
    CHEF_MEMBER_NOT_MATCH("요리사가 아닙니다."),
    REQUEST_MEMBER_NOT_MATCH("요청자가 아닙니다."),
    ALREADY_SUGGESTED("이미 제안이 요청되었습니다."),
    NOT_ENOUGH_POINT("포인트가 부족합니다."),
    SUGGESTION_NOT_FOUND("요청된 제안이 없습니다."),
    ;

    private final String description;
}