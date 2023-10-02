package com.zerobase.foodlier.module.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private String payType;
    private Long amount;
    private String orderId;
    private String orderName;
    private String customerEmail;
    private String customerNickName;
    private String successUrl;
    private String failUrl;
    private String payDate;
    private String paySuccessYn;
}
