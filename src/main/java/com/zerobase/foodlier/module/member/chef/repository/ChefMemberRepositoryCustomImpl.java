package com.zerobase.foodlier.module.member.chef.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.member.chef.domain.model.QChefMember;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.chef.type.ChefSearchType;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
import com.zerobase.foodlier.module.request.domain.model.QRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChefMemberRepositoryCustomImpl implements ChefMemberRepositoryCustom {
    private static final OrderSpecifier<?> INITIAL_ORDER_BY = null;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AroundChefDto> findAroundChefOrderByType(Long memberId, double lat, double lnt, double distance, Pageable pageable, ChefSearchType type) {
        QMember member = QMember.member;
        QChefMember chefMember = QChefMember.chefMember;
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        OrderSpecifier<?> orderBy = INITIAL_ORDER_BY;
        switch (type) {
            case STAR:
                orderBy = Expressions.stringPath("star").desc();
                break;
            case REVIEW:
                orderBy = Expressions.stringPath("review").desc();
                break;
            case RECIPE:
                orderBy = Expressions.stringPath("recipeCount").desc();
                break;
            case DISTANCE:
                orderBy = Expressions.stringPath("distance").asc();
                break;
        }

        List<AroundChefDto> content = queryFactory.select(Projections.constructor(AroundChefDto.class,
                        chefMember.id, chefMember.introduce, chefMember.starAvg.as("star"),
                        chefMember.reviewCount.as("review"), member.profileUrl, member.nickname,
                        member.address.lat, member.address.lnt,
                        Expressions.numberTemplate(Double.class,
                                "ROUND(ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))/1000, 2)",
                                member.address.lnt, member.address.lat, lnt, lat).as("distance")
                        , ExpressionUtils.as(JPAExpressions.select(recipe.count())
                                .from(recipe)
                                .where(recipe.member.id.eq(chefMember.member.id)
                                        .and(recipe.isQuotation.isFalse())
                                        .and(recipe.isDeleted.isFalse())
                                        .and(recipe.isPublic.isTrue())), "recipeCount")))
                .from(chefMember)
                .join(member)
                .on(chefMember.member.id.eq(member.id))
                .where(member.id.ne(memberId)
                        .and(member.isDeleted.eq(false))
                        .and(Expressions.booleanTemplate("st_contains(st_buffer(point({0}, {1}), {2}), point({3}, {4}))",
                                lat, lnt, distance, member.address.lat, member.address.lnt).isTrue())
                        .and(chefMember.id.notIn(JPAExpressions.select(chefMember.id)
                                .from(member)
                                .join(request)
                                .on(request.member.id.eq(member.id).and(request.isFinished.isFalse()))
                                .join(chefMember)
                                .on(request.chefMember.id.eq(chefMember.id))
                                .where(member.id.eq(memberId)))))
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(chefMember)
                .join(chefMember.member, member)
                .where(member.id.ne(memberId)
                        .and(member.isDeleted.eq(false))
                        .and(Expressions.booleanTemplate("st_contains(st_buffer(point({0}, {1}), {2}), point({3}, {4}))",
                                lat, lnt, distance, member.address.lat, member.address.lnt).isTrue())
                        .and(chefMember.id.notIn(JPAExpressions.select(chefMember.id)
                                .from(member)
                                .join(request)
                                .on(request.member.id.eq(member.id).and(request.isFinished.isFalse()))
                                .join(chefMember)
                                .on(request.chefMember.id.eq(chefMember.id))
                                .where(member.id.eq(memberId)))))
                .fetchFirst();


        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<RequestedChefDto> findRequestedChef(Long memberId, Pageable pageable) {
        QMember requestMember = new QMember("requestMember");
        QMember chefsMember = new QMember("chefsMember");
        QChefMember chefMember = QChefMember.chefMember;
        QRecipe recipe = QRecipe.recipe;
        QRequest request = QRequest.request;

        List<RequestedChefDto> content = queryFactory.select(Projections.constructor(RequestedChefDto.class,
                        chefMember.id, chefMember.introduce,
                        chefMember.starAvg, chefMember.reviewCount,
                        chefsMember.profileUrl, chefsMember.nickname,
                        chefsMember.address.lat, chefsMember.address.lnt,
                        Expressions.numberTemplate(Double.class,
                                "ROUND(ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))/1000, 2)",
                                requestMember.address.lnt, requestMember.address.lat,
                                chefsMember.address.lnt, chefsMember.address.lat).as("distance"),
                        JPAExpressions.select(recipe.count().as("recipeCount"))
                                .from(recipe)
                                .where(recipe.member.id.eq(chefMember.member.id)
                                        .and(recipe.isQuotation.isFalse())
                                        .and(recipe.isDeleted.isFalse())
                                        .and(recipe.isPublic.isTrue())),
                        request.id,
                        Expressions.booleanTemplate("COALESCE({0}, false)",
                                recipe.isQuotation), recipe.id))
                .from(requestMember)
                .join(request)
                .on(request.member.id.eq(requestMember.id)
                        .and(request.isPaid.isFalse())
                        .and(request.dmRoom.isNull())
                        .and(request.isFinished.isFalse()))
                .join(chefMember)
                .on(chefMember.id.eq(request.chefMember.id))
                .join(chefsMember)
                .on(chefsMember.id.eq(chefMember.member.id)
                        .and(chefsMember.isDeleted.isFalse()))
                .leftJoin(recipe)
                .on(recipe.id.eq(request.recipe.id))
                .where(requestMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(requestMember)
                .join(request)
                .on(request.member.id.eq(requestMember.id)
                        .and(request.isPaid.isFalse())
                        .and(request.dmRoom.isNull())
                        .and(request.isFinished.isFalse()))
                .join(chefMember)
                .on(chefMember.id.eq(request.chefMember.id))
                .join(chefsMember)
                .on(chefsMember.id.eq(chefMember.member.id)
                        .and(chefsMember.isDeleted.isFalse()))
                .leftJoin(recipe)
                .on(recipe.id.eq(request.recipe.id))
                .where(requestMember.id.eq(memberId))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }
}
