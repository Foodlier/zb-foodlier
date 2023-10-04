package com.zerobase.foodlier.module.notification.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class NotificationException extends BaseException {
    private final NotificationErrorCode errorCode;
    private final String description;

    public NotificationException(NotificationErrorCode notificationErrorCode) {
        this.errorCode = notificationErrorCode;
        this.description = notificationErrorCode.getDescription();
    }
}
