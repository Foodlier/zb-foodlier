package com.zerobase.foodlier.module.comment.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.domain.model.QComment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.member.member.domain.model.QMember;
import com.zerobase.foodlier.module.recipe.domain.model.QRecipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentSearchRepositoryImpl implements CommentSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentDto> findCommentList(Long recipeId, Pageable pageable) {
        QRecipe recipe = QRecipe.recipe;
        QComment comment = QComment.comment;

        List<CommentDto> result = jpaQueryFactory.select(Projections.constructor(CommentDto.class,
                        comment.id, comment.message, comment.createdAt, comment.modifiedAt,
                        comment.member.nickname, comment.member.profileUrl, comment.member.id))
                .from(comment)
                .join(recipe)
                .on(recipe.id.eq(recipeId))
                .where(comment.recipe.id.eq(recipeId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(comment)
                .join(recipe)
                .on(recipe.id.eq(recipeId))
                .fetchFirst();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Optional<Comment> findComment(Long memberId, Long commentId) {
        QComment comment = QComment.comment;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(comment)
                .where(comment.member.id.eq(memberId)
                        .and(comment.id.eq(commentId)))
                .fetchOne());
    }

    @Override
    public Page<MyPageCommentDto> findMyCommentList(Long memberId, Pageable pageable) {
        QComment comment = QComment.comment;
        List<MyPageCommentDto> result = jpaQueryFactory.select(Projections.constructor(MyPageCommentDto.class, comment.recipe.id, comment.message, comment.createdAt))
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(comment)
                .where(comment.member.id.eq(memberId))
                .fetchFirst();

        return new PageImpl<>(result, pageable, count);
    }
}
