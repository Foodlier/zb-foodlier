package com.zerobase.foodlier.global.point.facade;

import com.zerobase.foodlier.module.history.charge.service.PointChargeHistoryService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {
    @Mock
    private PaymentService paymentService;

    @Mock
    private PointChargeHistoryService pointChargeHistoryService;

    @InjectMocks
    private PaymentFacade paymentFacade;

    @Test
    @DisplayName("결제 완료 및 내역 생성")
    void success_pointChargeAndCreateHistory() {
        //given
        Payment payment = Payment.builder()
                .paySuccessYn("Y")
                .amount(1000L)
                .member(Member.builder()
                        .id(1L)
                        .point(1000L)
                        .build())
                .isCanceled(false)
                .build();

        given(paymentService.requestFinalPayment(anyString(), anyString(), anyLong()))
                .willReturn(payment);

        //when
        paymentFacade.pointChargeAndCreateHistory("paymentKey",
                "orderId", 1000L);

        //then
        verify(paymentService, times(1))
                .requestFinalPayment("paymentKey", "orderId", 1000L);
        verify(pointChargeHistoryService, times(1))
                .createPointChargeHistory(payment);
    }

    @Test
    @DisplayName("결제 취소 및 내역 생성 성공")
    void success_pointChargeCancelAndCreateHistory() {
        //given
        Payment payment = Payment.builder()
                .paySuccessYn("Y")
                .amount(1000L)
                .member(Member.builder()
                        .id(1L)
                        .point(1000L)
                        .build())
                .isCanceled(true)
                .build();

        given(paymentService.requestPaymentCancel(anyString(), anyString()))
                .willReturn(payment);

        //when
        paymentFacade.pointChargeCancelAndCreateHistory("paymentKey",
                "단순 변심");

        //then
        verify(paymentService, times(1))
                .requestPaymentCancel("paymentKey", "단순 변심");
        verify(pointChargeHistoryService, times(1))
                .createPointCancelHistory(payment);
    }
}