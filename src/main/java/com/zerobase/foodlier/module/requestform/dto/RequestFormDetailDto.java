package com.zerobase.foodlier.module.requestform.dto;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFormDetailDto {
    private Long requestFormId;
    private String title;
    private String content;
    private List<String> ingredientList;
    private Long expectedPrice;
    private LocalDateTime expectedAt;
    private String mainImageUrl;
    private String recipeTitle;
    private String recipeContent;
    private int heartCount;
}
