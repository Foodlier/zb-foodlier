package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.request.domain.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface DmRoomService {
    DmRoom createDmRoom(Request request);

    Page<DmRoomDto> getDmRoomPage(Long id, int pageIdx, int pageSize);
}
