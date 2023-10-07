package com.zerobase.foodlier.module.transaction.dto;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private Member requestMember;
    private Member chefMember;
    private Integer changePoint;
}
