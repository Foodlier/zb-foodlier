package com.zerobase.foodlier.module.dm.dm.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class DmException extends BaseException {
    private final DmErrorCode errorCode;
    private final String description;

    public DmException(DmErrorCode dmErrorCode) {
        this.errorCode = dmErrorCode;
        this.description = dmErrorCode.getDescription();
    }
}
