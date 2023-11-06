package com.zerobase.foodlier.module.recipe.validation;

import com.zerobase.foodlier.module.recipe.type.OrderType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OrderTypeValidator implements ConstraintValidator<ValidOrder, OrderType> {

    @Override
    public void initialize(ValidOrder constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(OrderType value, ConstraintValidatorContext context) {
        return Arrays.stream(OrderType.values())
                .anyMatch(orderType -> orderType==value);
    }
}
