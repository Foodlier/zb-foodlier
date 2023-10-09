package com.zerobase.foodlier.module.comment.reply.servcie;

import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyPagingDto;
import org.springframework.data.domain.PageRequest;

public interface ReplyService {

    void createReply(Reply reply);

    void updateReply(Long memberId, Long replyId, String modifiedMessage);

    void deleteReply(Long memberId, Long replyId);
    ReplyPagingDto getReplyList(Long commentId, PageRequest pageRequest);
}
