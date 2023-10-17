package com.zerobase.foodlier.module.notification.domain.model;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
@Builder
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @Column(nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String url;
    private boolean isRead;
    @CreatedDate
    private LocalDateTime sendAt;
}