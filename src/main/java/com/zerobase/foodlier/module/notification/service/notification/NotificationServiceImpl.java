package com.zerobase.foodlier.module.notification.service.notification;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification create(Member receiver, NotificationType notificationType, String content, String url) {
        return notificationRepository.save(createNotification(receiver, notificationType, content, url));
    }

    @Override
    public List<NotificationDto> getNotificationBy(Long memberId, Pageable pageable) {
        return notificationRepository.findNotificationBy(memberId, pageable).getContent();
    }

    private Notification createNotification(Member receiver, NotificationType notificationType, String content, String url) {
        return Notification.builder()
                .member(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}