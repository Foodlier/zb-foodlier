package com.zerobase.foodlier.module.recipe.exception.quotation;

import com.zerobase.foodlier.common.exception.exception.BaseException;
import lombok.Getter;

@Getter
public class QuotationException extends BaseException {
    private final QuotationErrorCode errorCode;
    private final String description;

    public QuotationException(QuotationErrorCode quotationErrorCode){
        this.errorCode = quotationErrorCode;
        this.description = quotationErrorCode.getDescription();
    }
}
