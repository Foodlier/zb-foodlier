package com.zerobase.foodlier.module.payment.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class Payment extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private String orderId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderNameType orderName;
    @Column(nullable = false)
    private String customerEmail;
    @Column(nullable = false)
    private String customerNickName;
    @Column(nullable = false)
    private String payDate;
    private String paymentKey;
    private String paySuccessYn;
    private String payFailReason;
    private boolean isCanceled;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member member;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .payType(String.valueOf(payment.getPayType()))
                .amount(payment.getAmount())
                .orderId(payment.getOrderId())
                .orderName(String.valueOf(payment.getOrderName()))
                .customerEmail(payment.getCustomerEmail())
                .customerNickName(payment.getCustomerNickName())
                .payDate(payment.getPayDate())
                .build();
    }

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateCustomerEmail(String email) {
        this.customerEmail = email;
    }

    public void updateCustomerNickName(String nickName) {
        this.customerNickName = nickName;
    }

    public void updatePaySuccessYn(String value) {
        this.paySuccessYn = value;
    }

    public void updatePayFailReason(String reason) {
        this.payFailReason = reason;
    }

    public void updateCanceled() {
        this.isCanceled = true;
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

}
