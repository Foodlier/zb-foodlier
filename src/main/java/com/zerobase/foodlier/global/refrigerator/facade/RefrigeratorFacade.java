package com.zerobase.foodlier.global.refrigerator.facade;

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

    public void requesterApproveAndCreateDm(Long memberId, Long requestId) {
        Request request = requestService.requesterApproveRequest(memberId, requestId);
        dmRoomService.createDmRoom(request);
    }

    public void chefApproveAndCreateDm(Long memberId, Long requestId){
        Request request = requestService.chefApproveRequest(memberId, requestId);
        dmRoomService.createDmRoom(request);
    }
}
