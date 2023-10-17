package com.zerobase.foodlier.module.recipe.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum SortType {

    HEART("numberOfHeart"),
    COMMENT("numberOfComment"),
    CREATE_AT("createAt");

    private final String documentKey;

    public static boolean existsSortType(SortType sortType){
        return Arrays.stream(SortType.values())
                .anyMatch(type -> type==sortType);

    }

}
