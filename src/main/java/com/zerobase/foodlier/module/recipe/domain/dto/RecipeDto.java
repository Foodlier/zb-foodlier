package com.zerobase.foodlier.module.recipe.domain.dto;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private String title;
    private String content;
    private MultipartFile image;
    private List<RecipeIngredientDto> recipeIngredientList;
    private Difficulty difficulty;
    private List<RecipeDetailDto> recipeDetailList;
    private int expectedTime;

    public Recipe toEntity() {
        return Recipe.builder()
                .summary(Summary.builder()
                        .title(title)
                        .content(content)
                        .build())
                .expectedTime(expectedTime)
                .difficulty(difficulty)
                .build();
    }
}
