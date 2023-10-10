package com.zerobase.foodlier.global.member.password.facade;

import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.mail.service.MailService;
import com.zerobase.foodlier.module.member.member.mail.service.RandomCodeService;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.zerobase.foodlier.module.member.member.type.MailSendType.PASSWORD_FIND;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordFindFacadeTest {
    @Mock
    private MailService mailService;

    @Mock
    private MemberService memberService;

    @Mock
    private RandomCodeService randomCodeService;

    @InjectMocks
    private PasswordFindFacade passwordFindFacade;

    @Test
    @DisplayName("비밀번호 재설정 메일 발송 및 비밀번호 재설정 성공")
    void success_sendMailAndUpdateNewPassword() {
        //given
        PasswordFindForm form = PasswordFindForm.builder()
                .email("test@test.com")
                .phoneNumber("01012345678")
                .build();

        given(randomCodeService.createRandomCode())
                .willReturn("randomCode");

        //when
        passwordFindFacade.sendMailAndUpdateNewPassword(form);

        //then
        verify(randomCodeService, times(1))
                .createRandomCode();
        verify(memberService, times(1))
                .updateRandomPassword(form, "randomCode");
        verify(mailService, times(1))
                .sendMail(form.getEmail(), "randomCode", PASSWORD_FIND);
    }

}