package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDtoRequest {
    private String title;
    private String content;
    private String mainImageUrl;
    private List<RecipeIngredientDto> recipeIngredientDtoList;
    private Difficulty difficulty;
    private List<RecipeDetailDto> recipeDetailDtoList;
    private int expectedTime;

    public Recipe toEntity(Member member) {
        return Recipe.builder()
                .summary(Summary.builder()
                        .title(title)
                        .content(content)
                        .build())
                .mainImageUrl(mainImageUrl)
                .expectedTime(expectedTime)
                .recipeStatistics(new RecipeStatistics())
                .difficulty(difficulty)
                .isPublic(true)
                .member(member)
                .recipeIngredientList(recipeIngredientDtoList
                        .stream()
                        .map(RecipeIngredientDto::toEntity)
                        .collect(Collectors.toList()))
                .recipeDetailList(recipeDetailDtoList
                        .stream()
                        .map(RecipeDetailDto::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

}
