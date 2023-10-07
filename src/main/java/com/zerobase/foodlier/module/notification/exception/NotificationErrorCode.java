package com.zerobase.foodlier.module.notification.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode {

    NOTIFICATION_NOT_FOUND("알림을 찾을 수 없습니다."),
    INVALID_ACCESS("잘못된 접근입니다."),
    INVALID_NOTIFICATION("해당하는 알림 유형이 없습니다."),
    NO_SUCH_EMITTER("알림 전송이 가능한 객체가 존재하지 않습니다.")
    ;

    private final String description;

}