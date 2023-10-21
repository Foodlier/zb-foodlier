package com.zerobase.foodlier.module.member.member.mail.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class MailException extends BaseException {

    private final MailErrorCode errorCode;
    private final String description;

    public MailException(MailErrorCode mailErrorCode) {
        this.errorCode = mailErrorCode;
        this.description = mailErrorCode.getDescription();
    }

}
