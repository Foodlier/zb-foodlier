package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.dto.ImageUrlDto;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoResponse;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;

public interface RecipeService {
    void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest);

    void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id);

    Recipe getRecipe(Long id);

    RecipeDtoResponse getRecipeDetail(Long id);

    ImageUrlDto deleteRecipe(Long id);

}
