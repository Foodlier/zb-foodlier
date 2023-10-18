package com.zerobase.foodlier.module.notification.repository.notification;

import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepositoryCustom {
    Page<NotificationDto> findNotificationBy(Long memberId, Pageable pageable);
    Long countUnreadNotification(Long memberId);
    Optional<Notification> findNotification(Long memberId, Long notificationId);
}
