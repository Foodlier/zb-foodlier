package com.zerobase.foodlier.module.member.member.dto;

import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterDto {

    private String nickname;
    private String profileUrl;
    private String email;
    private String password;
    private String phoneNumber;
    private String roadAddress;
    private String addressDetail;
    private double lat;
    private double lnt;
    private RegistrationType registrationType;

}
