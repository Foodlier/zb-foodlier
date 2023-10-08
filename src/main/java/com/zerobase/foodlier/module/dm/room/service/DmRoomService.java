package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.request.domain.model.Request;

import java.util.List;

public interface DmRoomService {
    DmRoom createDmRoom(Request request);

    List<DmRoomDto> getDmRoomList(Long id, int pageIdx, int pageSize);

    void exitDmRoom(Long id, Long roomId);
}
