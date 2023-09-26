package com.zerobase.foodlier.module.dm.dm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DmErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
