package com.zerobase.foodlier.module.notification.service.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;


public interface EmitterService {
    String makeTimeIncludeId(String email);
    SseEmitter createEmitter(String userEmail);
    void deleteEmitter(String emitterId);
    void send(SseEmitter emitter, String eventId, String emitterId, Object data);
    Map<String, SseEmitter> findAllEmitter(String receiverEmail);
    boolean isEmitterExists(Map<String, SseEmitter> emitterMap);
}
