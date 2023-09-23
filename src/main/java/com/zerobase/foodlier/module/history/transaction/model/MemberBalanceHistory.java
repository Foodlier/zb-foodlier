package com.zerobase.foodlier.module.history.transaction.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.history.transaction.type.PaymentType;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_balance_history")
public class MemberBalanceHistory extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "chef_id")
    private ChefMember chefMember;
    @Column(nullable = false)
    private Integer changePoint;
    @Column(nullable = false)
    private Integer currentPoint;
    @Column(nullable = false)
    private String fr;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
