package com.zerobase.foodlier.common.redis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailVerificationErrorCode {
    VERIFICATION_NOT_FOUND("이메일 인증 정보를 찾을 수 없습니다."),
    VERIFICATION_CODE_MISMATCH("인증 코드가 일치하지 않습니다."),
    VERIFICATION_IS_NOT_AUTHORIZED("이메일 인증이 수행되지 않았습니다."),
    VERIFICATION_IS_EXPIRED("만료된 인증입니다. 다시 인증부탁드립니다."),
    ;
    private final String description;
}
