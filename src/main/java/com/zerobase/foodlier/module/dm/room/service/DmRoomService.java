package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.request.domain.model.Request;

public interface DmRoomService {
    DmRoom createDmRoom(Request request);
}
