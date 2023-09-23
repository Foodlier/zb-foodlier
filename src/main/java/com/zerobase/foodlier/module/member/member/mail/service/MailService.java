package com.zerobase.foodlier.module.member.member.mail.service;

public interface MailService {
    String sendMailAndGetVerificationCode(String email, String verificationCode);
}
