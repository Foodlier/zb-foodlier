package com.zerobase.foodlier.module.notification.repository.notification;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.notification.domain.model.Notification;
import com.zerobase.foodlier.module.notification.domain.model.QNotification;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {
    private static final boolean NOT_READ = false;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<NotificationDto> findNotificationBy(Long memberId, Pageable pageable) {
        QNotification notification = QNotification.notification;
        QMember member = QMember.member;

        List<NotificationDto> content = jpaQueryFactory.select(
                        Projections.constructor(NotificationDto.class,
                                notification.id, notification.content, notification.notificationType,
                                notification.sendAt, notification.isRead, notification.targetId))
                .from(notification)
                .join(member)
                .on(member.id.eq(memberId))
                .where(notification.member.id.eq(memberId))
                .orderBy(notification.sendAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(notification)
                .join(member)
                .on(notification.member.eq(member))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Long countUnreadNotification(Long memberId) {
        QNotification notification = QNotification.notification;
        QMember member = QMember.member;

        return jpaQueryFactory.select(notification.count())
                .from(notification)
                .leftJoin(member)
                .on(member.id.eq(memberId))
                .where(notification.member.id.eq(memberId).and(notification.isRead.eq(NOT_READ)))
                .fetchOne();
    }

    @Override
    public Optional<Notification> findNotification(Long memberId, Long notificationId) {

        QNotification notification = QNotification.notification;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(notification)
                .where(notification.member.id.eq(memberId)
                        .and(notification.id.eq(notificationId)))
                .fetchOne());
    }
}