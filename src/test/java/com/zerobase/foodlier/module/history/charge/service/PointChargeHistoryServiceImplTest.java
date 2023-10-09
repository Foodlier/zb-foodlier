package com.zerobase.foodlier.module.history.charge.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointChargeHistoryServiceImplTest {
    @Mock
    private PointChargeHistoryRepository pointChargeHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

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

    @Test
    @DisplayName("충전 내역 조회 성공")
    void success_getPointHistory() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .id(1L)
                        .build()));
        given(pointChargeHistoryRepository.findByMemberOrderByCreatedAtDesc(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(
                        List.of(
                                PointChargeHistory.builder()
                                        .paymentKey("paymentKey1")
                                        .chargePoint(1000L)
                                        .description("포인트 충전")
                                        .createdAt(LocalDateTime.of(2023, 10, 9, 0, 0))
                                        .build(),
                                PointChargeHistory.builder()
                                        .paymentKey("paymentKey2")
                                        .chargePoint(1000L)
                                        .createdAt(LocalDateTime.of(2023, 10, 9, 0, 0))
                                        .description("포인트 충전")
                                        .build(),
                                PointChargeHistory.builder()
                                        .paymentKey("paymentKey1")
                                        .chargePoint(-1000L)
                                        .createdAt(LocalDateTime.of(2023, 10, 9, 0, 0))
                                        .description("결제 취소")
                                        .build()
                        ))));

        //when
        List<PointChargeHistoryDto> pointHistoryList = pointChargeHistoryService.getPointHistory(MemberAuthDto.builder()
                .id(1L)
                .build(), PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals("paymentKey1",
                        pointHistoryList.get(0).getPaymentKey()),
                () -> assertEquals(1000L,
                        pointHistoryList.get(0).getChargePoint()),
                () -> assertEquals("2023-10-09",
                        pointHistoryList.get(0).getChargeAt()),
                () -> assertEquals("포인트 충전",
                        pointHistoryList.get(0).getDescription()),

                () -> assertEquals("paymentKey2",
                        pointHistoryList.get(1).getPaymentKey()),
                () -> assertEquals(1000L,
                        pointHistoryList.get(1).getChargePoint()),
                () -> assertEquals("2023-10-09",
                        pointHistoryList.get(1).getChargeAt()),
                () -> assertEquals("포인트 충전",
                        pointHistoryList.get(1).getDescription()),

                () -> assertEquals("paymentKey1",
                        pointHistoryList.get(2).getPaymentKey()),
                () -> assertEquals(-1000L,
                        pointHistoryList.get(2).getChargePoint()),
                () -> assertEquals("2023-10-09",
                        pointHistoryList.get(2).getChargeAt()),
                () -> assertEquals("결제 취소",
                        pointHistoryList.get(2).getDescription())
        );
    }

    @Test
    @DisplayName("충전 내역 조회 실패 - 회원을 찾을 수 없음")
    void fail_getPointHistory_memberNotFound() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> pointChargeHistoryService.getPointHistory(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10)));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }
}