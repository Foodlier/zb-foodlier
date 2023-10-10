package com.zerobase.foodlier.module.member.member.mail.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.member.member.mail.constants.GenerateCodeConstants.*;

@Service
public class RandomCodeServiceImpl implements RandomCodeService {
    @Override
    public String createRandomCode() {
        return RandomStringUtils.random(CODE_LENGTH, USE_LETTER, USE_NUMBER);
    }
}