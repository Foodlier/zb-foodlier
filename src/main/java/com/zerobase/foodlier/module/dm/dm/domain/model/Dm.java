package com.zerobase.foodlier.module.dm.dm.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.dm.dm.type.MessageType;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dm")
public class Dm extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private DmRoom dmroom;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private String flag;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
}
