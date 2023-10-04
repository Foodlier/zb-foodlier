package com.zerobase.foodlier.module.member.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다"),
    EMAIL_IS_ALREADY_EXIST("이미 사용 중인 이메일 입니다."),
    NICKNAME_IS_ALREADY_EXIST("이미 사용 중인 닉네임 입니다."),
    PHONE_NUMBER_IS_ALREADY_EXIST("이미 사용 중인 핸드폰 번호 입니다.");

    private final String description;
}
