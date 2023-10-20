package com.zerobase.foodlier.module.comment.comment.repository;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentSearchRepository{

}