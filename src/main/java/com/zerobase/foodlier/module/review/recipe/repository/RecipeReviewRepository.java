package com.zerobase.foodlier.module.review.recipe.repository;

import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeReviewRepository extends JpaRepository<RecipeReview, Long> {
}
