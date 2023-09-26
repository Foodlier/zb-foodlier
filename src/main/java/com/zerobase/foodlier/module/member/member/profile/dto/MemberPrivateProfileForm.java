package com.zerobase.foodlier.module.member.member.profile.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPrivateProfileForm {
    private String nickName;
    private String phoneNumber;
    private String roadAddress;
    private String addressDetail;
    private MultipartFile profileImage;
}
