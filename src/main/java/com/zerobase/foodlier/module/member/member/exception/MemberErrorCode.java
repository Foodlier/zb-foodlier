package com.zerobase.foodlier.module.member.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다");

    private final String description;
}
