package com.zerobase.foodlier.module.notification.service.emitter;

import com.zerobase.foodlier.module.notification.exception.NotificationErrorCode;
import com.zerobase.foodlier.module.notification.exception.NotificationException;
import com.zerobase.foodlier.module.notification.repository.sse.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmitterServiceImpl implements EmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String EVENT_NAME = "sse";
    private static final String DELIMITER = "_";

    private final EmitterRepository emitterRepository;

    @Override
    public SseEmitter createEmitter(String userEmail) {
        String emitterId = makeUUIDIncludeId(userEmail);
        return emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
    }

    @Override
    public void deleteEmitter(String emitterId) {
        emitterRepository.deleteById(emitterId);
    }

    @Override
    public void send(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name(EVENT_NAME)
                    .data(data));
        } catch (IOException exception) {
            emitter.complete();
            emitterRepository.deleteById(emitterId);
            throw new NotificationException(NotificationErrorCode.NO_SUCH_EMITTER);
        }
    }

    @Override
    public String makeUUIDIncludeId(String email) {
        return email + DELIMITER + UUID.randomUUID();
    }

    @Override
    public Map<String, SseEmitter> findAllEmitter(String receiverEmail) {
        return emitterRepository.findAllEmitterStartWithByEmail(receiverEmail);
    }

    @Override
    public boolean isEmitterExists(Map<String, SseEmitter> emitterMap){
        return !emitterMap.isEmpty();
    }
}
