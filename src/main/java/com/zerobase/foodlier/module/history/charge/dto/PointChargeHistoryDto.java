package com.zerobase.foodlier.module.history.charge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointChargeHistoryDto {
    private String paymentKey;
    private Long chargePoint;
    private String chargeAt;
    private String description;
}
