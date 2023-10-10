package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import com.zerobase.foodlier.global.member.password.facade.PasswordFindFacade;
import com.zerobase.foodlier.global.member.regiser.facade.MemberRegisterFacade;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailVerificationFacade emailVerificationFacade;
    private final EmailVerificationService emailVerificationService;
    private final MemberRegisterFacade memberRegisterFacade;
    private final MemberService memberService;
    private final PasswordFindFacade passwordFindFacade;

    @PostMapping("/verification/send/{email}")
    public ResponseEntity<String> sendVerificationCode(
            @PathVariable String email
    ) {
        emailVerificationFacade.sendMailAndCreateVerification(email, LocalDateTime.now());
        return ResponseEntity.ok("인증 코드 전송 완료");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(
            @RequestParam("email") String email,
            @RequestParam("verificationCode") String verificationCode
    ) {
        emailVerificationService.verify(email, verificationCode,
                LocalDateTime.now());
        return ResponseEntity.ok("인증 완료");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@ModelAttribute @Valid MemberInputDto memberInputDto) {
        memberRegisterFacade.domainRegister(memberInputDto);
        return ResponseEntity.ok(
                "회원가입 완료"
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signIn(
            @RequestBody @Valid SignInForm form
    ) {
        return ResponseEntity.ok(memberService.signIn(form));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        memberService.signOut(memberAuthDto.getEmail());

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> passwordFind(
            @RequestBody @Valid PasswordFindForm form
    ) {
        return ResponseEntity.ok(passwordFindFacade
                .sendMailAndUpdateNewPassword(form));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(memberService.withdraw(memberAuthDto));
    }
}
