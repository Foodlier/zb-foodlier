package com.zerobase.foodlier.module.heart.reposiotry;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.domain.model.QHeart;
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
}
