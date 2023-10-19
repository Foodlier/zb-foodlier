package com.zerobase.foodlier.module.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String content;
    private NotificationType notificationType;
    @JsonFormat(pattern = "yyyy:MM:ss'T'HH:mm:ss")
    private LocalDateTime sentAt;
    private boolean isRead;

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
