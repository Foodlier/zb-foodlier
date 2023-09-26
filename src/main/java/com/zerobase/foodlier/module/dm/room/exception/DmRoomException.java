package com.zerobase.foodlier.module.dm.room.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class DmRoomException extends BaseException {
    private final DmRoomErrorCode errorCode;
    private final String description;

    public DmRoomException(DmRoomErrorCode dmRoomErrorCode) {
        this.errorCode = dmRoomErrorCode;
        this.description = dmRoomErrorCode.getDescription();
    }
}
