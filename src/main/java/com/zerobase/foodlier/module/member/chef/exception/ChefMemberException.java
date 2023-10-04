package com.zerobase.foodlier.module.member.chef.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class ChefMemberException extends BaseException {

    private final ChefMemberErrorCode errorCode;
    private final String description;

    public ChefMemberException(ChefMemberErrorCode chefMemberErrorCode){
        this.errorCode = chefMemberErrorCode;
        this.description= chefMemberErrorCode.getDescription();
    }
}
