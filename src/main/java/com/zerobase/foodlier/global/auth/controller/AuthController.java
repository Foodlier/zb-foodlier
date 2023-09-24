package com.zerobase.foodlier.global.auth.controller;

import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;
import com.zerobase.foodlier.global.request.facade.TokenFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final TokenFacade tokenFacade;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInForm form) {
        return ResponseEntity.ok(tokenFacade.createToken(form));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@RequestBody String refreshToken) {
        refreshTokenService.signOut(refreshToken);

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
