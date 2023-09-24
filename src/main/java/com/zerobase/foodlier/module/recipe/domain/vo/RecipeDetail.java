package com.zerobase.foodlier.module.recipe.domain.vo;

import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.List;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDetail {

    @Column(nullable = false)
    private String cookingOrder;
    private String cookingOrderImageUrl;

    public static List<RecipeDetail> getRecipeDetailList(
            List<RecipeDetailDto> recipeDetailDtoList) {
        RecipeDetail.builder()
                .cookingOrder()
                .cookingOrderImageUrl
                .build();
    }
}
