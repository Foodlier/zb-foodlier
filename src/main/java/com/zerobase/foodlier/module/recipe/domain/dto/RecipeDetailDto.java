package com.zerobase.foodlier.module.recipe.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailDto {
    private MultipartFile cookingOrderImage;
    private String cookingOrder;
}
