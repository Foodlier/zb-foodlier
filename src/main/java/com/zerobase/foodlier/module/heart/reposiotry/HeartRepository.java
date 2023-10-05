package com.zerobase.foodlier.module.heart.reposiotry;

import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query(
            "SELECT h FROM Heart h WHERE h.recipe.id = :recipeId AND h.member.id = :memberId"
    )
    Optional<Heart> findByRecipeIdAndMemberId(
            @Param("recipeId") Long recipeId, @Param("memberId") Long memberId);

    boolean existsByRecipeAndMember(Recipe recipe, Member member);
}
