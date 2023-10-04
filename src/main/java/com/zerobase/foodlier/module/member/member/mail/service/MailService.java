package com.zerobase.foodlier.module.member.member.mail.service;

public interface MailService {
    void sendMail(String email, String verificationCode);
}
