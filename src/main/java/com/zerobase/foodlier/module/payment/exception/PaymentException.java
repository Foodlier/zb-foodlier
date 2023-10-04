package com.zerobase.foodlier.module.payment.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class PaymentException extends BaseException {
    private final PaymentErrorCode errorCode;
    private final String description;

    public PaymentException(PaymentErrorCode paymentErrorCode) {
        this.errorCode = paymentErrorCode;
        this.description = paymentErrorCode.getDescription();
    }
}
