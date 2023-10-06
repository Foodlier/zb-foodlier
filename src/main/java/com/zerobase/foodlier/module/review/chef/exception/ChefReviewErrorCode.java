package com.zerobase.foodlier.module.review.chef.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChefReviewErrorCode {

    CHEF_REVIEW_NOT_FOUND("요리사 후기를 찾을 수 없습니다."),
    ALREADY_REVIEWED_TO_CHEF("이미 해당 요청에대한 요리사 후기를 작성하였습니다."),
    YOU_ARE_NOT_REQUESTER("해당 요청의 요청자가 아닙니다."),
    IS_NOT_CLOSED_REQUEST("거래가 성사된 요청이 아닙니다."),
    NEW_ENUM("enum 추가");

    private final String description;
}
