package com.zerobase.foodlier.module.review.chef.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChefReviewErrorCode {

    NEW_ENUM("enum 추가");

    private final String description;
}
