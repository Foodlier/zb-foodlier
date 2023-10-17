package com.zerobase.foodlier.global.point.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.point.facade.PaymentFacade;
import com.zerobase.foodlier.global.point.facade.TransactionFacade;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.history.charge.service.PointChargeHistoryService;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.history.transaction.service.MemberBalanceHistoryService;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleFailDto;
import com.zerobase.foodlier.module.payment.service.PaymentService;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final PaymentFacade paymentFacade;
    private final TransactionFacade transactionFacade;
    private final MemberBalanceHistoryService memberBalanceHistoryService;
    private final PointChargeHistoryService pointChargeHistoryService;

    @PostMapping("/charge")
    public ResponseEntity<PaymentResponse> requestPayments(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody @Valid PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(paymentService.requestPayments(paymentRequest, memberAuthDto));
    }

    /**
     * redirect 되는 부분 협의 필요
     */
    @GetMapping("/success")
    public ResponseEntity<String> requestFinalPayments(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        paymentFacade.pointChargeAndCreateHistory(paymentKey, orderId, amount);
        return ResponseEntity.ok("결제 완료, 금액 : " + amount);
    }

    @GetMapping("/fail")
    public ResponseEntity<PaymentResponseHandleFailDto> requestFail(
            @RequestParam(name = "code") String errorCode,
            @RequestParam(name = "message") String errorMsg,
            @RequestParam(name = "orderId") String orderId
    ) {
        return ResponseEntity.ok(paymentService.requestFail(errorCode, errorMsg, orderId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> requestPaymentCancel(
            @RequestParam String paymentKey,
            @RequestParam String cancelReason
    ) {
        paymentFacade.pointChargeCancelAndCreateHistory(paymentKey, cancelReason);
        return ResponseEntity.ok("결제 취소 완료, 이유 : " + cancelReason);
    }

    @PostMapping("/suggest/{dmRoomId}")
    public ResponseEntity<String> suggestPrice(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody @Valid SuggestionForm form,
            @PathVariable(name = "dmRoomId") Long dmRoomId
    ) {
        return ResponseEntity.ok(transactionService
                .sendSuggestion(memberAuthDto, form, dmRoomId));
    }

    @PostMapping("/suggest/cancel/{dmRoomId}")
    public ResponseEntity<String> cancelSuggestion(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "dmRoomId") Long dmRoomId
    ) {
        return ResponseEntity.ok(transactionService
                .cancelSuggestion(memberAuthDto, dmRoomId));
    }

    @PatchMapping("/suggest/approve/{dmRoomId}")
    public ResponseEntity<String> approveSuggestion(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "dmRoomId") Long dmRoomId
    ) {
        transactionFacade
                .pointTransactionAndSaveHistory(memberAuthDto, dmRoomId);
        return ResponseEntity.ok("제안을 수락했습니다.");
    }

    @PatchMapping("/suggest/reject/{dmRoomId}")
    public ResponseEntity<String> rejectSuggestion(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "dmRoomId") Long dmRoomId
    ) {
        return ResponseEntity.ok(transactionService
                .rejectSuggestion(memberAuthDto, dmRoomId));
    }

    @GetMapping("/charge/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<PointChargeHistoryDto>> getPointHistory(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(pointChargeHistoryService
                .getPointHistory(memberAuthDto, PageRequest.of(pageIdx, pageSize)));
    }

    @GetMapping("/transaction/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<MemberBalanceHistoryDto>> getTransactionHistory(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(memberBalanceHistoryService
                .getTransactionHistory(memberAuthDto, PageRequest.of(pageIdx, pageSize)));
    }
}
