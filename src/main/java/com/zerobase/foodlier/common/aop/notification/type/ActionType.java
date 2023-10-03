package com.zerobase.foodlier.common.aop.notification.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionType {
    SEND("요청"),
    APPROVE("수락"),
    REJECT("거절"),
    CANCEL("취소"),
    RE_COMMENT("대댓글"),
    COMMENT("댓글"),
    HEART( "좋아요"),
    ;
    private final String korean;

}
