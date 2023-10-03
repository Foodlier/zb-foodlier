package com.zerobase.foodlier.module.notification.dto;

import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationDto {

    private Long id;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime sentAt;
    private boolean isRead;

    public NotificationDto(Long id, String content, NotificationType notificationType, LocalDateTime sentAt, boolean isRead) {
        this.id = id;
        this.content = content;
        this.notificationType = notificationType;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    public NotificationDto() {
    }

    public static NotificationDto from(Notification notification) {

        return NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .notificationType(notification.getNotificationType())
                .sentAt(notification.getSendAt())
                .isRead(notification.isRead())
                .build();


    }
}
