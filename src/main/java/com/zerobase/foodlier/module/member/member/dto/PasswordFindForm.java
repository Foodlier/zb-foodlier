package com.zerobase.foodlier.module.member.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordFindForm {
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력값 입니다.")
    @Pattern(regexp = "^010[0-9]{8}$", message = "전화번호 형식으로 입력해주세요")
    private String phoneNumber;
}
