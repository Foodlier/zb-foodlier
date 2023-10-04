package com.zerobase.foodlier.module.dm.room.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DmRoomErrorCode {
    DM_ROOM_NOT_FOUND("dm 방을 찾을 수 없습니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
