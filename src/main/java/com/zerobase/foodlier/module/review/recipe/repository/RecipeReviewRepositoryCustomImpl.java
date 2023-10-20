package com.zerobase.foodlier.module.review.recipe.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
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
    private final StringPath CREATED_AT_ORDER_BY = Expressions.stringPath("createdAt");

    @Override
    public Page<RecipeReview> findRecipe(Long recipeId, Long memberId, Pageable pageable) {
        QRecipeReview recipeReview = QRecipeReview.recipeReview;
        List<RecipeReview> content = queryFactory.selectFrom(recipeReview)
                .where(recipeReview.recipe.id.eq(recipeId)
                        .and(recipeReview.member.id.ne(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(recipeReview)
                .where(recipeReview.recipe.id.eq(recipeId)
                        .and(recipeReview.member.id.ne(memberId)))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<RecipeReview> findByRecipeReviewForRecipeWriter(Long memberId, Pageable pageable) {
        QRecipeReview recipeReview = QRecipeReview.recipeReview;
        QRecipe recipe = QRecipe.recipe;

        List<RecipeReview> content = queryFactory.select(recipeReview)
                .from(recipeReview)
                .where(recipeReview.recipe.in(
                        JPAExpressions
                                .selectFrom(recipe)
                                .where(recipe.member.id.eq(memberId))
                ))
                .orderBy(CREATED_AT_ORDER_BY.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(Wildcard.count)
                .from(recipeReview)
                .where(recipeReview.recipe.in(
                        JPAExpressions
                                .selectFrom(recipe)
                                .where(recipe.member.id.eq(memberId))
                ))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }
}
