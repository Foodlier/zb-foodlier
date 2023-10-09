package com.zerobase.foodlier.global.notification.facade;

import com.zerobase.foodlier.common.aop.notification.type.ActionType;
import com.zerobase.foodlier.common.aop.notification.type.NotifyMessage;
import com.zerobase.foodlier.common.aop.notification.type.NotifyUrl;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.exception.NotificationException;
import com.zerobase.foodlier.module.notification.service.emitter.EmitterService;
import com.zerobase.foodlier.module.notification.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.zerobase.foodlier.module.notification.exception.NotificationErrorCode.NO_SUCH_EMITTER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationFacadeTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmitterService emitterService;

    private NotificationFacade notificationFacade;

    @BeforeEach
    void setup(){
        notificationFacade = new NotificationFacade(notificationService, emitterService);
    }

    @Test
    @DisplayName("알림 구독 성공")
    void success_subscribe(){

        // given
        String userEmail = "test@example.com";
        String lastEventId = "test@example.com_12345678";
        String emitterId = "test@example.com_123456789";
        String eventId = "test@example.com_1234567891";
        SseEmitter sseEmitter = mock(SseEmitter.class);
        SseEmitter cacheEmitter = mock(SseEmitter.class);
        Map<String, Object> eventCaches = new HashMap<>();
        eventCaches.put(lastEventId, cacheEmitter);

        // 새로운 emitter 객체 생성
        given(emitterService.makeTimeIncludeId(any()))
                .willReturn(emitterId);
        given(emitterService.createEmitter(any()))
                .willReturn(sseEmitter);
        doNothing().when(sseEmitter).onCompletion(any());
        doNothing().when(sseEmitter).onTimeout(any());

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        given(emitterService.makeTimeIncludeId(any()))
                .willReturn(eventId);

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        when(emitterService.hasLostData(lastEventId)).thenReturn(true);

        given(emitterService.findEventCaches(anyString()))
                .willReturn(eventCaches);
        doNothing().when(emitterService).send(any(), any(), any(),any());

        // when
        SseEmitter resultEmitter = notificationFacade.subscribe(userEmail, lastEventId);

        // then
        assertNotNull(resultEmitter);
        assertEquals(sseEmitter, resultEmitter);
        verify(emitterService,times(0)).deleteEmitter(emitterId);
        verify(emitterService, times(2)).makeTimeIncludeId(userEmail);
        verify(emitterService, times(2)).send(any(), any(), any(), any());

    }

    @Test
    @DisplayName("알림 전송 성공")
    void success_send_notification(){

        // given
        Member requester = Member.builder()
                .id(1L)
                .nickname("nickname")

                .build();

        Member receiver = Member.builder()
                .id(2L)
                .nickname("세체요")
                .email("test@example.com")
                .build();

        NotificationType notificationType = NotificationType.REQUEST;
        String content = "Test Notification";
        String url = "https://example.com";
        Notification notification = new Notification();
        Map<String, SseEmitter> emitters = new HashMap<>();
        SseEmitter sseEmitter = new SseEmitter();

        String eventId = "test@example.com_123456789";
        emitters.put(eventId, sseEmitter);

        when(notificationService.create(receiver, notificationType, content, url)).thenReturn(notification);
        when(emitterService.findAllEmitter(receiver.getEmail())).thenReturn(emitters);
        when(emitterService.isEmitterExists(emitters)).thenReturn(true);

        doNothing().when(emitterService).createEventCache(any(), any());
        doNothing().when(emitterService).send(eq(sseEmitter), any(), any(), any());

        // Act
        notificationFacade.send(receiver, notificationType, content, url);

        // Assert
        verify(notificationService, times(1)).create(receiver, notificationType, content, url);
        verify(emitterService, times(1)).findAllEmitter(receiver.getEmail());
        verify(emitterService, times(1)).createEventCache(eventId, notification);
        verify(emitterService, times(1)).send(eq(sseEmitter), any(), any(), any());
    }

    @Test
    @DisplayName("알림 전송 실패 - 일치하는 전송 객체 없음")
    void fail_send_notification(){

        // Arrange
        Member requester = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("test@gmail.com")
                .build();
        Member receiver = Member.builder()
                .id(1L)
                .nickname("세체요")
                .build();

        NotificationType notificationType = NotificationType.REQUEST;

        String content = NotifyMessage.MESSAGE_TO_CHEF.createMessage(
                "제육볶음 요청",
                requester.getNickname(),
                ActionType.SEND.getKorean());

        String url = NotifyUrl.REQUEST.createUrl(1L);

        Notification notification = Notification.builder()
                .id(1L)
                .member(receiver)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .content(content)
                .url(url)
                .build();

        Map<String, SseEmitter> emitters = new HashMap<>();

        when(notificationService.create(receiver, notificationType, content, url)).thenReturn(notification);
        when(emitterService.findAllEmitter(receiver.getEmail())).thenReturn(emitters);
        when(emitterService.isEmitterExists(emitters)).thenReturn(false);

        // Act
        NotificationException notificationException = assertThrows(NotificationException.class,
                () -> notificationFacade.send(receiver, notificationType, content, url));

        // Assert
        verify(notificationService, times(1)).create(receiver, notificationType, content, url);
        verify(emitterService, times(1)).findAllEmitter(receiver.getEmail());
        assertEquals(notificationException.getErrorCode(), NO_SUCH_EMITTER);
        assertEquals(notificationException.getDescription(), NO_SUCH_EMITTER.getDescription());
    }

}