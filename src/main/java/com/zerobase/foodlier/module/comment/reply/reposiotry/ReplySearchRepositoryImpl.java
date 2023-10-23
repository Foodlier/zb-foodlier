package com.zerobase.foodlier.module.comment.reply.reposiotry;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.comment.comment.domain.model.QComment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.reply.domain.model.QReply;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReplySearchRepositoryImpl implements ReplySearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Reply> findReply(Long replyId, Long memberId) {
        QReply reply = QReply.reply;

        return Optional.ofNullable(jpaQueryFactory.selectFrom(reply)
                .where(reply.member.id.eq(memberId)
                        .and(reply.id.eq(replyId)))
                .fetchOne());
    }

    @Override
    public Page<CommentDto> findReplyList(Long commentId, Pageable pageable) {
        QReply reply = QReply.reply;
        QComment comment = QComment.comment;
        List<CommentDto> result = jpaQueryFactory.select(Projections.constructor(CommentDto.class,
                        reply.id, reply.message, reply.createdAt, reply.modifiedAt,
                        reply.member.nickname, reply.member.profileUrl, reply.member.id))
                .from(reply)
                .join(comment)
                .on(comment.id.eq(commentId))
                .where(reply.comment.id.eq(commentId))
                .orderBy(reply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(reply)
                .join(comment)
                .on(comment.id.eq(commentId))
                .where(reply.comment.id.eq(commentId))
                .fetchFirst();

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<MyPageCommentDto> findMyReplyList(Long memberId, Pageable pageable) {
        QReply reply = QReply.reply;

        List<MyPageCommentDto> result = jpaQueryFactory.select(Projections.constructor(MyPageCommentDto.class, reply.comment.recipe.id, reply.message, reply.createdAt))
                .from(reply).where(reply.member.id.eq(memberId))
                .orderBy(reply.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(reply)
                .where(reply.member.id.eq(memberId))
                .fetchFirst();

        return new PageImpl<>(result, pageable, count);
    }
}
