package com.zerobase.foodlier.common.aop.notification.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionType {
    SEND("send", "요청"),
    APPROVE("approve", "수락"),
    REJECT("reject", "거절"),
    CANCEL("cancel", "취소"),
    RE_COMMENT("recomment", "대댓글"),
    COMMENT("comment", "댓글"),
    HEART("heart", "좋아요"),
    ;

    private final String english;
    private final String korean;

}
