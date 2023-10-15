package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    Page<Recipe> findByMemberAndIsPublicTrueAndIsQuotationFalse(Member member, Pageable pageable);

    int countByMember(Member member);

    Optional<Recipe> findByIdAndMemberAndIsQuotationTrue(Long recipeId, Member member);

    List<Recipe> findTop3ByIsPublicOrderByCreatedAtDesc(Boolean isPublic);

    Page<Recipe> findByIsPublicOrderByCreatedAtDesc(Boolean isPublic,
                                                    Pageable pageable);

    Page<Recipe> findByIsPublicOrderByHeartCountDesc(Boolean isPublic,
                                                     Pageable pageable);

    Page<Recipe> findByIsPublicOrderByCommentCountDesc(Boolean isPublic,
                                                       Pageable pageable);

    List<Recipe> findTop5ByIsPublicAndCreatedAtAfterOrderByHeartCountDesc(
            Boolean isPublic, LocalDateTime createdAt);
}
