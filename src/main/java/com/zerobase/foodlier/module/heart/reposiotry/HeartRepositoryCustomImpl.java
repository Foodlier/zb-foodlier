package com.zerobase.foodlier.module.heart.reposiotry;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.domain.model.QHeart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HeartRepositoryCustomImpl implements HeartRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Heart> findHeart(Long recipeId, Long memberId) {
        QHeart heart = QHeart.heart;

        return Optional.ofNullable(queryFactory.selectFrom(heart)
                .where(heart.recipe.id.eq(recipeId)
                        .and(heart.member.id.eq(memberId)))
                .fetchOne());
    }

    @Override
    public boolean existsHeart(Long recipeId, Member member) {
        QRecipe recipe = QRecipe.recipe;
        QHeart heart = QHeart.heart;

        return queryFactory.selectFrom(heart)
                .join(recipe)
                .on(recipe.id.eq(recipeId))
                .where(heart.member.eq(member).and(heart.recipe.id.eq(recipeId)))
                .fetchFirst() != null;
    }
}
