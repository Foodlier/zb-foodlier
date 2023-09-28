package com.zerobase.foodlier.global.refrigerator.facade;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class RefrigeratorFacade {
    private final RequestService requestService;
    private final DmRoomService dmRoomService;

    public void requesterApproveAndCreateDm(Long memberId, Long requestId) {
        Request request = requestService.requesterApproveRequest(memberId, requestId);
        DmRoom dmRoom = dmRoomService.createDmRoom(request);
        requestService.setDmRoom(request, dmRoom);
    }

    public void chefApproveAndCreateDm(Long memberId, Long requestId){
        Request request = requestService.chefApproveRequest(memberId, requestId);
        DmRoom dmRoom = dmRoomService.createDmRoom(request);
        requestService.setDmRoom(request, dmRoom);
    }
}
