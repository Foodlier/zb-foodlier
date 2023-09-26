package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoResponse;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;

import java.util.Optional;

public interface RecipeService {
    void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest);

    void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id);

    Optional<Recipe> getRecipe(Long id);

    RecipeDtoResponse getRecipeDetail(Long id);

    void deleteRecipe(Long id);

}
