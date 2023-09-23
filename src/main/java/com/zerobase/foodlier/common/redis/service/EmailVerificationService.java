package com.zerobase.foodlier.common.redis.service;

import java.time.LocalDateTime;

public interface EmailVerificationService {
    void createVerification(String email, String verificationCode,
                            LocalDateTime nowTime);
    void verify(String email, String verificationCode, LocalDateTime nowTime);
    void isAuthorized(String email);
}
