package com.zerobase.foodlier.global.notification.facade;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.RequesterNotify;
import com.zerobase.foodlier.module.notification.service.emitter.EmitterService;
import com.zerobase.foodlier.module.notification.service.notification.NotificationService;
import com.zerobase.foodlier.module.request.domain.model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    void setup() {
        notificationFacade = new NotificationFacade(notificationService, emitterService);
    }

    @Test
    @DisplayName("알림 구독 성공")
    void success_subscribe() {

        // given
        String userEmail = "test@example.com";
        String emitterId = "test@example.com_123456789";
        String eventId = "test@example.com_1234567891";
        SseEmitter sseEmitter = mock(SseEmitter.class);

        given(emitterService.makeUUIDIncludeId(any()))
                .willReturn(emitterId);
        given(emitterService.createEmitter(any()))
                .willReturn(sseEmitter);
        doNothing().when(sseEmitter).onCompletion(any());
        doNothing().when(sseEmitter).onTimeout(any());

        given(emitterService.makeUUIDIncludeId(any()))
                .willReturn(eventId);



        // when
        SseEmitter resultEmitter = notificationFacade.subscribe(MemberAuthDto.builder()
                        .id(1L)
                        .email("test@example.com")
                        .roles(List.of(RoleType.ROLE_USER.name()))
                .build());

        // then
        assertNotNull(resultEmitter);
        assertEquals(sseEmitter, resultEmitter);
        verify(emitterService, times(0)).deleteEmitter(emitterId);
        verify(emitterService, times(2)).makeUUIDIncludeId(userEmail);
        verify(emitterService, times(1)).send(any(), any(), any(), any());

    }

    @Test
    @DisplayName("알림 전송 성공")
    void success_send_notification() {

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
        ChefMember chefMember = ChefMember.builder()
                .member(receiver)
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .notificationType(NotificationType.REQUEST)
                .sendAt(LocalDateTime.now())
                .isRead(false)
                .member(receiver)
                .build();

        Map<String, SseEmitter> emitters = new HashMap<>();
        SseEmitter sseEmitter = new SseEmitter();

        String eventId = "test@example.com_123456789";
        emitters.put(eventId, sseEmitter);

        RequesterNotify notify = RequesterNotify.from(Request.builder()
                        .chefMember(chefMember)
                        .member(requester)

                        .build(),
                NotifyInfoDto.builder().build());

        when(notificationService.create(notify)).thenReturn(notification);
        when(emitterService.findAllEmitter(any())).thenReturn(emitters);
        when(emitterService.isEmitterExists(emitters)).thenReturn(true);

        doNothing().when(emitterService).send(eq(sseEmitter), any(), any(), any());

        // Act
        notificationFacade.send(notify);

        // Assert
        verify(notificationService, times(1)).create(notify);
        verify(emitterService, times(1)).findAllEmitter(any());
        verify(emitterService, times(1)).send(eq(sseEmitter), any(), any(), any());
    }

}