package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDto {
    @NotBlank(message = "요리 재료를 입력해주세요")
    private String name;
    @Min(value = 1, message = "갯수는 1이상으로 입력해주세요")
    private int count;
    @NotBlank(message = "단위를 입력해주세요.")
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
