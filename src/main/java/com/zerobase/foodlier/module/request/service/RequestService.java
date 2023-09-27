package com.zerobase.foodlier.module.request.service;

public interface RequestService {
    void sendRequest(Long requestId, Long chefMemberId);
    void cancelRequest(Long requestId);
    void rejectRequest(Long requestId);
}
