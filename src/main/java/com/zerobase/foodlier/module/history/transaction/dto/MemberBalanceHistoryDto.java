package com.zerobase.foodlier.module.history.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberBalanceHistoryDto {
    private int changePoint;
    private int currentPoint;
    private  String sender;
    private String description;
}
