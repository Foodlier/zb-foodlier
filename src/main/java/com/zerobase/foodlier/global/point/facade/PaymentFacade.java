package com.zerobase.foodlier.global.point.facade;

import com.zerobase.foodlier.module.history.charge.service.PointChargeHistoryService;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PointChargeHistoryService pointChargeHistoryService;

    public void pointChargeAndCreateHistory(String paymentKey, String orderId,
                                            Long amount) {
        Payment payment =
                paymentService.requestFinalPayment(paymentKey, orderId, amount);
        pointChargeHistoryService.createPointChargeHistory(payment);
    }

    public void pointChargeCancelAndCreateHistory(String paymentKey,
                                                  String cancelReason) {
        Payment payment =
                paymentService.requestPaymentCancel(paymentKey, cancelReason);
        pointChargeHistoryService.createPointCancelHistory(payment);
    }
}
