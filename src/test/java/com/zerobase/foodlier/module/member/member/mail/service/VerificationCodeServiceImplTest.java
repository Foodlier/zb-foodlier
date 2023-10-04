package com.zerobase.foodlier.module.member.member.mail.service;

import org.junit.jupiter.api.Test;

import static com.zerobase.foodlier.module.member.member.mail.constants.GenerateCodeConstants.CODE_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class VerificationCodeServiceImplTest {

    @Test
    void success_createAuthenticationCode(){
        String verificationCode = new VerificationCodeServiceImpl().createAuthenticationCode();

        assertAll(
                () -> assertEquals(CODE_LENGTH, verificationCode.length()),
                () -> assertTrue(verificationCode.matches(".*[a-zA-Z0-9].*"))
        );
    }

}