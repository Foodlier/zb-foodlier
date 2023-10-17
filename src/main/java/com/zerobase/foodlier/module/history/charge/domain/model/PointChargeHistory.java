package com.zerobase.foodlier.module.history.charge.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.history.type.TransactionType;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "point_charge_history")
public class PointChargeHistory extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false)
    private String paymentKey;
    @Column(nullable = false)
    private Long chargePoint;
    @Column(nullable = false)
    private TransactionType transactionType;
}
