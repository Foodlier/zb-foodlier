package com.zerobase.foodlier.module.payment.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleFailDto;
import com.zerobase.foodlier.module.payment.exception.PaymentException;
import com.zerobase.foodlier.module.payment.repository.PaymentRepository;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.payment.exception.PaymentErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PointChargeHistoryRepository pointChargeHistoryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("주문 요청 성공")
    void success_requestPayments() {
        //given
        ReflectionTestUtils.setField(paymentService, "successCallBackUrl",
                "http://localhost:8080/point/success");
        ReflectionTestUtils.setField(paymentService, "failCallBackUrl",
                "http://localhost:8080/point/fail");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payType(PayType.CARD)
                .amount(1000L)
                .orderName(OrderNameType.CHARGE)
                .build();

        Payment payment = PaymentRequest.from(paymentRequest);

        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@test.com")
                .roles(new ArrayList<>())
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .email(memberAuthDto.getEmail())
                        .nickname("test")
                        .build()));
        given(paymentRepository.save(any()))
                .willReturn(payment);

        //when
        PaymentResponse paymentResponse =
                paymentService.requestPayments(paymentRequest,
                        memberAuthDto);

        //then
        assertAll(
                () -> assertEquals(payment.getPayType().name(),
                        paymentResponse.getPayType()),
                () -> assertEquals(1000L,
                        paymentResponse.getAmount()),
                () -> assertEquals(payment.getOrderId().length(),
                        paymentResponse.getOrderId().length()),
                () -> assertEquals(payment.getOrderName().name(),
                        paymentResponse.getOrderName()),
                () -> assertEquals("test@test.com",
                        paymentResponse.getCustomerEmail()),
                () -> assertEquals("test",
                        paymentResponse.getCustomerNickName()),
                () -> assertEquals("http://localhost:8080/point/success",
                        paymentResponse.getSuccessUrl()),
                () -> assertEquals("http://localhost:8080/point/fail",
                        paymentResponse.getFailUrl()),
                () -> assertEquals(payment.getPayDate(),
                        paymentResponse.getPayDate())
        );
    }

    @Test
    @DisplayName("주문 요청 실패 - 멤버를 찾을 수 없음")
    void fail_requestPayments_memberNotFound() {
        //given
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> paymentService.requestPayments(new PaymentRequest(),
                        new MemberAuthDto()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("결제 성공 redirect 성공")
    void success_requestFinalPayment() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.of(Payment.builder()
                        .member(Member.builder()
                                .point(0L)
                                .build())
                        .amount(1000L)
                        .build()));

        //when
        paymentService.requestFinalPayment("paymentKey", "orderId", 1000L);

        //then
        ArgumentCaptor<Payment> paymentCaptor =
                ArgumentCaptor.forClass(Payment.class);
        ArgumentCaptor<PointChargeHistory> pointChargeHistoryCaptor =
                ArgumentCaptor.forClass(PointChargeHistory.class);

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(HttpEntity.class), any());
        verify(paymentRepository, times(2))
                .findByOrderId("orderId");
        verify(paymentRepository, times(1))
                .save(paymentCaptor.capture());
        verify(pointChargeHistoryRepository, times(1))
                .save(pointChargeHistoryCaptor.capture());

        Payment payment = paymentCaptor.getValue();
        PointChargeHistory pointChargeHistory = pointChargeHistoryCaptor.getValue();

        assertAll(
                () -> assertEquals("Y", payment.getPaySuccessYn()),
                () -> assertEquals("paymentKey", payment.getPaymentKey())
        );

        assertAll(
                () -> assertEquals("paymentKey",
                        pointChargeHistory.getPaymentKey()),
                () -> assertEquals(1000L, pointChargeHistory.getChargePoint())
        );
    }

    @Test
    @DisplayName("결제 성공 redirect 실패 - 요청 금액과 결제 금액이 다름")
    void fail_requestFinalPayment_paymentErrorOrderAmount() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.ofNullable(Payment.builder()
                        .amount(100L)
                        .build()));

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestFinalPayment("paymentKey",
                        "orderId", 1000L));

        //then
        assertEquals(PAYMENT_ERROR_ORDER_AMOUNT,
                paymentException.getErrorCode());
    }

    @Test
    @DisplayName("결제 성공 redirect 실패 - 정의되지 않은 오류")
    void fail_requestFinalPayment_undefinedError() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.empty());

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestFinalPayment("paymentKey",
                        "orderId", 1000L));

        //then
        assertEquals(UNDEFINED_ERROR,
                paymentException.getErrorCode());
    }

    @Test
    @DisplayName("결제 성공 redirect 실패 - 이미 처리된 결제")
    void fail_requestFinalPayment_alreadyProcessedPayment() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.ofNullable(Payment.builder()
                        .amount(1000L)
                        .build()));
        given(restTemplate.postForEntity(anyString(), any(HttpEntity.class), any()))
                .willThrow(HttpClientErrorException.class);

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestFinalPayment("paymentKey",
                        "orderId", 1000L));

        //then
        assertEquals(ALREADY_PROCESSED_PAYMENT,
                paymentException.getErrorCode());
    }

    @Test
    @DisplayName("결제 실패 redirect 성공")
    void success_requestFail() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.of(new Payment()));
        given(paymentRepository.save(any()))
                .willReturn(Payment.builder()
                        .paySuccessYn("N")
                        .payFailReason("오류")
                        .build());

        //when
        PaymentResponseHandleFailDto paymentResponseHandleFailDto =
                paymentService.requestFail("-1", "오류",
                        "orderId");

        //then
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1))
                .save(captor.capture());
        Payment value = captor.getValue();

        assertAll(
                () -> assertEquals("N", value.getPaySuccessYn()),
                () -> assertEquals("오류", value.getPayFailReason())
        );

        assertAll(
                () -> assertEquals("-1",
                        paymentResponseHandleFailDto.getErrorCode()),
                () -> assertEquals("오류",
                        paymentResponseHandleFailDto.getErrorMsg()),
                () -> assertEquals("orderId",
                        paymentResponseHandleFailDto.getOrderId())
        );
    }

    @Test
    @DisplayName("결제 실패 redirect 실패 - 주문 요청을 찾을 수 없음")
    void fail_requestFail_paymentRequestNotFound() {
        //given
        given(paymentRepository.findByOrderId(anyString()))
                .willReturn(Optional.empty());

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestFail("-1", "오류",
                        "orderId"));

        //then
        assertEquals(PAYMENT_REQUEST_NOT_FOUND, paymentException.getErrorCode());
    }

    @Test
    @DisplayName("결제 취소 성공")
    void success_requestPaymentCancel() {
        //given
        Payment payment = Payment.builder()
                .paySuccessYn("Y")
                .amount(1000L)
                .member(Member.builder()
                        .point(1000L)
                        .build())
                .isCanceled(true)
                .build();

        given(paymentRepository.findByPaymentKey(anyString()))
                .willReturn(Optional.ofNullable(payment));
        given(paymentRepository.save(any()))
                .willReturn(payment);

        //when
        String reason = paymentService.requestPaymentCancel("paymentKey",
                "단순 변심");

        //then
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1))
                .save(captor.capture());
        Payment value = captor.getValue();

        assertAll(
                () -> assertTrue(value.isCanceled()),
                () -> assertEquals(0L, value.getMember().getPoint()),
                () -> assertEquals("단순 변심", reason)
        );
    }

    @Test
    @DisplayName("결제 취소 실패 - restTemplate 오류")
    void fail_requestPaymentCancel_paymentCancelError() {
        //given
        given(restTemplate.postForObject(any(), any(HttpEntity.class), any()))
                .willThrow(HttpClientErrorException.class);

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestPaymentCancel("paymentKey",
                        "단순 변심"));

        //then
        assertEquals(PAYMENT_CANCEL_ERROR, paymentException.getErrorCode());
    }

    @Test
    @DisplayName("결제 취소 실패 - 요청을 찾을 수 없음")
    void fail_requestPaymentCancel_paymentRequestNotFound() {
        //given
        given(paymentRepository.findByPaymentKey(anyString()))
                .willReturn(Optional.empty());

        //when
        PaymentException paymentException = assertThrows(PaymentException.class,
                () -> paymentService.requestPaymentCancel("paymentKey",
                        "단순 변심"));

        //then
        assertEquals(PAYMENT_REQUEST_NOT_FOUND, paymentException.getErrorCode());
    }
}