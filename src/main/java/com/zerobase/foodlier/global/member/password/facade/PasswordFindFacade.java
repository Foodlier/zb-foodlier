package com.zerobase.foodlier.global.member.password.facade;

import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.mail.service.MailService;
import com.zerobase.foodlier.module.member.member.mail.service.RandomCodeService;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.zerobase.foodlier.module.member.member.type.MailSendType.PASSWORD_FIND;

@Component
@RequiredArgsConstructor
public class PasswordFindFacade {
    private final MailService mailService;
    private final MemberService memberService;
    private final RandomCodeService randomCodeService;

    public String sendMailAndUpdateNewPassword(PasswordFindForm form) {
        String newPassword = randomCodeService.createRandomCode();

        String result =
                memberService.updateRandomPassword(form, newPassword);

        mailService.sendMail(form.getEmail(), newPassword, PASSWORD_FIND);

        return result;
    }
}
