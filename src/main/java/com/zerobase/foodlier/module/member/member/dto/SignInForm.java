package com.zerobase.foodlier.module.member.member.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInForm {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime currentDate;

    public Date getCurrentDate(){
        return java.sql.Timestamp.valueOf(this.currentDate);
    }
}
