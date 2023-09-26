package com.zerobase.foodlier.module.history.transaction.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class MemberBalanceHistoryException extends BaseException {
    private final MemberBalanceHistoryErrorCode errorCode;
    private final String description;

    public MemberBalanceHistoryException(MemberBalanceHistoryErrorCode memberBalanceHistoryErrorCode) {
        this.errorCode = memberBalanceHistoryErrorCode;
        this.description = memberBalanceHistoryErrorCode.getDescription();
    }
}
