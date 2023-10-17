package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailDto {

    @NotBlank(message = "조리 순서에 대한 이미지 경로를 입력해주세요.")
    @URL(message = "조리 순서 이미지가 웹 경로 형식이 아닙니다.")
    private String cookingOrderImageUrl;
    @NotBlank(message = "조리 순서를 입력해주세요.")
    private String cookingOrder;

    public RecipeDetail toEntity() {
        return RecipeDetail.builder()
                .cookingOrderImageUrl(cookingOrderImageUrl)
                .cookingOrder(cookingOrder)
                .build();
    }

    public static RecipeDetailDto fromEntity(RecipeDetail recipeDetail) {
        return RecipeDetailDto.builder()
                .cookingOrderImageUrl(recipeDetail.getCookingOrderImageUrl())
                .cookingOrder(recipeDetail.getCookingOrder())
                .build();
    }
}
