package com.zerobase.foodlier.module.comment.reply.reposiotry;

import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r " +
            "from Reply r " +
            "left join r.member m " +
            "where m.id = :memberId " +
            "and r.id = :replyId")
    Optional<Reply> findReply(@Param("replyId") Long replyId,
                              @Param("memberId") Long memberId);

    @Query("select new com.zerobase.foodlier.module.comment.reply.dto.ReplyDto(r.message, r.createdAt, r.modifiedAt) " +
            "from Reply r " +
            "join r.comment c " +
            "where c.id = :commentId " +
            "order by r.createdAt asc ")
    Page<ReplyDto> findReplyList(@Param("commentId") Long commentId,
                                 Pageable pageable);
}
