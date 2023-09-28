package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.module.request.domain.model.Request;

public interface RequestService {
    void sendRequest(Long memberId, Long requestFormId, Long chefMemberId);

    void cancelRequest(Long memberId, Long requestId);

    Request requesterApproveRequest(Long memberId, Long requestId);

    Request chefApproveRequest(Long memberId, Long requestId);

    void rejectRequest(Long memberId, Long requestId);
}
