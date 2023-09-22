package com.zerobase.foodlier.module.review.chef.exception;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class ChefReviewException extends BaseException {
    private final ChefReviewErrorCode errorCode;
    private final String description;

    public ChefReviewException(ChefReviewErrorCode chefReviewErrorCode) {
        this.errorCode = chefReviewErrorCode;
        this.description = chefReviewErrorCode.getDescription();
    }
}
