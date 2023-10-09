package com.zerobase.foodlier.module.history.charge.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
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
    private String description;

    public static PointChargeHistoryDto from(PointChargeHistory pointChargeHistory) {
        return PointChargeHistoryDto.builder()
                .paymentKey(pointChargeHistory.getPaymentKey())
                .chargePoint(pointChargeHistory.getChargePoint())
                .chargeAt(String.valueOf(LocalDate.from(pointChargeHistory.getCreatedAt())))
                .description(pointChargeHistory.getDescription())
                .build();
    }
}
