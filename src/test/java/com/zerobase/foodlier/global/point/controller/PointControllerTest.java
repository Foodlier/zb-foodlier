package com.zerobase.foodlier.global.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.point.facade.PaymentFacade;
import com.zerobase.foodlier.global.point.facade.TransactionFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.history.charge.service.PointChargeHistoryService;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.history.transaction.service.MemberBalanceHistoryService;
import com.zerobase.foodlier.module.history.type.TransactionType;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleFailDto;
import com.zerobase.foodlier.module.payment.service.PaymentService;
import com.zerobase.foodlier.module.payment.type.OrderNameType;
import com.zerobase.foodlier.module.payment.type.PayType;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PointController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class PointControllerTest {

    @MockBean
    private PaymentService paymentService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private PaymentFacade paymentFacade;
    @MockBean
    private TransactionFacade transactionFacade;
    @MockBean
    private MemberBalanceHistoryService memberBalanceHistoryService;
    @MockBean
    private PointChargeHistoryService pointChargeHistoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("충전 요청 성공")
    @WithCustomMockUser
    void success_requestPayments() throws Exception {
        //given
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .payType(PayType.CARD)
                .orderName(OrderNameType.CHARGE)
                .amount(1000L)
                .build();

        given(paymentService.requestPayments(any(), any()))
                .willReturn(PaymentResponse.builder()
                        .payType(PayType.CARD.name())
                        .amount(1000L)
                        .orderId("orderId")
                        .orderName(OrderNameType.CHARGE.name())
                        .customerEmail("test@test.com")
                        .customerNickName("test")
                        .successUrl("success")
                        .failUrl("fail")
                        .payDate(String.valueOf(LocalDate.now()))
                        .build());

        //when
        ResultActions perform = mockMvc.perform(post("/point/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.payType")
                                .value(paymentRequest.getPayType().name()),
                        jsonPath("$.amount")
                                .value(paymentRequest.getAmount()),
                        jsonPath("$.orderId")
                                .value("orderId"),
                        jsonPath("$.orderName")
                                .value(paymentRequest.getOrderName().name()),
                        jsonPath("$.customerEmail")
                                .value("test@test.com"),
                        jsonPath("$.customerNickName")
                                .value("test"),
                        jsonPath("$.successUrl")
                                .value("success"),
                        jsonPath("$.failUrl")
                                .value("fail"),
                        jsonPath("$.payDate")
                                .value(String.valueOf(LocalDate.now()))
                );
    }

    @Test
    @DisplayName("결제 완료 성공")
    @WithCustomMockUser
    void success_requestFinalPayments() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(get("/point/success?" +
                "paymentKey=paymentKey&orderId=orderId&amount=1000"));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("결제 완료, 금액 : 1000"));
    }

    @Test
    @DisplayName("결제 실패 성공")
    @WithCustomMockUser
    void success_requestFail() throws Exception {
        //given
        PaymentResponseHandleFailDto paymentResponseHandleFailDto =
                PaymentResponseHandleFailDto.builder()
                        .errorCode("errorCode")
                        .errorMsg("결제실패")
                        .orderId("orderId")
                        .build();

        given(paymentService.requestFail(any(), any(), any()))
                .willReturn(paymentResponseHandleFailDto);

        //when
        ResultActions perform = mockMvc.perform(get("/point/fail?" +
                "code=errorCode&message=결제실패&orderId=orderId"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.errorCode")
                                .value(paymentResponseHandleFailDto.getErrorCode()),
                        jsonPath("$.errorMsg")
                                .value(paymentResponseHandleFailDto.getErrorMsg()),
                        jsonPath("$.orderId")
                                .value(paymentResponseHandleFailDto.getOrderId())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("결제 취소 성공")
    void success_requestPaymentCancel() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(post("/point/cancel?" +
                "paymentKey=paymentKey&cancelReason=단순변심")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("결제 취소 완료, 이유 : 단순변심")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("가격 제안 성공")
    void success_suggestPrice() throws Exception {
        //given
        SuggestionForm suggestionForm = new SuggestionForm(1000);

        given(transactionService.sendSuggestion(any(), any(), anyLong()))
                .willReturn("가격을 제안했습니다.");

        //when
        ResultActions perform = mockMvc.perform(post("/point/suggest/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suggestionForm))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("가격을 제안했습니다.")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("가격 제안 취소 성공")
    void success_cancelSuggestion() throws Exception {
        //given
        given(transactionService.cancelSuggestion(any(), anyLong()))
                .willReturn("제안이 취소되었습니다.");

        //when
        ResultActions perform = mockMvc.perform(post("/point/suggest/cancel/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("제안이 취소되었습니다.")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("제안 수락 성공")
    void success_approveSuggestion() throws Exception {
        //given

        //when
        ResultActions perform = mockMvc.perform(patch("/point/suggest/approve/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("제안을 수락했습니다.")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("제안 거절 성공")
    void success_rejectSuggestion() throws Exception {
        //given
        given(transactionService.rejectSuggestion(any(), anyLong()))
                .willReturn("제안을 거절하였습니다.");

        //when
        ResultActions perform = mockMvc.perform(patch("/point/suggest/reject/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string("제안을 거절하였습니다.")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("포인 충전 내역 조회 성공")
    void success_getPointHistory() throws Exception {
        //given
        PointChargeHistoryDto pointCharge = PointChargeHistoryDto.builder()
                .paymentKey("paymentKey1")
                .chargePoint(1000L)
                .description(TransactionType.CHARGE_POINT.getDescription())
                .chargeAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
                .build();
        PointChargeHistoryDto pointChargeCancel = PointChargeHistoryDto.builder()
                .paymentKey("paymentKey2")
                .chargePoint(1000L)
                .description(TransactionType.CHARGE_CANCEL.getDescription())
                .chargeAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
                .build();


        given(pointChargeHistoryService.getPointHistory(any(), any()))
                .willReturn(ListResponse.from(new PageImpl<>(
                        List.of(pointCharge, pointChargeCancel)
                )));


        //when
        ResultActions perform = mockMvc.perform(get("/point/charge/0/10"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].chargePoint")
                                .value(pointCharge.getChargePoint()),
                        jsonPath("$.content.[0].paymentKey")
                                .value(pointCharge.getPaymentKey()),
                        jsonPath("$.content.[0].chargeAt")
                                .value("2023-12-25 01:23:45"),
                        jsonPath("$.content.[0].description")
                                .value(pointCharge.getDescription()),

                        jsonPath("$.content.[1].chargePoint")
                                .value(pointChargeCancel.getChargePoint()),
                        jsonPath("$.content.[1].paymentKey")
                                .value(pointChargeCancel.getPaymentKey()),
                        jsonPath("$.content.[1].chargeAt")
                                .value("2023-12-25 01:23:45"),
                        jsonPath("$.content.[1].description")
                                .value(pointChargeCancel.getDescription())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("거래 내역 조회 성공")
    void success_getTransactionHistory() throws Exception {
        //given
        MemberBalanceHistoryDto memberBalanceHistoryDto = MemberBalanceHistoryDto.builder()
                .currentPoint(1000)
                .changePoint(1000)
                .sender("testSender")
                .description(TransactionType.POINT_RECEIVE.getDescription())
                .transactionAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
                .build();

        given(memberBalanceHistoryService.getTransactionHistory(any(), any()))
                .willReturn(ListResponse.from(new PageImpl<>(
                        List.of(memberBalanceHistoryDto)
                )));

        //when
        ResultActions perform = mockMvc.perform(get("/point/transaction/0/10"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].changePoint")
                                .value(memberBalanceHistoryDto.getChangePoint()),
                        jsonPath("$.content.[0].currentPoint")
                                .value(memberBalanceHistoryDto.getCurrentPoint()),
                        jsonPath("$.content.[0].sender")
                                .value(memberBalanceHistoryDto.getSender()),
                        jsonPath("$.content.[0].transactionAt")
                                .value("2023-12-25 01:23:45"),
                        jsonPath("$.content.[0].description")
                                .value(memberBalanceHistoryDto.getDescription())
                );
    }
}