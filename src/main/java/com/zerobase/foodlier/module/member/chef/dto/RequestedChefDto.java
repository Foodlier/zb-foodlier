package com.zerobase.foodlier.module.member.chef.dto;

public interface RequestedChefDto {

    //chefMember
    Long getChefId();
    String getIntroduce();
    double getStarAvg();
    int getReviewCount();

    //member
    String getProfileUrl();
    String getNickname();

    //etc
    double getDistance();

    //request
    Long getRequestId();
    int getIsQuotation();
    Long getQuotationId();

}
