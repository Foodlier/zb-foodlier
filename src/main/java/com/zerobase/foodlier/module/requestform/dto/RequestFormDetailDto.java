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
    private Long requestId;
    private String title;
    private String content;
    private List<String> ingredientList;
    private Long expectedPrice;
    private LocalDateTime expectedAt;
    private Recipe recipe;
}
