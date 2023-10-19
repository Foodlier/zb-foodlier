package com.zerobase.foodlier.module.comment.reply.servcie;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import org.springframework.data.domain.PageRequest;

public interface ReplyService {

    Reply createReply(Reply reply);

    void updateReply(Long memberId, Long replyId, String modifiedMessage);

    void deleteReply(Long memberId, Long replyId);
    ListResponse<CommentDto> getReplyList(Long commentId, PageRequest pageRequest);
    ListResponse<MyPageCommentDto> getMyReplyList(Long memberId, PageRequest pageRequest);
}
