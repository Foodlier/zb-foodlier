package com.zerobase.foodlier.module.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode {
    PAYMENT_CANCELED("취소된 결제 요청입니다."),
    PAYMENT_REQUEST_NOT_FOUND("결제 요청을 찾을 수 없습니다."),
    PAYMENT_ERROR_ORDER_AMOUNT("결제 금액이 맞지 않습니다."),
    UNDEFINED_ERROR("정의되지 않은 오류입니다."),
    ALREADY_PROCESSED_PAYMENT("이미 처리된 결제 입니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
