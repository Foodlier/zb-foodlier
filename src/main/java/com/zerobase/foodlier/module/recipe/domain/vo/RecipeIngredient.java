package com.zerobase.foodlier.module.recipe.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    private String name;
    private int count;
    private String unit;
}
