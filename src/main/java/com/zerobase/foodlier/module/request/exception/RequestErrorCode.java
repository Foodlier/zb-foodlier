package com.zerobase.foodlier.module.request.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestErrorCode {
    REQUEST_NOT_FOUND("존재하지 않는 요청서입니다."),
    MEMBER_REQUEST_NOT_MATCH("요청서의 작성자와 일치하지 않습니다."),
    CHEF_MEMBER_REQUEST_NOT_MATCH("요청서의 요리사와 일치하지 않습니다."),
    ALREADY_REQUESTED_CHEF("이미 요청한 요리사입니다."),
    CANNOT_CANCEL_APPROVED("수락한 요청은 취소할 수 없습니다."),
    CANNOT_CANCEL_IS_PAID("결제된 요청은 취소할 수 없습니다."),
    CANNOT_REQUESTER_APPROVE_HAS_NOT_QUOTATION("견적서가 없는 요청을 수락할 수 없습니다."),
    CANNOT_REQUESTER_APPROVE_IS_NOT_QUOTATION("견적서가 아닌 요청을 수락할 수 없습니다."),
    CANNOT_CHEF_APPROVE_HAS_NOT_RECIPE("요청서에 태깅된 레시피가 존재하지 않습니다."),
    CANNOT_CHEF_APPROVE_IS_QUOTATION("견적서인 요청을 수락할 수 없습니다."),
    ALREADY_APPROVED("이미 수락된 요청입니다."),
    ;

    private final String description;
}
