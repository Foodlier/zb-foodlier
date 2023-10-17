package com.zerobase.foodlier.module.notification.repository.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);
    void deleteById(String id);
}
