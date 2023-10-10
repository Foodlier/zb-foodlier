package com.zerobase.foodlier.module.history.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberBalanceHistoryDto {
    private int changePoint;
    private int currentPoint;
    private  String sender;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime transactionAt;

    public static MemberBalanceHistoryDto from(MemberBalanceHistory memberBalanceHistory) {
        return MemberBalanceHistoryDto.builder()
                .changePoint(memberBalanceHistory.getChangePoint())
                .currentPoint(memberBalanceHistory.getCurrentPoint())
                .sender(memberBalanceHistory.getSender())
                .description(memberBalanceHistory.getTransactionType().getDescription())
                .transactionAt(memberBalanceHistory.getCreatedAt())
                .build();
    }
}
