package com.zerobase.foodlier.module.recipe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode {
    NO_SUCH_RECIPE("레시피가 없습니다."),
    ;

    private final String description;
}
