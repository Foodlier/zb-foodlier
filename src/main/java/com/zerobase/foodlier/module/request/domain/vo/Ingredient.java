package com.zerobase.foodlier.module.request.domain.vo;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Ingredient {
    @Column(nullable = false)
    private String ingredientName;
}
