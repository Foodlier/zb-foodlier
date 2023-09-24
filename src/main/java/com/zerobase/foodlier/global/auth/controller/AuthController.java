package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.common.security.provider.vo.MemberVo;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final EmailVerificationFacade emailVerificationFacade;
    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;

    @PostMapping("/verification/send/{email}")
    public ResponseEntity<?> sendVerificationCode(
            @PathVariable String email
    ) {
        emailVerificationFacade.sendMailAndCreateVerification(email);
        return ResponseEntity.ok("인증 코드 전송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode
    ) {
        emailVerificationService.verify(email, verificationCode,
                LocalDateTime.now());
        return ResponseEntity.ok("인증 완료");
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody @Valid SignInForm form) {
        return ResponseEntity.ok(memberService.signIn(form));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@AuthenticationPrincipal MemberVo memberVo) {
        memberService.signOut(memberVo.getEmail());

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

}
