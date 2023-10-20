package com.zerobase.foodlier.module.heart.reposiotry;

import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;

import java.util.Optional;

public interface HeartRepositoryCustom {
    Optional<Heart> findHeart(Long recipeId,Long memberId);
    boolean existsHeart(Long recipeId, Member member);
}
