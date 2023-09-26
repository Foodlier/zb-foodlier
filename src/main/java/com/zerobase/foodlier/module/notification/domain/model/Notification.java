package com.zerobase.foodlier.module.notification.domain.model;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
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
    private String title;
    @Column(nullable = false)
    private String content;
    private String type;
    private Long accessId;
    @CreatedDate
    private LocalDateTime sendAt;
}
