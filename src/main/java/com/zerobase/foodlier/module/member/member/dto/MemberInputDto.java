package com.zerobase.foodlier.module.member.member.dto;

import com.zerobase.foodlier.common.validator.image.ImageFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInputDto {

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 2, max = 8, message = "닉네임은 2~8자로 입력해주세요")
    private String nickname;

    @ImageFile
    private MultipartFile profileImage;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^010[0-9]{8}$", message = "전화번호 형식으로 입력해주세요")
    private String phoneNumber;

    @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
    private String roadAddress;
    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String addressDetail;
}
