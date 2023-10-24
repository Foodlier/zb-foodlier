package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.dto.notify.Notify;
import com.zerobase.foodlier.module.notification.exception.NotificationErrorCode;
import com.zerobase.foodlier.module.notification.exception.NotificationException;
import com.zerobase.foodlier.module.notification.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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
                .targetId(notify.getTargetId())
                .build());
    }

    public ListResponse<NotificationDto> getNotificationBy(Long memberId, Pageable pageable) {
        Page<NotificationDto> notificationBy = notificationRepository.findNotificationBy(memberId, pageable);

        return ListResponse.<NotificationDto>builder()
                .totalPages(notificationBy.getTotalPages())
                .totalElements(notificationBy.getTotalElements())
                .hasNextPage(notificationBy.hasNext())
                .content(notificationBy.getContent()
                        .stream()
                        .map(notification -> NotificationDto.builder()
                                .id(notification.getId())
                                .sentAt(notification.getSentAt())
                                .content(notification.getContent())
                                .notificationType(notification.getNotificationType())
                                .isRead(notification.isRead())
                                .targetId(notification.getTargetId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
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
