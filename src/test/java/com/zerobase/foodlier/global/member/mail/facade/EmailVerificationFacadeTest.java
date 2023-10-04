package com.zerobase.foodlier.global.member.mail.facade;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.module.member.member.mail.service.MailService;
import com.zerobase.foodlier.module.member.member.mail.service.VerificationCodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailVerificationFacadeTest {

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private VerificationCodeService verificationCodeService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private EmailVerificationFacade emailVerificationFacade;

    @Test
    @DisplayName("이메일 검증 파서드 성공 케이스")
    void success_sendMailAndCreateVerification(){

        //given
        String email = "test178295031875@test.com";
        String verificationCode = "LzOXCMpEUo";
        LocalDateTime nowTime = LocalDateTime.of(2023, 9, 25, 9, 0, 0);


        given(verificationCodeService.createAuthenticationCode())
                .willReturn(verificationCode);

        //when
        emailVerificationFacade.sendMailAndCreateVerification(email, nowTime);

        //then
        verify(emailVerificationService, times(1)).createVerification(
                email, verificationCode, nowTime
        );

        verify(mailService, times(1)).sendMail(
                email, verificationCode
        );
    }

}