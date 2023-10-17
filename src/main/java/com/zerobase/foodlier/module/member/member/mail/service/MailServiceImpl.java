package com.zerobase.foodlier.module.member.member.mail.service;

import com.zerobase.foodlier.module.member.member.type.MailSendType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.member.member.mail.constants.MailConstants.*;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String email, String randomCode, MailSendType mailSendType) {
        SimpleMailMessage message = createMailMessage(email, randomCode, mailSendType);
        mailSender.send(message);
    }


    private SimpleMailMessage createMailMessage(String email,
                                                String randomCode,
                                                MailSendType mailSendType) {
        SimpleMailMessage message = new SimpleMailMessage();

        switch (mailSendType) {
            case REGISTER:
                message.setFrom(fromEmail);
                message.setTo(email);
                message.setSubject(SUBJECT_REGISTER);
                message.setText(CONTENT_REGISTER + "\n" + randomCode);
                break;
            case PASSWORD_FIND:
                message.setFrom(fromEmail);
                message.setTo(email);
                message.setSubject(SUBJECT_PASSWORD_FIND);
                message.setText(CONTENT_PASSWORD_FIND + randomCode);
                break;
        }

        return message;
    }

}
