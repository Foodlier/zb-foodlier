package com.zerobase.foodlier.module.member.member.mail.service;

import com.zerobase.foodlier.module.member.member.mail.exception.MailException;
import com.zerobase.foodlier.module.member.member.type.MailSendType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import static com.zerobase.foodlier.module.member.member.mail.constants.MailConstants.*;
import static com.zerobase.foodlier.module.member.member.mail.exception.MailErrorCode.CREATE_MAIL_MESSAGE_ERROR;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String email, String randomCode, MailSendType mailSendType) {
        MimeMessage message = createMailMessage(email, randomCode, mailSendType);
        mailSender.send(message);
    }


    private MimeMessage createMailMessage(String email,
                                              String randomCode,
                                              MailSendType mailSendType) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);

            switch (mailSendType){
                case REGISTER:
                    helper.setSubject(SUBJECT_REGISTER);
                    helper.setText(CONTENT_REGISTER.replace("{CODE}", randomCode), true);
                    break;
                case PASSWORD_FIND:
                    helper.setSubject(SUBJECT_PASSWORD_FIND);
                    helper.setText(CONTENT_PASSWORD_FIND.replace("{CODE}", randomCode), true);
                    break;
            }

        }catch (Exception e){
            throw new MailException(CREATE_MAIL_MESSAGE_ERROR);
        }

        return message;
    }

}
