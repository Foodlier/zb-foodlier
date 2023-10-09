package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentPagingDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    void createComment(Comment comment);
    void updateComment(Long memberId, Long commentId, String modifiedMessage);
    void deleteComment(Long memberId, Long commentId);
    CommentPagingDto getCommentList(Long recipeId, PageRequest pageRequest);
    Comment findComment(Long commentId);

    List<MyPageCommentDto> getMyCommentList(Long memberId, Pageable pageable);
}
