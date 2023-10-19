package com.zerobase.foodlier.module.review.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.review.recipe.domain.model.QRecipeReview;
import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeReviewRepositoryCustomImpl implements RecipeReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecipeReview> findRecipe(Long recipeId, Long memberId, Pageable pageable) {
        QRecipeReview recipeReview = QRecipeReview.recipeReview;
        List<RecipeReview> content = queryFactory.selectFrom(recipeReview)
                .where(recipeReview.recipe.id.eq(recipeId)
                        .and(recipeReview.member.id.ne(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content);
    }
}
