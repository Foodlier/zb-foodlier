package com.zerobase.foodlier.module.member.member.mail.constants;

public final class MailConstants {

    public static final String SUBJECT_REGISTER = "[FOODLIER] 회원가입 인증 메일입니다.";
    public static final String CONTENT_REGISTER = "FOODLIER 회원가입을 환영합니다!\n" +
            "회원가입을 위해서 다음의 인증코드를 복사하여, 가입창에 붙여넣어주세요.\n";

    public static final String SUBJECT_PASSWORD_FIND=
            "[FOODLIER] 재설정된 비밀번호입니다.";
    public static final String CONTENT_PASSWORD_FIND=
            "임의로 생성된 비밀번호입니다. 비밀번호를 추후에 변경해 주세요.\n\n" +
                    "비밀번호 : ";
}
