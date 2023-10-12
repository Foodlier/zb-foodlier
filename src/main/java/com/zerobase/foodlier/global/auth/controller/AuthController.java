package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.common.socialLogin.dto.KakaoLoginParams;
import com.zerobase.foodlier.common.socialLogin.dto.NaverLoginParams;
import com.zerobase.foodlier.common.socialLogin.dto.SocialLoginResponse;
import com.zerobase.foodlier.common.socialLogin.facade.OAuthFacade;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import com.zerobase.foodlier.global.member.regiser.facade.MemberRegisterFacade;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
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
    private final MemberRegisterFacade memberRegisterFacade;
    private final MemberService memberService;
    private final OAuthFacade oAuthFacade;

    @PostMapping("/verification/send/{email}")
    public ResponseEntity<?> sendVerificationCode(
            @PathVariable String email
    ) {
        emailVerificationFacade.sendMailAndCreateVerification(email, LocalDateTime.now());
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

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@ModelAttribute MemberInputDto memberInputDto) {
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

    @PostMapping("/oauth2/kakao")
    public ResponseEntity<SocialLoginResponse> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthFacade.login(params));
    }

    @PostMapping("/oauth2/naver")
    public ResponseEntity<SocialLoginResponse> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthFacade.login(params));
    }

}
