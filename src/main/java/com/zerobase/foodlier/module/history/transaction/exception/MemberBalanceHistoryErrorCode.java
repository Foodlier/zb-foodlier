package com.zerobase.foodlier.module.history.transaction.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberBalanceHistoryErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
