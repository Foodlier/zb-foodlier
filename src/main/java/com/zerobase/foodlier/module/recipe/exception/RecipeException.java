package com.zerobase.foodlier.module.recipe.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class RecipeException extends BaseException {

    private final RecipeErrorCode errorCode;
    private final String description;

    public RecipeException(RecipeErrorCode recipeExceptionCode) {
        this.errorCode = recipeExceptionCode;
        this.description = recipeExceptionCode.getDescription();
    }

}
