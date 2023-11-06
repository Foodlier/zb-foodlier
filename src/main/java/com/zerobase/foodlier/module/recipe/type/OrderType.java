package com.zerobase.foodlier.module.recipe.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    CREATED_AT("createAt"),
    HEART_COUNT("numberOfHeart"),
    COMMENT_COUNT("numberOfComment"),
    INVALID_TYPE("invalid");

    private final String orderKey;

    public static boolean isInvalidType(OrderType orderType){
        return Arrays.stream(OrderType.values())
                .filter(type -> type.equals(orderType))
                .findFirst()
                .isEmpty();
    }

}
