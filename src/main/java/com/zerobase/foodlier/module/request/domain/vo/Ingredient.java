package com.zerobase.foodlier.module.request.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
public class Ingredient {
    @Column(nullable = false)
    private String ingredientName;
}
