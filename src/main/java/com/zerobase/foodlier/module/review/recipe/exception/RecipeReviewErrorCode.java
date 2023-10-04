package com.zerobase.foodlier.module.review.recipe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeReviewErrorCode {

    RECIPE_REVIEW_NOT_FOUND("꿀조합 후기를 찾을 수 없습니다."),
    ALREADY_WRITTEN_RECIPE_REVIEW("이미 후기를 작성하였습니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
