package com.zerobase.foodlier.module.recipe.domain.dto;

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
