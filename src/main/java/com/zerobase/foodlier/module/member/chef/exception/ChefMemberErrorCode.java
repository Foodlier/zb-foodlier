package com.zerobase.foodlier.module.member.chef.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChefMemberErrorCode {
    CHEF_MEMBER_NOT_FOUND("요리사 정보를 찾을 수 없습니다.");
    private final String description;
}
