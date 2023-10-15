package com.zerobase.foodlier.module.member.member.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class OAuthException extends BaseException {

    private final OAuthErrorCode errorCode;
    private final String description;

    public OAuthException(OAuthErrorCode oAuthErrorCode) {
        this.errorCode = oAuthErrorCode;
        this.description = oAuthErrorCode.getDescription();
    }

}
