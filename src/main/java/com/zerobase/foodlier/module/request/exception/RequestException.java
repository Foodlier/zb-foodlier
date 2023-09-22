package com.zerobase.foodlier.module.request.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RequestException extends BaseException {
    private final RequestErrorCode errorCode;
    private final String description;

    public RequestException(RequestErrorCode requestErrorCode) {
        this.errorCode = requestErrorCode;
        this.description = requestErrorCode.getDescription();
    }
}
