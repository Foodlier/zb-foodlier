package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.request.domain.model.Request;

public interface DmRoomService {
    DmRoom createDmRoom(Request request);

    ListResponse<DmRoomDto> getDmRoomList(Long id, int pageIdx, int pageSize);

    void exitDmRoom(Long id, Long roomId);
}
