package com.zerobase.foodlier.module.member.member.local.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocalErrorCode {
    CANNOT_ADDRESS_PARSING("좌표로 변환 하는 중에 오류가 발생하였습니다.");
    private final String description;
}
