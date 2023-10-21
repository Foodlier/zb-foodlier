package com.zerobase.foodlier.module.recipe.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.heart.domain.model.QHeart;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import com.zerobase.foodlier.module.request.domain.model.QRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recipe> findHeart(Long memberId, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QHeart heart = QHeart.heart;
        List<Recipe> content = queryFactory.selectFrom(recipe)
                .join(heart)
                .on(heart.recipe.eq(recipe)
                        .and(heart.member.id.eq(memberId)))
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(recipe)
                .join(heart)
                .on(heart.recipe.eq(recipe)
                        .and(heart.member.id.eq(memberId)))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<QuotationTopResponse> findQuotationListForRefrigerator(Long memberId, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QMember member = QMember.member;
        QRequest request = QRequest.request;

        List<QuotationTopResponse> content = queryFactory.select(Projections.constructor(QuotationTopResponse.class,
                        recipe.id, recipe.summary.title, recipe.summary.content,
                        recipe.difficulty, recipe.expectedTime))
                .from(recipe)
                .join(member)
                .on(recipe.member.id.eq(member.id)
                        .and(member.id.eq(memberId)))
                .where(recipe.isQuotation.isTrue()
                        .and(recipe.id.notIn(JPAExpressions.select(recipe.id)
                                .from(recipe)
                                .join(request)
                                .on(request.recipe.eq(recipe))
                                .where(recipe.member.id.eq(memberId)))))
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(recipe)
                .join(member)
                .on(recipe.member.id.eq(member.id)
                        .and(member.id.eq(memberId)))
                .where(recipe.isQuotation.isTrue()
                        .and(recipe.id.notIn(JPAExpressions.select(recipe.id)
                                .from(recipe)
                                .join(request)
                                .on(request.recipe.eq(recipe))
                                .where(recipe.member.id.eq(memberId)))))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<QuotationTopResponse> findQuotationListForRecipe(Long memberId, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        List<QuotationTopResponse> content = queryFactory.select(Projections.constructor(QuotationTopResponse.class,
                        recipe.id, recipe.summary.title, recipe.summary.content,
                        recipe.difficulty, recipe.expectedTime))
                .from(recipe)
                .join(request)
                .on(request.recipe.eq(recipe)
                        .and(request.isPaid.isTrue()))
                .where(recipe.member.id.eq(memberId)
                        .and(recipe.isQuotation.isTrue()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(recipe)
                .join(request)
                .on(request.recipe.eq(recipe)
                        .and(request.isPaid.isTrue()))
                .where(recipe.member.id.eq(memberId)
                        .and(recipe.isQuotation.isTrue()))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public boolean existsByIdAndMemberForQuotation(Long memberId, Long quotationId) {
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        return queryFactory.select(new CaseBuilder().when(Wildcard.count.gt(0))
                        .then(true).otherwise(false))
                .from(recipe)
                .leftJoin(request)
                .on(request.recipe.eq(recipe))
                .where(recipe.id.eq(quotationId)
                        .and(recipe.isQuotation.isTrue())
                        .and(request.member.id.eq(memberId)
                                .or(recipe.member.id.eq(memberId))))
                .fetchFirst();
    }

    @Override
    public boolean isNotAbleToDeleteForQuotation(Long memberId, Long quotationId) {
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        return queryFactory.select(new CaseBuilder().when(Wildcard.count.gt(0))
                        .then(true).otherwise(false))
                .from(recipe)
                .join(request)
                .on(request.recipe.eq(recipe))
                .where(recipe.id.eq(quotationId)
                        .and(recipe.isQuotation.isTrue())
                        .and(recipe.member.id.eq(memberId)))
                .fetchFirst();
    }

    @Override
    public boolean isAbleToConvert(Recipe quotation) {
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        return queryFactory.select(new CaseBuilder().when(Wildcard.count.gt(0))
                        .then(true).otherwise(false))
                .from(recipe)
                .join(request)
                .on(request.recipe.eq(recipe)
                        .and(request.isPaid.isTrue()))
                .where(recipe.eq(quotation))
                .fetchFirst();
    }
}
