package com.zerobase.foodlier.module.recipe.domain.dto;

import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailDto {
    private String cookingOrderImageUrl;
    private String cookingOrder;

    public RecipeDetail toEntity() {
        return RecipeDetail.builder()
                .cookingOrderImageUrl(cookingOrderImageUrl)
                .cookingOrder(cookingOrder)
                .build();
    }

    public static RecipeDetailDto fromEntity(RecipeDetail recipeDetail) {
        return RecipeDetailDto.builder()
                .cookingOrderImageUrl(recipeDetail.getCookingOrderImageUrl())
                .cookingOrder(recipeDetail.getCookingOrder())
                .build();
    }
}
