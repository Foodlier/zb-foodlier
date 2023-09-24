package com.zerobase.foodlier.common.s3.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class S3Exception extends BaseException {
    private final S3ErrorCode errorCode;
    private final String description;

    public S3Exception(S3ErrorCode s3ErrorCode) {
        this.errorCode = s3ErrorCode;
        this.description = s3ErrorCode.getDescription();
    }
}
