package com.zerobase.foodlier.module.payment.dto;

import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    @ApiModelProperty("지불방법")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @ApiModelProperty("지불금액")
    private Long amount;
    @ApiModelProperty("주문 상품 이름")
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
