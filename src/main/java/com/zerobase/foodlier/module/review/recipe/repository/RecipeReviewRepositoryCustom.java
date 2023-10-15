package com.zerobase.foodlier.module.review.recipe.repository;

import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewRepositoryCustom {
    Page<RecipeReview> findRecipe(Long recipeId, Long memberId, Pageable pageable);
}
