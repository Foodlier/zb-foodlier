package com.zerobase.foodlier.module.requestform.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFormDto {
    private Long recipeId;
    private String title;
    private String content;
    private Long expectedPrice;
    private LocalDateTime expectedAt;
    private List<String> ingredientList;
}
