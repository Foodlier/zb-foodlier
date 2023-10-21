package com.zerobase.foodlier.module.member.member.mail.constants;

public final class MailConstants {

    public static final String SUBJECT_REGISTER = "[FOODLIER] 회원가입 인증 메일입니다.";
    public static final String CONTENT_REGISTER =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"ko\" style=\"text-align: center;\">\n" +
                    "<head style=\"text-align: center;\"><meta charset=\"UTF-8\" style=\"text-align: center;\"></head>\n" +
                    "<body style=\"text-align: center;\">\n" +
                    "<div id=\"main-div\" style=\"text-align:center;background-color:#f8eacd;padding:2rem;margin:0 auto 0 auto;border-radius:1rem;box-shadow:2px 2px 4px rgba(0, 0, 0, 0.2);\">\n" +
                    "    <h1 style=\"text-align:center;font-size:4rem;color:#E45141;\">FOODLIER</h1>\n" +
                    "    <h2 style=\"text-align: center;\">FOODLIER 회원가입을 환영합니다.</h2>\n" +
                    "    <h2 style=\"text-align: center;\">회원가입을 위해 다음의 인증코드를 입력해주세요.</h2>\n" +
                    "    <br style=\"text-align: center;\"><br style=\"text-align: center;\"><div id=\"code-box\" style=\"text-align:center;background-color:#ffbe58;padding:1px;margin:0 auto 0 auto;border-radius:1rem;\">\n" +
                    "    <h1 style=\"text-align:center;font-size:2rem;color:#E45141;\">{CODE}</h1>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

    public static final String SUBJECT_PASSWORD_FIND=
            "[FOODLIER] 재설정된 비밀번호입니다.";
    public static final String CONTENT_PASSWORD_FIND=
            "<!DOCTYPE html>\n" +
                    "<html lang=\"ko\" style=\"text-align: center;\">\n" +
                    "<head style=\"text-align: center;\"><meta charset=\"UTF-8\" style=\"text-align: center;\"></head>\n" +
                    "<body style=\"text-align: center;\">\n" +
                    "<div id=\"main-div\" style=\"text-align:center;background-color:#f8eacd;padding:2rem;margin:0 auto 0 auto;border-radius:1rem;box-shadow:2px 2px 4px rgba(0, 0, 0, 0.2);\">\n" +
                    "    <h1 style=\"text-align:center;font-size:4rem;color:#E45141;\">FOODLIER</h1>\n" +
                    "    <h2 style=\"text-align: center;\">임의로 비밀번호가 재설정 되었습니다.</h2>\n" +
                    "    <h2 style=\"text-align: center;\">추후에 반드시 비밀번호를 변경해주세요.</h2>\n" +
                    "    <br style=\"text-align: center;\"><br style=\"text-align: center;\"><div id=\"code-box\" style=\"text-align:center;background-color:#ffbe58;padding:1px;margin:0 auto 0 auto;border-radius:1rem;\">\n" +
                    "    <h1 style=\"text-align:center;font-size:2rem;color:#E45141;\">{CODE}</h1>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
}
