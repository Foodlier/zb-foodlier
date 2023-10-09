package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentPagingDto;
import org.springframework.data.domain.PageRequest;

public interface CommentService {
    void createComment(Comment comment);
    void updateComment(Long memberId, Long commentId, String modifiedMessage);
    void deleteComment(Long memberId, Long commentId);
    CommentPagingDto getCommentList(Long recipeId, PageRequest pageRequest);
    Comment findComment(Long commentId);
}
