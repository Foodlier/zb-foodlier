package com.zerobase.foodlier.module.dm.dm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DmErrorCode {

    NO_SUCH_DM("채팅기록 없음");

    private final String description;
}
