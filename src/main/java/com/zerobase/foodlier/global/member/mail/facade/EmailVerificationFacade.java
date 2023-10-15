package com.zerobase.foodlier.global.member.mail.facade;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.module.member.member.mail.service.MailService;
import com.zerobase.foodlier.module.member.member.mail.service.RandomCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.zerobase.foodlier.module.member.member.type.MailSendType.REGISTER;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailVerificationFacade {

    private final EmailVerificationService emailVerificationService;
    private final RandomCodeService randomCodeService;
    private final MailService mailService;

    public void sendMailAndCreateVerification(String email, LocalDateTime nowTime) {
        String verificationCode = randomCodeService
                .createIntegerRandomCode();

        emailVerificationService.createVerification(email, verificationCode,
                nowTime);

        mailService.sendMail(email, verificationCode, REGISTER);
    }

}
