package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.dto.RequestDetailDto;

public interface RequestService {

    void setDmRoom(Request request, DmRoom dmRoom);
    Request setQuotation(Long requestId, Recipe quotation);
    RequestDetailDto getRequestDetail(Long memberId, Long requestId);
    Request sendRequest(Long memberId, Long requestFormId, Long chefMemberId);

    Request cancelRequest(Long memberId, Long requestId);

    Request requesterApproveRequest(Long memberId, Long requestId);

    Request chefApproveRequest(Long memberId, Long requestId);

    Request rejectRequest(Long memberId, Long requestId);
}
