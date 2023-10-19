package com.zerobase.foodlier.module.recipe.dto.quotation;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDetailResponse {
    private Long quotationId;

    private String title;

    private String content;

    private int expectedTime;

    private Difficulty difficulty;

    private List<RecipeIngredientDto> recipeIngredientDtoList;

    private List<String> recipeDetailDtoList;

    public static QuotationDetailResponse fromEntity(Recipe recipe) {
        return QuotationDetailResponse.builder()
                .quotationId(recipe.getId())
                .title(recipe.getSummary().getTitle())
                .content(recipe.getSummary().getContent())
                .expectedTime(recipe.getExpectedTime())
                .difficulty(recipe.getDifficulty())
                .recipeDetailDtoList(recipe.getRecipeDetailList().stream()
                        .map(RecipeDetail::getCookingOrder)
                        .collect(Collectors.toList()))
                .recipeIngredientDtoList(recipe.getRecipeIngredientList().stream()
                        .map(RecipeIngredientDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
