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
    private Long chefId;
    private String introduce;
    private Double starAvg;
    private Integer reviewCount;

    //member
    private String profileUrl;
    private String nickname;
    private Double lat;
    private Double lnt;

    //etc
    private Double distance;
    private Long recipeCount;

}
