package com.zerobase.foodlier.global.point.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {
    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<?> requestPayments(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(paymentService.requestPayments(paymentRequest, memberAuthDto));
    }

    /**
     * redirect 되는 부분 협의 필요
     */
    @GetMapping("/success")
    public ResponseEntity<?> requestFinalPayments(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        paymentService.requestFinalPayment(paymentKey, orderId, amount);
        return ResponseEntity.ok("결제 완료, 금액 : " + amount);
    }

    @GetMapping("/fail")
    public ResponseEntity<?> requestFail(
            @RequestParam(name = "code") String errorCode,
            @RequestParam(name = "message") String errorMsg,
            @RequestParam(name = "orderId") String orderId
    ) {
        return ResponseEntity.ok(paymentService.requestFail(errorCode, errorMsg, orderId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> requestPaymentCancel(
            @RequestParam String paymentKey,
            @RequestParam String cancelReason
    ) {
        paymentService.requestPaymentCancel(paymentKey, cancelReason);
        return ResponseEntity.ok("결제 취소 완료");
    }
}
