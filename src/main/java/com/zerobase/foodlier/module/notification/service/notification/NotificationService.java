package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.dto.notify.Notify;
import com.zerobase.foodlier.module.notification.exception.NotificationErrorCode;
import com.zerobase.foodlier.module.notification.exception.NotificationException;
import com.zerobase.foodlier.module.notification.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification create(Notify notify) {
        return notificationRepository.save(Notification.builder()
                .member(notify.getReceiver())
                .notificationType(notify.getNotificationType())
                .content(notify.getMessage())
                .isRead(false)
                .build());
    }

    public ListResponse<NotificationDto> getNotificationBy(Long memberId, Pageable pageable) {
        return ListResponse.from(
                notificationRepository.findNotificationBy(memberId, pageable));
    }

    public Long countUnreadNotification(Long memberId) {
        return notificationRepository.countUnreadNotification(memberId);
    }

    public void updateNotificationStatus(Long memberId, Long notificationId) {

        Notification notification = notificationRepository.findNotification(memberId, notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));
        notification.updateReadState();
        notificationRepository.save(notification);
    }

}
