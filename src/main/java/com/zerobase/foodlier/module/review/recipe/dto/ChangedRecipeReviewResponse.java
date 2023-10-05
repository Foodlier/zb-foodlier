package com.zerobase.foodlier.module.review.recipe.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangedRecipeReviewResponse {

    private Long recipeId;
    private String cookImageUrl;
    private int star;

}
