package com.zerobase.foodlier.global.profile.dto;

import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import lombok.Getter;

@Getter
public class MemberPrivateProfileForm {
    private String nickName;
    private String phoneNumber;
    private Address address;
    private String profileUrl;
}
