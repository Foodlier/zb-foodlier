package com.zerobase.foodlier.module.member.member.dto;

public interface RequestedMemberDto {

    Long getMemberId();
    String getProfileUrl();
    String getNickname();
    double getDistance();
    double getLat();
    double getLnt();
    Long getRequestId();
    Long getExpectedPrice();
    String getTitle();
    String getMainImageUrl();

}
