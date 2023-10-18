package com.zerobase.foodlier.module.recipe.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    CREATED_AT("createdAt"),
    HEART_COUNT("numberOfHeart"),
    COMMENT_COUNT("numberOfComment"),
    INVALID_TYPE("invalid");

    private final String orderKey;
}
