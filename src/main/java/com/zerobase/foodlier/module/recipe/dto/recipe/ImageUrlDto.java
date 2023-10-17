package com.zerobase.foodlier.module.recipe.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUrlDto {
    private String mainImageUrl;
    private List<String> cookingOrderImageUrlList;

}
