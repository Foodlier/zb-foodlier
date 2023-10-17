package com.zerobase.foodlier.module.member.member.mail.service;

import org.junit.jupiter.api.Test;

import static com.zerobase.foodlier.module.member.member.mail.constants.GenerateCodeConstants.CODE_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class RandomCodeServiceImplTest {

    @Test
    void success_createRandomCode(){
        String verificationCode = new RandomCodeServiceImpl().createRandomCode();

        assertAll(
                () -> assertEquals(CODE_LENGTH, verificationCode.length()),
                () -> assertTrue(verificationCode.matches(".*[a-zA-Z0-9].*"))
        );
    }

    @Test
    void success_createIntegerRandomCode(){
        String verificationCode = new RandomCodeServiceImpl().createIntegerRandomCode();

        assertAll(
                () -> assertEquals(6, verificationCode.length()),
                () -> assertTrue(verificationCode.matches(".*[0-9].*"))
        );
    }

}