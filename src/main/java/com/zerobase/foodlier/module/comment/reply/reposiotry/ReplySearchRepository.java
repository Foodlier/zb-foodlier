package com.zerobase.foodlier.module.comment.reply.reposiotry;

import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReplySearchRepository {

    Optional<Reply> findReply(Long replyId, Long memberId);
    Page<CommentDto> findReplyList(Long commentId, Pageable pageable);
    Page<MyPageCommentDto> findMyReplyList(Long memberId, Pageable pageable);

}
