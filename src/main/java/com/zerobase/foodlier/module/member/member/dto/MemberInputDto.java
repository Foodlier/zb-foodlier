package com.zerobase.foodlier.module.member.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInputDto {

    private String nickname;
    private MultipartFile profileImage;
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
    private String password;
    @Pattern(regexp = "^010[0-9]{8}$", message = "전화번호 형식으로 입력해주세요")
    private String phoneNumber;
    private String roadAddress;
    private String addressDetail;
}
