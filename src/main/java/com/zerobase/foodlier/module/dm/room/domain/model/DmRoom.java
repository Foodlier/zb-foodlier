package com.zerobase.foodlier.module.dm.room.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dm_room")
public class DmRoom extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;
    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isChefExit = false;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isMemberExit = false;
}
