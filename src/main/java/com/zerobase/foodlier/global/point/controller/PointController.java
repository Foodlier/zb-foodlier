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
            @ModelAttribute PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(paymentService.requestPayments(paymentRequest, memberAuthDto));
    }

    @GetMapping("/success")
    public ResponseEntity<?> requestFinalPayments(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        return ResponseEntity.ok(paymentService.requestFinalPayment(paymentKey, orderId, amount));
    }

    @GetMapping("/redirect/success")
    public void requestSuccess(){

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
        return ResponseEntity.ok(paymentService.requestPaymentCancel(paymentKey, cancelReason));
    }
}
