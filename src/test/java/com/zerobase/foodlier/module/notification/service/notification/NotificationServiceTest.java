package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.common.aop.notification.type.ActionType;
import com.zerobase.foodlier.common.aop.notification.type.NotifyMessage;
import com.zerobase.foodlier.common.aop.notification.type.NotifyUrl;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.repository.notification.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        notificationService = new NotificationServiceImpl(notificationRepository);
    }

    @Test
    @DisplayName("알림 객체 생성 성공")
    void success_create_notification() {

        // given
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .build();
        LocalDateTime now = LocalDateTime.now();
        Notification expected = Notification.builder()
                .id(1L)
                .content(NotifyMessage.MESSAGE_TO_CHEF.createMessage("맛있는 제육볶음을 만들어주세요",
                        member.getNickname(),
                        ActionType.SEND.getKorean()))
                .notificationType(NotificationType.REQUEST)
                .isRead(false)
                .url(NotifyUrl.REQUEST.createUrl(1L))
                .sendAt(now)
                .build();

        given(notificationRepository.save(any()))
                .willReturn(expected);

        // when
        Notification notification = notificationService.create(member,
                NotificationType.REQUEST,
                NotifyMessage.MESSAGE_TO_CHEF.createMessage("맛있는 제육볶음을 만들어주세요",
                        member.getNickname(),
                        ActionType.SEND.getKorean()),
                NotifyUrl.REQUEST.createUrl(1L));

        // then
        assertEquals(expected.getId(), notification.getId());
        assertEquals(expected.getMember(), notification.getMember());
        assertEquals(expected.getNotificationType(), notification.getNotificationType());
        assertEquals(expected.getContent(), notification.getContent());
        assertEquals(expected.getUrl(), notification.getUrl());
        assertEquals(expected.getSendAt(), notification.getSendAt());
    }

    @Test
    @DisplayName("현재까지 발생한 알림 목록 조회 성공")
    void success_get_notification() {

        // given
        ArrayList<NotificationDto> expectedNotification = makeExpectedNotificationList();

        given(notificationRepository.findNotificationBy(anyLong(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(expectedNotification)));

        // when
        ListResponse<NotificationDto> notificationBy = notificationService.getNotificationBy(1L, PageRequest.of(0, 10));

        // then
        assertAll(
                // newest notification
                () -> assertEquals(expectedNotification.get(0).getId(),
                        notificationBy.getContent().get(0).getId()),
                () -> assertEquals(expectedNotification.get(0).getNotificationType(),
                        notificationBy.getContent().get(0).getNotificationType()),
                () -> assertEquals(expectedNotification.get(0).getUrl(),
                        notificationBy.getContent().get(0).getUrl()),
                () -> assertEquals(expectedNotification.get(0).getContent(),
                        notificationBy.getContent().get(0).getContent()),
                () -> assertEquals(expectedNotification.get(0).getSentAt(),
                        notificationBy.getContent().get(0).getSentAt()),

                // next order notification
                () -> assertEquals(expectedNotification.get(1).getId(),
                        notificationBy.getContent().get(1).getId()),
                () -> assertEquals(expectedNotification.get(1).getNotificationType(),
                        notificationBy.getContent().get(1).getNotificationType()),
                () -> assertEquals(expectedNotification.get(1).getUrl(),
                        notificationBy.getContent().get(1).getUrl()),
                () -> assertEquals(expectedNotification.get(1).getContent(),
                        notificationBy.getContent().get(1).getContent()),
                () -> assertEquals(expectedNotification.get(1).getSentAt(),
                        notificationBy.getContent().get(1).getSentAt())
        );

    }


    private static ArrayList<NotificationDto> makeExpectedNotificationList() {
        Member member = Member.builder()
                .id(1L)
                .nickname("제육볶음 러버")
                .build();

        return new ArrayList<>(Arrays.asList(
                NotificationDto.builder()
                        .id(2L)
                        .sentAt(LocalDateTime.now())
                        .url(NotifyUrl.REQUEST.createUrl(1L))
                        .isRead(false)
                        .notificationType(NotificationType.REQUEST)
                        .content(NotifyMessage.MESSAGE_TO_CHEF.createMessage("맛있는 제육볶음을 만들어주세요",
                                "세체요",
                                ActionType.APPROVE.getKorean()))
                        .build(),
                NotificationDto.builder()
                        .id(1L)
                        .sentAt(LocalDateTime.now().minusMinutes(30L))
                        .url(NotifyUrl.REQUEST.createUrl(1L))
                        .isRead(false)
                        .notificationType(NotificationType.REQUEST)
                        .content(NotifyMessage.MESSAGE_TO_CHEF.createMessage("맛있는 제육볶음을 만들어주세요",
                                member.getNickname(),
                                ActionType.SEND.getKorean()))
                        .build()
        ));
    }
}