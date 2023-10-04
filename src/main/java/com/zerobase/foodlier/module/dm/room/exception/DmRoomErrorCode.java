package com.zerobase.foodlier.module.dm.room.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DmRoomErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
