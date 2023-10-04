package com.zerobase.foodlier.module.recipe.dto.recipe;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeImageResponse {

    private String mainImage;
    private List<String> cookingOrderImageList;

}
