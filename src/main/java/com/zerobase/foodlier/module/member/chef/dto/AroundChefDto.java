package com.zerobase.foodlier.module.member.chef.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AroundChefDto {

    //chefMember
    Long chefId;
    String introduce;
    Double starAvg;
    Integer reviewCount;

    //member
    String profileUrl;
    String nickname;
    Double lat;
    Double lnt;

    //etc
    Double distance;
    Long recipeCount;

}
