package com.zerobase.foodlier.global.notification.facade;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.service.notification.NotificationService;
import com.zerobase.foodlier.module.notification.service.emitter.EmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private final static String DELIMITER = "_";
    private final static int MILLISECOND = 1;
    private final NotificationService notificationService;
    private final EmitterService emitterService;

    @Transactional
    public SseEmitter subscribe(String userEmail, String lastEventId) {

        String emitterId = emitterService.makeTimeIncludeId(userEmail);
        SseEmitter emitter = emitterService.createEmitter(userEmail);
        emitter.onCompletion(() -> emitterService.deleteEmitter(emitterId));
        emitter.onTimeout(() -> emitterService.deleteEmitter(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = emitterService.makeTimeIncludeId(userEmail);
        emitterService.send(emitter, eventId, emitterId, userEmail);

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (this.hasLostData(lastEventId)) {
            Map<String, Object> eventCaches = emitterService.findMissingNotification(userEmail);

            long lastEventMilliSeconds = Long.parseLong(lastEventId.split(DELIMITER)[MILLISECOND]);

            for (Map.Entry<String, Object> cache : eventCaches.entrySet()) {
                long cacheMilliSeconds = Long.parseLong(cache.getKey().split(DELIMITER)[MILLISECOND]);
                if (lastEventMilliSeconds >= cacheMilliSeconds) {
                    emitterService.send(emitter, cache.getKey(), emitterId, cache.getValue());
                }
            }
        }

        return emitter;
    }

    @Transactional
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationService.create(receiver, notificationType, content, url);

        String eventId = receiver.getEmail() + DELIMITER + System.currentTimeMillis();

        Map<String, SseEmitter> emitters = emitterService.findAllEmitter(receiver.getEmail());

        for (Map.Entry<String, SseEmitter> emitter : emitters.entrySet()) {
            emitterService.createEventCache(emitter.getKey(), notification);
            emitterService.send(emitter.getValue(), eventId, emitter.getKey(), NotificationDto.from(notification));
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

}

