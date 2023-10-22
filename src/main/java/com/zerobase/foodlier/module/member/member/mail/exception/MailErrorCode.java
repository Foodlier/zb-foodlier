package com.zerobase.foodlier.module.member.member.mail.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode {
    CREATE_MAIL_MESSAGE_ERROR("메일 메세지를 생성하는 중에 오류가 발생하였습니다.");
    private final String description;
}
