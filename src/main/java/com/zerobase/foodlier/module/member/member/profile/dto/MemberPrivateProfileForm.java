package com.zerobase.foodlier.module.member.member.profile.dto;

import com.zerobase.foodlier.common.validator.image.ImageFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPrivateProfileForm {

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자로 입력해주세요")
    private String nickName;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^010[0-9]{8}$", message = "전화번호 형식으로 입력해주세요")
    private String phoneNumber;

    @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
    private String roadAddress;

    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String addressDetail;

    @ImageFile
    private MultipartFile profileImage;
}
