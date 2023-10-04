package com.zerobase.foodlier.module.member.member.dto;

import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
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

    public static MemberRegisterDto from(MemberInputDto memberInputDto,
                                         CoordinateResponseDto coordinateResponseDto,
                                         String profileUrl, RegistrationType registrationType) {
        return MemberRegisterDto.builder()
                .nickname(memberInputDto.getNickname())
                .profileUrl(profileUrl)
                .email(memberInputDto.getEmail())
                .password(memberInputDto.getPassword())
                .phoneNumber(memberInputDto.getPhoneNumber())
                .roadAddress(memberInputDto.getRoadAddress())
                .addressDetail(memberInputDto.getAddressDetail())
                .lat(coordinateResponseDto.getLat())
                .lnt(coordinateResponseDto.getLnt())
                .registrationType(registrationType)
                .build();
    }
}
