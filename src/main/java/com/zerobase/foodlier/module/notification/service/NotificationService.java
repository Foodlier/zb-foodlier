package com.zerobase.foodlier.module.notification.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    Notification create(Member receiver, NotificationType notificationType, String content, String url);

    List<Notification> getSimpleNotification(Long id);

    Page<Notification> getNotificationBy(Long id, Pageable pageable);
}