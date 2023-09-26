package com.zerobase.foodlier.module.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode {

    NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다.");

    private final String description;

}
