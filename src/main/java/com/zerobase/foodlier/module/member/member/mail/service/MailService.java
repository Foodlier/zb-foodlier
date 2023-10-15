package com.zerobase.foodlier.module.member.member.mail.service;

import com.zerobase.foodlier.module.member.member.type.MailSendType;

public interface MailService {
    void sendMail(String email, String randomCode, MailSendType mailSendType);
}
