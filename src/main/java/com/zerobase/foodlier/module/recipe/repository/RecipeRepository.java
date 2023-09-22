package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
