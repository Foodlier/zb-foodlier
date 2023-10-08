package com.zerobase.foodlier.module.member.member.mail.service;

import com.zerobase.foodlier.module.member.member.mail.constants.MailConstants;
import com.zerobase.foodlier.module.member.member.type.MailSendType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static com.zerobase.foodlier.module.member.member.mail.constants.MailConstants.CONTENT_REGISTER;
import static com.zerobase.foodlier.module.member.member.type.MailSendType.REGISTER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void success_send_mail() {
        //when
        String email = "test178295031875@test.com";
        String verificationCode = "LzOXCMpEUo";
        mailService.sendMail(email, verificationCode, REGISTER);

        //then
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor
                .forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertAll(
                () -> assertEquals(email, Objects.requireNonNull(message.getTo())[0]),
                () -> assertEquals(MailConstants.SUBJECT_REGISTER, message.getSubject()),
                () -> assertEquals(CONTENT_REGISTER + "\n" + verificationCode, message.getText())
        );
    }

}