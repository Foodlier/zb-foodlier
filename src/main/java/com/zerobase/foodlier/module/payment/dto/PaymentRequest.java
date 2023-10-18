package com.zerobase.foodlier.module.payment.dto;

import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    @NotNull(message = "결제 수단을 입력해주세요.")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @NotNull(message = "금액을 입력해주세요.")
    @Min(value = 1000, message = "1000원 부터 충전이 가능합니다.")
    private Long amount;
    @NotNull(message = "주문 유형을 입력해주세요.")
    @Enumerated(EnumType.STRING)
    private OrderNameType orderName;

    public static Payment from(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(UUID.randomUUID().toString())
                .payType(paymentRequest.getPayType())
                .amount(paymentRequest.getAmount())
                .orderName(paymentRequest.getOrderName())
                .payDate(String.valueOf(LocalDate.now()))
                .build();
    }
}
