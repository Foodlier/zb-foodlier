package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.comment.exception.CommentException;
import com.zerobase.foodlier.module.comment.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.ALREADY_DELETED;
import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.NO_SUCH_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final String DELETED_MESSAGE = "삭제된 댓글입니다.";
    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long memberId, Long commentId, String modifiedMessage) {
        Comment comment = commentRepository.findComment(memberId, commentId)
                .orElseThrow(() -> new CommentException(NO_SUCH_COMMENT));
        comment.updateMessage(modifiedMessage);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long memberId, Long commentId) {

        Comment comment = commentRepository.findComment(memberId, commentId)
                .orElseThrow(() -> new CommentException(NO_SUCH_COMMENT));
        if (comment.isDeleted()) {
            throw new CommentException(ALREADY_DELETED);
        }
        comment.updateMessage(DELETED_MESSAGE);
        comment.delete();
        commentRepository.save(comment);
    }

    @Override
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(NO_SUCH_COMMENT));
    }

    @Override
    @Transactional(readOnly = true)
    public ListResponse<CommentDto> getCommentList(Long recipeId, PageRequest pageRequest) {
        return ListResponse.from(
                commentRepository.findCommentList(recipeId, pageRequest));
    }

    public ListResponse<MyPageCommentDto> getMyCommentList(Long memberId, Pageable pageable){
        return ListResponse.from(
                commentRepository.findMyCommentList(memberId, pageable));
    }

}
