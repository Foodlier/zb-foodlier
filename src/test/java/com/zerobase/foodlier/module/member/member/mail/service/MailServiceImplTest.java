package com.zerobase.foodlier.module.member.member.mail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static com.zerobase.foodlier.module.member.member.type.MailSendType.PASSWORD_FIND;
import static com.zerobase.foodlier.module.member.member.type.MailSendType.REGISTER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    @DisplayName("가입 메일 전송 테스트")
    void success_send_mail_register() {
        //given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        //when
        String email = "test178295031875@test.com";
        String randomCode = "010254";
        mailService.sendMail(email, randomCode, REGISTER);

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 메일 전송 테스트")
    void success_send_mail_passwordFind() {
        //given
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        //when
        String email = "test178295031875@test.com";
        String randomCode = "YpFgxWT5FVXiLiv71aNsURBhs9Zpqy";
        mailService.sendMail(email, randomCode, PASSWORD_FIND);

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }

}