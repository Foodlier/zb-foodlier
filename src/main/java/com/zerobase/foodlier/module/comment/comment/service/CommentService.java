package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment createComment(Comment comment);
    void updateComment(Long memberId, Long commentId, String modifiedMessage);
    void deleteComment(Long memberId, Long commentId);
    ListResponse<CommentDto> getCommentList(Long recipeId, PageRequest pageRequest);
    Comment findComment(Long commentId);

    ListResponse<MyPageCommentDto> getMyCommentList(Long memberId, Pageable pageable);
}
