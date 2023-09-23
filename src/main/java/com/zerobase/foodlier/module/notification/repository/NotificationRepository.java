package com.zerobase.foodlier.module.notification.repository;

import com.zerobase.foodlier.module.notification.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
