package com.zerobase.foodlier.module.requestform.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RequestFormException extends BaseException {

    private final RequestFormErrorCode errorCode;
    private final String description;

    public RequestFormException(RequestFormErrorCode requestFormErrorCode){
        this.errorCode = requestFormErrorCode;
        this.description = requestFormErrorCode.getDescription();
    }

}
