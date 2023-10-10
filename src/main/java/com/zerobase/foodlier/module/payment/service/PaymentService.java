package com.zerobase.foodlier.module.payment.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleFailDto;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {
    @Transactional
    PaymentResponse requestPayments(PaymentRequest paymentRequest, MemberAuthDto memberAuthDto);

    @Transactional
    Payment requestFinalPayment(String paymentKey, String orderId, Long amount);

    @Transactional
    PaymentResponseHandleFailDto requestFail(String errorCode, String errorMsg, String orderId);

    @Transactional
    Payment requestPaymentCancel(String paymentKey, String cancelReason);
}
