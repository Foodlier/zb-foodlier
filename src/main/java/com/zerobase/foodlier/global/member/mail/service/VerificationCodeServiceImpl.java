package com.zerobase.foodlier.global.member.mail.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.global.member.mail.constants.GenerateCodeConstants.*;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{
    @Override
    public String createAuthenticationCode() {
        return RandomStringUtils.random(CODE_LENGTH, USE_LETTER, USE_NUMBER);
    }
}
