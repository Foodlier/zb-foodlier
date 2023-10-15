package com.zerobase.foodlier.module.member.member.mail.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.zerobase.foodlier.module.member.member.mail.constants.GenerateCodeConstants.*;

@Service
public class RandomCodeServiceImpl implements RandomCodeService {
    @Override
    public String createRandomCode() {
        return RandomStringUtils.random(CODE_LENGTH, USE_LETTER, USE_NUMBER);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-13
     *  정수 6자리로 된, 랜덤 코드를 발급함.
     */
    @Override
    public String createIntegerRandomCode(){
        return String.format(INTEGER_CODE_FORMAT, new Random().nextInt(INTEGER_CODE_RANGE));
    }
}
