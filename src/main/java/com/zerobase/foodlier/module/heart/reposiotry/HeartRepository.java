package com.zerobase.foodlier.module.heart.reposiotry;

import com.zerobase.foodlier.module.heart.domain.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByRecipeId(Long recipeId);
}
