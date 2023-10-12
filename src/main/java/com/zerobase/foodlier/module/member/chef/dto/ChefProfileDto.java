package com.zerobase.foodlier.module.member.chef.dto;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefProfileDto {

    private GradeType grade;
    private long exp;

    public static ChefProfileDto from(ChefMember chefMember){
        return ChefProfileDto
                .builder()
                .grade(chefMember.getGradeType())
                .exp(chefMember.getExp())
                .build();
    }

}
