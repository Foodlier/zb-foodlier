package com.zerobase.foodlier.module.notification.service.emitter;

import com.zerobase.foodlier.module.notification.domain.model.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;


public interface EmitterService {
    String makeTimeIncludeId(String email);
    SseEmitter createEmitter(String userEmail);
    Map<String, Object> findMissingNotification(String userEmail);
    void deleteEmitter(String emitterId);
    void send(SseEmitter emitter, String eventId, String emitterId, Object data);
    void createEventCache(String emitterId, Notification notification);
    Map<String, SseEmitter> findAllEmitter(String receiverEmail);
}
