package com.zerobase.foodlier.module.history.charge.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class PointChargeHistoryException extends BaseException {
    private final PointChargeHistoryErrorCode errorCode;
    private final String description;

    public PointChargeHistoryException(PointChargeHistoryErrorCode pointChargeHistoryErrorCode) {
        this.errorCode = pointChargeHistoryErrorCode;
        this.description = pointChargeHistoryErrorCode.getDescription();
    }
}
