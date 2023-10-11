package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Notification create(Member receiver, NotificationType notificationType, String content, String url);

    ListResponse<NotificationDto> getNotificationBy(Long id, Pageable pageable);
}