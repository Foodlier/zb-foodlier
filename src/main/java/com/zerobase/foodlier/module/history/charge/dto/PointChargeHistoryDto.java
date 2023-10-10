package com.zerobase.foodlier.module.history.charge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointChargeHistoryDto {
    private String paymentKey;
    private Long chargePoint;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime chargeAt;
    private String description;

    public static PointChargeHistoryDto from(PointChargeHistory pointChargeHistory) {
        return PointChargeHistoryDto.builder()
                .paymentKey(pointChargeHistory.getPaymentKey())
                .chargePoint(pointChargeHistory.getChargePoint())
                .chargeAt(pointChargeHistory.getCreatedAt())
                .description(pointChargeHistory.getTransactionType().getDescription())
                .build();
    }
}
