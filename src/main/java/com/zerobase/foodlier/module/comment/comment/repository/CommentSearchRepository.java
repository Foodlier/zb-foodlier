package com.zerobase.foodlier.module.comment.comment.repository;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentSearchRepository {

    Page<CommentDto> findCommentList(Long recipeId, Pageable pageable);
    Optional<Comment> findComment(Long memberId, Long commentId);
    Page<MyPageCommentDto> findMyCommentList(Long memberId, Pageable pageable);
}
