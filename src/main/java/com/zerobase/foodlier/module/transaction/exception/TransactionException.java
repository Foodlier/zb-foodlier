package com.zerobase.foodlier.module.transaction.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class TransactionException extends BaseException {
    private final TransactionErrorCode errorCode;
    private final String description;

    public TransactionException(TransactionErrorCode transactionErrorCode) {
        this.errorCode = transactionErrorCode;
        this.description = transactionErrorCode.getDescription();
    }
}
