package com.zerobase.foodlier.module.payment.dto;

import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    @Enumerated(EnumType.STRING)
    private PayType payType;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private OrderNameType orderName;

    public static Payment from(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(UUID.randomUUID().toString())
                .payType(paymentRequest.getPayType())
                .amount(paymentRequest.getAmount())
                .orderName(paymentRequest.getOrderName())
                .payDate(String.valueOf(LocalDateTime.now()))
                .build();
    }

}
