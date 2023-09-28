package com.zerobase.foodlier.module.recipe.dto;

import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
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

    public RecipeIngredient toEntity() {
        return RecipeIngredient.builder()
                .name(name)
                .unit(unit)
                .count(count)
                .build();
    }

    public static RecipeIngredientDto fromEntity(RecipeIngredient recipeIngredient) {
        return RecipeIngredientDto.builder()
                .name(recipeIngredient.getName())
                .count(recipeIngredient.getCount())
                .unit(recipeIngredient.getUnit())
                .build();
    }
}
