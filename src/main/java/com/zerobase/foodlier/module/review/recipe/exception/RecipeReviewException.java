package com.zerobase.foodlier.module.review.recipe.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RecipeReviewException extends BaseException {
    private final RecipeReviewErrorCode errorCode;
    private final String description;

    public RecipeReviewException(RecipeReviewErrorCode recipeReviewErrorCode) {
        this.errorCode = recipeReviewErrorCode;
        this.description = recipeReviewErrorCode.getDescription();
    }
}
