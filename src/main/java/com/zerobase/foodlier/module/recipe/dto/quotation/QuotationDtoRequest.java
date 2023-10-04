package com.zerobase.foodlier.module.recipe.dto.quotation;

import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDtoRequest {

    private String title;
    private String content;
    private List<RecipeIngredientDto> recipeIngredientDtoList;
    private Difficulty difficulty;
    private List<String> recipeDetailDtoList;
    private int expectedTime;

}
