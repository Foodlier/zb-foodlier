package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.request.domain.model.Request;

public interface RequestService {
    void sendRequest(MemberAuthDto memberAuthDto, Long requestId, Long chefMemberId);

    void cancelRequest(MemberAuthDto memberAuthDto, Long requestId);

    Request approveRequest(MemberAuthDto memberAuthDto, Long requestId);

    void rejectRequest(MemberAuthDto memberAuthDto, Long requestId);
}
