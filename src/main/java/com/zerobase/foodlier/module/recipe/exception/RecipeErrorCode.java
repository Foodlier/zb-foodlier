package com.zerobase.foodlier.module.recipe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode {
    NO_SUCH_RECIPE("레시피가 없습니다."),
    NO_SUCH_RECIPE_DOCUMENT("레시피 검색 객체를 찾을 수 없습니다."),
    NO_PERMISSION("레시피 접근 권한이 없습니다."),
    ;

    private final String description;
}