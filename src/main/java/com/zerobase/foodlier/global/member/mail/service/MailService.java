package com.zerobase.foodlier.global.member.mail.service;

public interface MailService {
    void sendMail(String email, String verificationCode);
}
