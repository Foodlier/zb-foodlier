package com.zerobase.foodlier.module.history.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    CHARGE_POINT("포인트 충전"),
    CHARGE_CANCEL("결제 취소"),
    POINT_SEND("포인트 출금"),
    POINT_RECEIVE("포인트 입금"),
    ;

    private final String description;
}
