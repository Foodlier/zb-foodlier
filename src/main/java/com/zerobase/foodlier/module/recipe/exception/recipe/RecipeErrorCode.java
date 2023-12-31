package com.zerobase.foodlier.module.recipe.exception.recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode {
    NO_SUCH_RECIPE("레시피가 없습니다."),
    NO_SUCH_RECIPE_DOCUMENT("레시피 검색 객체를 찾을 수 없습니다."),
    NO_PERMISSION("레시피 접근 권한이 없습니다."),
    NOT_PUBLIC_RECIPE("공개되지 않은 레시피입니다."),
    DELETED_RECIPE("삭제된 레시피입니다."),
    QUOTATION_CANNOT_BE_TAGGED("견적서는 태그할 수 없습니다."),
    ORDER_TYPE_NOT_FOUND("정의되지 않은 정렬타입입니다."),
    SEARCH_TYPE_NOT_FOUND("정의되지 않은 검색 타입입니다."),
    SORT_TYPE_NOT_FOUND("정의되지 않은 레시피 검색 정렬 타입입니다."),
    ;

    private final String description;
}