package com.zerobase.foodlier.global.refrigerator.facade;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefrigeratorFacade {
    private final RequestService requestService;
    private final DmRoomService dmRoomService;

    public void validRequestAndCreateDmRoom(MemberAuthDto memberAuthDto, Long requestId) {
        Request request = requestService.approveRequest(memberAuthDto, requestId);
        dmRoomService.createDmRoom(request);
    }
}
