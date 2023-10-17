package com.zerobase.foodlier.module.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerformerType {

    CHEF("요리사"),
    REQUESTER("요청자"),
    PUSH_HEART("사용자"),
    COMMENTER("사용자"),
    ;

    private final String korean;
}