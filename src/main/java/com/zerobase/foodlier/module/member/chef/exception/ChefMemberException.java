package com.zerobase.foodlier.module.member.chef.exception;

import lombok.Getter;

@Getter
public class ChefMemberException {

    private final ChefMemberErrorCode chefMemberErrorCode;
    private final String description;

    public ChefMemberException(ChefMemberErrorCode chefMemberErrorCode){
        this.chefMemberErrorCode = chefMemberErrorCode;
        this.description= chefMemberErrorCode.getDescription();
    }
}
