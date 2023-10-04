package com.zerobase.foodlier.module.member.chef.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChefMemberErrorCode {
    CHEF_MEMBER_NOT_FOUND("요리사 정보를 찾을 수 없습니다."),
    ALREADY_REGISTER_CHEF("이미 등록된 요리사입니다."),
    CANNOT_REGISTER_LESS_THAN_THREE_RECIPE("3개 이상의 꿀조합을 작성해야 요리사 등록이 가능합니다.");
    private final String description;
}
