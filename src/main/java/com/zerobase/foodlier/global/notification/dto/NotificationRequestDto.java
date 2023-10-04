package com.zerobase.foodlier.global.notification.dto;

import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    private Long id;
    private NotificationType notificationType;
}
