package com.zerobase.foodlier.module.notification.repository;

import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select new com.zerobase.foodlier.module.notification.dto.NotificationDto(" +
           "n.id, n.content, n.notificationType, n.sendAt, n.isRead) " +
            "from  Notification n join n.member m " +
            "where m.id = :memberId ")
    Page<NotificationDto> findNotificationBy(@Param("memberId") Long id, Pageable pageable);
}