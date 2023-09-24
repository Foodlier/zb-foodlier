package com.zerobase.foodlier.common.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtErrorCode {

    ACCESS_TOKEN_EXPIRED("access token이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND("refresh token을 찾을 수 없습니다."),

    MALFORMED_JWT("올바르지 않은 jwt 토큰입니다."),
    INVALID_SIGNATURE("서명이 올바르지 않습니다."),
    INVALID_JWT_COMPONENT("내용이 올바르지 않습니다."),
    EMPTY_TOKEN("헤더에 토큰을 포함하고 있지 않습니다."),
    MALFORMED_JWT_REQUEST("요청 형태가 잘못 되었습니다."),
    INVALID_REFRESH_TOKEN("refresh token이 일치하지 않습니다."),
    ALL_TOKEN_EXPIRED("모든 토큰이 만료되었습니다.");

    private final String description;



}
