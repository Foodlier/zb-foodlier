package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.global.member.mail.facade.EmailVerificationFacade;
import com.zerobase.foodlier.global.member.oAuth.facade.OAuthFacade;
import com.zerobase.foodlier.global.member.password.facade.PasswordFindFacade;
import com.zerobase.foodlier.global.member.regiser.facade.MemberRegisterFacade;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.social.dto.KakaoLoginParams;
import com.zerobase.foodlier.module.member.member.social.dto.NaverLoginParams;
import com.zerobase.foodlier.module.member.member.social.dto.SocialLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.REFRESH_HEADER;
import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final EmailVerificationFacade emailVerificationFacade;
    private final EmailVerificationService emailVerificationService;
    private final MemberRegisterFacade memberRegisterFacade;
    private final MemberService memberService;
    private final PasswordFindFacade passwordFindFacade;
    private final OAuthFacade oAuthFacade;

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
        return ResponseEntity.ok(memberService.signIn(form, new Date()));
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

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(@RequestHeader(REFRESH_HEADER) final String refreshToken) {
        return ResponseEntity.ok(memberService.reissue(refreshToken.substring(TOKEN_PREFIX.length())));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(memberService.withdraw(memberAuthDto));
    }

    @PostMapping("/oauth2/kakao")
    public ResponseEntity<SocialLoginResponse> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthFacade.login(params));
    }

    @PostMapping("/oauth2/naver")
    public ResponseEntity<SocialLoginResponse> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthFacade.login(params));
    }


    @GetMapping("/check/nickname")
    public ResponseEntity<String> checkNickname(
            @RequestParam String nickname
    ) {
        memberService.checkNickname(nickname);
        return ResponseEntity.ok(
                "사용가능한 닉네임 입니다."
        );
    }

    @GetMapping("/check/phone")
    public ResponseEntity<String> checkPhoneNumber(
            @RequestParam String phoneNumber
    ) {
        memberService.checkPhoneNumber(phoneNumber);
        return ResponseEntity.ok(
                "사용가능한 전화번호 입니다."
        );
    }

    @GetMapping("/check/email")
    public ResponseEntity<String> checkEmail(
            @RequestParam String email
    ) {
        memberService.checkEmail(email);
        return ResponseEntity.ok(
                "사용가능한 이메일 입니다."
        );
    }
}
