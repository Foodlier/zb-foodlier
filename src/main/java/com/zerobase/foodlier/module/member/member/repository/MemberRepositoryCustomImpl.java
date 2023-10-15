package com.zerobase.foodlier.module.member.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.member.chef.domain.model.QChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
import com.zerobase.foodlier.module.request.domain.model.QRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestedMemberDto> getRequestedMemberListOrderByType(Long chefMemberId, double lat, double lnt, Pageable pageable, RequestedOrderingType orderingType) {

        QMember member = QMember.member;
        QChefMember chefMember = QChefMember.chefMember;
        QRequest request = QRequest.request;
        QRecipe recipe = QRecipe.recipe;
        StringPath orderBy = orderingType.equals(RequestedOrderingType.DISTANCE) ?
                Expressions.stringPath("distance") : Expressions.stringPath("expectedPrice");

        List<RequestedMemberDto> content = queryFactory.select(Projections.constructor(RequestedMemberDto.class,
                        member.id, member.profileUrl, member.nickname,
                        Expressions.numberTemplate(Double.class,
                                "ROUND(ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))/1000, 2)",
                                member.address.lnt, member.address.lat, lnt, lat).as("distance"),
                        member.address.lat, member.address.lnt, request.id, request.title, request.content,
                        request.expectedPrice.as("expectedPrice"),
                        recipe.mainImageUrl))
                .from(chefMember)
                .join(request)
                .on(request.chefMember.id.eq(chefMember.id)
                        .and(request.isPaid.isFalse()).and(request.dmRoom.isNull()))
                .join(member)
                .on(request.member.id.eq(member.id))
                .leftJoin(recipe)
                .on(request.recipe.id.eq(recipe.id).and(request.recipe.isNull().or(request.recipe.isQuotation.isTrue())))
                .where(chefMember.id.eq(chefMemberId))
                .orderBy(orderBy.asc())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(chefMember)
                .join(request)
                .on(request.chefMember.id.eq(chefMember.id).and(request.isPaid.isFalse()).and(request.dmRoom.isNull()))
                .join(member)
                .on(request.member.id.eq(member.id))
                .leftJoin(recipe)
                .on(recipe.id.eq(request.recipe.id))
                .where(chefMember.id.eq(chefMemberId))
                .fetchFirst();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public DefaultProfileDtoResponse getDefaultProfile(Long memberId) {
        QMember member = QMember.member;
        QChefMember chefMember = QChefMember.chefMember;
        QRecipe recipe = QRecipe.recipe;

        return queryFactory.select(Projections.constructor(DefaultProfileDtoResponse.class,
                        member.id, member.nickname, member.profileUrl,
                        recipe.heartCount.sum().coalesce(0),
                        new CaseBuilder().when(chefMember.id.isNull())
                                .then(false).otherwise(true), chefMember.id))
                .from(member)
                .leftJoin(chefMember)
                .on(chefMember.member.id.eq(member.id))
                .leftJoin(recipe)
                .on(recipe.member.id.eq(member.id)
                        .and(recipe.isPublic.isTrue())
                        .and(recipe.isQuotation.isFalse())
                        .and(recipe.isDeleted.isFalse()))
                .where(member.id.eq(memberId))
                .groupBy(member.id, member.nickname, member.profileUrl, chefMember.id)
                .fetchFirst();
    }
}
