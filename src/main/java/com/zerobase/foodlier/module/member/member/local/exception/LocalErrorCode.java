package com.zerobase.foodlier.module.member.member.local.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocalErrorCode {

    CANNOT_GET_API_RESPONSE("Local API 호출에 대한 응답에 오류가 발생하였습니다."),
    EMPTY_ADDRESS_LIST("검색 결과가 존재하지 않습니다.");
    private final String description;
}
