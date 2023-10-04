package com.zerobase.foodlier.module.history.charge.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointChargeHistoryErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
