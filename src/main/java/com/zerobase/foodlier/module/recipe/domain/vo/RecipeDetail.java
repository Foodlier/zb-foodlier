package com.zerobase.foodlier.module.recipe.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetail {

    @Column(nullable = false)
    private String cookingOrder;
    private String cookingOrderImageUrl;
}
