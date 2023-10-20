package com.zerobase.foodlier.module.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionType {
    SEND_RECIPE_REQUEST("요청을 보내셨습니다."),
    SEND_QUOTATION_REQUEST("견적서 요청을 보내셨습니다."),
    SEND_QUOTATION("견적서를 보내셨습니다."),
    REQUEST_APPROVE("수락하셨습니다."),
    REQUEST_REJECT("거절하셨습니다."),
    REQUEST_CANCEL("취소하셨습니다."),
    REPLY("답글을 남겼습니다."),
    COMMENT("게시물에 댓글을 남겼습니다."),
    HEART( "게시물에 좋아요를 눌렀습니다."),
    QUOTATION_APPROVE("견적서를 수락하셨습니다."),
    QUOTATION_REJECT("견적서를 거절하셨습니다."),
    ;
    private final String korean;

}