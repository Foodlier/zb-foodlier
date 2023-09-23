package com.zerobase.foodlier.module.recipe.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDto {
    private String name;
    private int count;
    private String unit;
}
