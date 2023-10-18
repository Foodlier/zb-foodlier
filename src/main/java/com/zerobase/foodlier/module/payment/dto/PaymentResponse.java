package com.zerobase.foodlier.module.payment.dto;

import lombok.*;

@Getter
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

    public void updateSuccessUrl(String url) {
        this.successUrl = url;
    }

    public void updateFailUrl(String url) {
        this.failUrl = url;
    }
}
