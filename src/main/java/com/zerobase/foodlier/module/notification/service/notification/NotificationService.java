package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.dto.notify.Notify;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    Notification create(Notify notify);
    ListResponse<NotificationDto> getNotificationBy(Long id, Pageable pageable);

    Long countUnreadNotification(Long memberId);

}