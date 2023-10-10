package com.zerobase.foodlier.module.comment.reply.servcie;

import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyDto;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyPagingDto;
import com.zerobase.foodlier.module.comment.reply.exception.ReplyException;
import com.zerobase.foodlier.module.comment.reply.reposiotry.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.module.comment.reply.exception.ReplyErrorCode.NO_SUCH_REPLY;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public void createReply(Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void updateReply(Long memberId,
                             Long replyId,
                             String modifiedMessage) {
        Reply reply = replyRepository.findReply(replyId, memberId)
                .orElseThrow(() -> new ReplyException(NO_SUCH_REPLY));
        reply.update(modifiedMessage);
        replyRepository.save(reply);
    }
    @Override
    public void deleteReply(Long memberId, Long replyId) {
        Reply reply = replyRepository.findReply(replyId, memberId)
                .orElseThrow(() -> new ReplyException(NO_SUCH_REPLY));

        replyRepository.deleteById(reply.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyPagingDto getReplyList(Long commentId, PageRequest pageRequest) {

        Page<ReplyDto> replyList = replyRepository.findReplyList(commentId, pageRequest);

        return ReplyPagingDto.builder()
                .hasNextPage(replyList.hasNext())
                .totalElements(replyList.getTotalElements())
                .totalPages(replyList.getTotalPages())
                .replyDtoList(replyList.getContent())
                .build();
    }


}
