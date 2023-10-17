package com.zerobase.foodlier.module.history.transaction.domain.model;

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
@Table(name = "member_balance_history")
public class MemberBalanceHistory extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(nullable = false)
    private Integer changePoint;
    @Column(nullable = false)
    private Integer currentPoint;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
