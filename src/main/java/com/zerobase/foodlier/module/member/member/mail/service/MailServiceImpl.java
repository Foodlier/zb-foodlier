package com.zerobase.foodlier.module.member.member.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.member.member.mail.constants.MailConstants.CONTENT;
import static com.zerobase.foodlier.module.member.member.mail.constants.MailConstants.SUBJECT;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;

    @Override
    public String sendMailAndGetVerificationCode(String email,
                                                 String verificationCode) {
        SimpleMailMessage message = createMailMessage(email, verificationCode);
        mailSender.send(message);

        return verificationCode;
    }



    private SimpleMailMessage createMailMessage(String email,
                                                String verificationCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject(SUBJECT);
        message.setText(CONTENT + "\n" + verificationCode);
        return message;
    }

}
