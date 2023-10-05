package com.zerobase.foodlier.module.history.charge.service;

import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointChargeHistoryServiceImplTest {
    @Mock
    private PointChargeHistoryRepository pointChargeHistoryRepository;

    @InjectMocks
    private PointChargeHistoryServiceImpl pointChargeHistoryService;

    @Test
    @DisplayName("충전 내역 생성 성공")
    void success_createPointChargeHistory() {
        //given
        Payment payment = Payment.builder()
                .paymentKey("paymentKey")
                .member(Member.builder()
                        .id(1L)
                        .build())
                .amount(1000L)
                .build();

        given(pointChargeHistoryRepository.save(any()))
                .willReturn(PointChargeHistory.builder()
                        .paymentKey("paymentKey")
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .chargePoint(1000L)
                        .build());

        ArgumentCaptor<PointChargeHistory> captor =
                ArgumentCaptor.forClass(PointChargeHistory.class);

        //when
        pointChargeHistoryService.createPointChargeHistory(payment);

        //then
        verify(pointChargeHistoryRepository, times(1))
                .save(captor.capture());

        PointChargeHistory value = captor.getValue();
        assertAll(
                () -> assertEquals("paymentKey", value.getPaymentKey()),
                () -> assertEquals(1L, value.getMember().getId()),
                () -> assertEquals(1000L, value.getChargePoint())
        );
    }

    @Test
    @DisplayName("결제 취소 충전 내역 생성 성공")
    void success_createPointCancelHistory() {
        //given
        Payment payment = Payment.builder()
                .paymentKey("paymentKey")
                .member(Member.builder()
                        .id(1L)
                        .build())
                .amount(1000L)
                .build();

        given(pointChargeHistoryRepository.save(any()))
                .willReturn(PointChargeHistory.builder()
                        .paymentKey("paymentKey")
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .chargePoint(1000L)
                        .build());

        ArgumentCaptor<PointChargeHistory> captor =
                ArgumentCaptor.forClass(PointChargeHistory.class);

        //when
        pointChargeHistoryService.createPointCancelHistory(payment);

        //then
        verify(pointChargeHistoryRepository, times(1))
                .save(captor.capture());

        PointChargeHistory value = captor.getValue();
        assertAll(
                () -> assertEquals("paymentKey", value.getPaymentKey()),
                () -> assertEquals(1L, value.getMember().getId()),
                () -> assertEquals(-1000L, value.getChargePoint())
        );
    }
}