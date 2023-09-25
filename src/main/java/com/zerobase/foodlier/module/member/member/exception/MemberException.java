package com.zerobase.foodlier.module.member.member.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class MemberException extends BaseException {

    private final MemberErrorCode errorCode;
    private final String description;

    public MemberException(MemberErrorCode memberErrorCode) {
        this.errorCode = memberErrorCode;
        this.description = memberErrorCode.getDescription();
    }

}
