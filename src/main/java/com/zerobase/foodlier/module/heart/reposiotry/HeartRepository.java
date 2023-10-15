package com.zerobase.foodlier.module.heart.reposiotry;

import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long>, HeartRepositoryCustom {
    boolean existsByRecipeAndMember(Recipe recipe, Member member);
}
