package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDtoResponse {
    private Long recipeId;

    private Long memberId;

    private String nickname;

    private String profileUrl;

    private String title;

    private String content;

    private String mainImageUrl;

    private int expectedTime;

    private int heartCount;

    private Difficulty difficulty;

    private List<RecipeDetailDto> recipeDetailDtoList;

    private List<RecipeIngredientDto> recipeIngredientDtoList;

    private boolean isHeart;

    public static RecipeDtoResponse fromEntity(Recipe recipe) {
        return RecipeDtoResponse.builder()
                .recipeId(recipe.getId())
                .memberId(recipe.getMember().getId())
                .nickname(recipe.getMember().getNickname())
                .profileUrl(recipe.getMember().getProfileUrl())
                .title(recipe.getSummary().getTitle())
                .content(recipe.getSummary().getContent())
                .mainImageUrl(recipe.getMainImageUrl())
                .expectedTime(recipe.getExpectedTime())
                .heartCount(recipe.getHeartCount())
                .difficulty(recipe.getDifficulty())
                .recipeDetailDtoList(recipe.getRecipeDetailList().stream()
                        .map(RecipeDetailDto::fromEntity)
                        .collect(Collectors.toList()))
                .recipeIngredientDtoList(recipe.getRecipeIngredientList().stream()
                        .map(RecipeIngredientDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public void updateHeart(boolean value) {
        this.isHeart = value;
    }
}
