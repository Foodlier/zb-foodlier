package com.zerobase.foodlier.global.member.mail.facade;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.module.member.member.mail.service.MailService;
import com.zerobase.foodlier.module.member.member.mail.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailVerificationFacade {

    private final EmailVerificationService emailVerificationService;
    private final VerificationCodeService verificationCodeService;
    private final MailService mailService;

    public void sendMailAndCreateVerification(String email){
        String verificationCode = verificationCodeService
                .createAuthenticationCode();

        emailVerificationService.createVerification(email, verificationCode,
                LocalDateTime.now());

        mailService.sendMail(email, verificationCode);
    }

}
