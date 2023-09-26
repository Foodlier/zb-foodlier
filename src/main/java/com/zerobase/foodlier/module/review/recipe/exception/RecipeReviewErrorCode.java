package com.zerobase.foodlier.module.review.recipe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeReviewErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
