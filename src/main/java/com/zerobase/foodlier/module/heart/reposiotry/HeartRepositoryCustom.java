package com.zerobase.foodlier.module.heart.reposiotry;

import com.zerobase.foodlier.module.heart.domain.model.Heart;

import java.util.Optional;

public interface HeartRepositoryCustom {
    Optional<Heart> findHeart(Long recipeId,Long memberId);
}
