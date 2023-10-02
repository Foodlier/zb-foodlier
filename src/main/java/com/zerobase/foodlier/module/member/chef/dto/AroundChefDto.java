package com.zerobase.foodlier.module.member.chef.dto;

public interface AroundChefDto {

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
    int getRecipeCount();

}
