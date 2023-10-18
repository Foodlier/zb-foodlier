package com.zerobase.foodlier.module.member.member.profile.dto;

import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateDto {
    private String nickName;
    private String profileUrl;
    private String phoneNumber;
    private String roadAddress;
    private String addressDetail;
    private double lat;
    private double lnt;

    public static MemberUpdateDto from(MemberPrivateProfileForm form,
                       CoordinateResponseDto coordinateResponseDto,
                       String profileUrl){
        return MemberUpdateDto.builder()
                .nickName(form.getNickName())
                .profileUrl(profileUrl)
                .phoneNumber(form.getPhoneNumber())
                .roadAddress(form.getRoadAddress())
                .addressDetail(form.getAddressDetail())
                .lat(coordinateResponseDto.getLat())
                .lnt(coordinateResponseDto.getLnt())
                .build();
    }
}
