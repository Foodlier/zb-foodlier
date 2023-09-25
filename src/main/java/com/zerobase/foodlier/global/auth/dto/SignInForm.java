package com.zerobase.foodlier.global.auth.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class SignInForm {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;
}
