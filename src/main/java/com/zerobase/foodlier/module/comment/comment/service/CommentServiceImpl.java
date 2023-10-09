package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.CommentPagingDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.comment.exception.CommentException;
import com.zerobase.foodlier.module.comment.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.ALREADY_DELETED;
import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.NO_SUCH_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final String DELETED_MESSAGE = "삭제된 댓글입니다.";
    private final CommentRepository commentRepository;

    @Override
    public void createComment(Comment comment) {
        commentRepository.save(comment);
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
    public CommentPagingDto getCommentList(Long recipeId, PageRequest pageRequest) {

        Page<CommentDto> commentDtoList = commentRepository.findCommentList(recipeId, pageRequest);

        return CommentPagingDto.builder()
                .hasNextPage(commentDtoList.hasNext())
                .totalElements(commentDtoList.getTotalElements())
                .totalPages(commentDtoList.getTotalPages())
                .commentDtoList(commentDtoList.getContent())
                .build();
    }

    public List<MyPageCommentDto> getMyCommentList(Long memberId, Pageable pageable){
        return commentRepository.findMyCommentList(memberId, pageable)
                .getContent();
    }

}
