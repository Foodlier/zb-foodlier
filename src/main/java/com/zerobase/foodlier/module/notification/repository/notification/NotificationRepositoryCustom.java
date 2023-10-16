package com.zerobase.foodlier.module.notification.repository.notification;

import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {
    Page<NotificationDto> findNotificationBy(Long memberId, Pageable pageable);
}
