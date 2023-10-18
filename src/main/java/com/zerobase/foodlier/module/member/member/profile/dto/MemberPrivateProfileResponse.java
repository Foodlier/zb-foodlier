package com.zerobase.foodlier.module.member.member.profile.dto;

import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPrivateProfileResponse {
    private Long myMemberId;
    private String nickName;
    private String email;
    private String phoneNumber;
    private long point;
    private Boolean isChef;
    private Address address;
    private String profileUrl;
}
