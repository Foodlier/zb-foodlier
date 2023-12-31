package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDtoTopResponse {

    private Long recipeId;
    private String mainImageUrl;
    private String title;
    private String content;
    private int heartCount;
    private Boolean isHeart;

    public static RecipeDtoTopResponse from(Recipe recipe, boolean isHeart){
        return RecipeDtoTopResponse
                .builder()
                .recipeId(recipe.getId())
                .mainImageUrl(recipe.getMainImageUrl())
                .title(recipe.getSummary().getTitle())
                .content(recipe.getSummary().getContent())
                .heartCount(recipe.getHeartCount())
                .isHeart(isHeart)
                .build();
    }
}
