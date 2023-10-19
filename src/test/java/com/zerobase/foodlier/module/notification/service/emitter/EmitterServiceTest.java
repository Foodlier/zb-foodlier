package com.zerobase.foodlier.module.notification.service.emitter;

import com.zerobase.foodlier.module.notification.repository.sse.EmitterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class EmitterServiceTest {
    @Mock
    private EmitterRepository emitterRepository;

    private EmitterService emitterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emitterService = new EmitterService(emitterRepository);
    }

    @Test
    @DisplayName("전송 객체 저장 성공")
    void success_create_emitter() {
        // given
        String userEmail = "test@example.com";
        SseEmitter sseEmitter = new SseEmitter();

        given(emitterRepository.save(anyString(), any(SseEmitter.class)))
                .willReturn(sseEmitter);
        // when
        SseEmitter createdEmitter = emitterService.createEmitter(userEmail);

        // then
        assertNotNull(createdEmitter);
        assertEquals(sseEmitter, createdEmitter);
    }

    @Test
    @DisplayName("전송 객체 삭제 성공")
    void success_delete_emitter() {
        // given
        String emitterId = "test@example.com_123456789";

        // when
        emitterService.deleteEmitter(emitterId);

        // then
        verify(emitterRepository).deleteById(emitterId);
    }

    @Test
    @DisplayName("알림 전송 성공")
    void success_send() throws IOException {
        // given
        SseEmitter sseEmitter = mock(SseEmitter.class);
        String eventId = "1";
        String emitterId = "test@example.com_123456789";
        Object data = "Test Data";

        doNothing().when(sseEmitter).send(any());

        // when
        emitterService.send(sseEmitter, eventId, emitterId, data);

        verify(sseEmitter, times(1)).send(any());
    }

    @Test
    @DisplayName("알림 전송 객체 id 생성 성공")
    void success_makeTimeIncludeId() {
        // given
        String email = "test@example.com";

        // when
        String emitterId = emitterService.makeUUIDIncludeId(email);

        // then
        assertNotNull(emitterId);
        assertTrue(emitterId.startsWith(email));
        assertTrue(emitterId.contains("_"));
    }

    @Test
    @DisplayName("알림 전송 객체 찾기 성공")
    void success_findAllEmitter() {
        // given
        String receiverEmail = "test@example.com";
        Map<String, SseEmitter> emitterMap = new HashMap<>();
        when(emitterRepository.findAllEmitterStartWithByEmail(receiverEmail)).thenReturn(emitterMap);

        // when
        Map<String, SseEmitter> result = emitterService.findAllEmitter(receiverEmail);

        // then
        assertNotNull(result);
        assertEquals(emitterMap, result);
    }
}