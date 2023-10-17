package com.zerobase.foodlier.module.comment.reply.servcie;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyDto;
import org.springframework.data.domain.PageRequest;

public interface ReplyService {

    Reply createReply(Reply reply);

    void updateReply(Long memberId, Long replyId, String modifiedMessage);

    void deleteReply(Long memberId, Long replyId);
    ListResponse<ReplyDto> getReplyList(Long commentId, PageRequest pageRequest);
}
