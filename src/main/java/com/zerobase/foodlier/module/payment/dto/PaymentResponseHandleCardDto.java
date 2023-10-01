package com.zerobase.foodlier.module.payment.dto;

import lombok.Data;

@Data
public class PaymentResponseHandleCardDto {
    String company;
    String number;
    String installmentPlanMonths;
    String isInterestFree;
    String approveNo;
    String useCardPoint;
    String cardType;
    String ownerType;
    String acquireStatus;
    String receiptUrl;
}
