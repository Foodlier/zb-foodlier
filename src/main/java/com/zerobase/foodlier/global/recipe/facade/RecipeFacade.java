package com.zerobase.foodlier.global.recipe.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDto;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeFacade {
    RecipeService recipeService;
    S3Service s3Service;

    public void createRecipe(String email, RecipeDto recipeDto) {
        Recipe recipe = recipeDto.toEntity();

    }
}
