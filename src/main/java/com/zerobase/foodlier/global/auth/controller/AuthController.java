package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailVerificationFacade emailVerificationFacade;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/verification/send/{email}")
    public ResponseEntity<?> sendVerificationCode(
            @PathVariable String email
    ){
        emailVerificationFacade.sendMailAndCreateVerification(email);
        return ResponseEntity.ok("인증 코드 전송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode
    ){
        emailVerificationService.verify(email, verificationCode,
                LocalDateTime.now());
        return ResponseEntity.ok("인증 완료");
    }

}
