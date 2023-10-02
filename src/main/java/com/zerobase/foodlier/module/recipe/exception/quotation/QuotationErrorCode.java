package com.zerobase.foodlier.module.recipe.exception.quotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuotationErrorCode {

    QUOTATION_NOT_FOUND("견적서를 찾을 수 없습니다."),
    HAS_NOT_QUOTATION_READ_PERMISSION("해당 견적서에 대한 읽기 권한이 없습니다."),
    CANNOT_DELETE_IS_LOCKED("삭제할 수 없는 견적서 입니다.");

    private final String description;

}
