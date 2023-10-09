package com.zerobase.foodlier.module.comment.comment.repository;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select new com.zerobase.foodlier.module.comment.comment.dto.CommentDto(" +
            "c.message, c.createdAt, c.modifiedAt) " +
            "from Comment c " +
            "join c.recipe r " +
            "where r.id=:recipeId " +
            "order by c.createdAt desc")
    Page<CommentDto> findCommentList(@Param("recipeId") Long recipeId,
                                     Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "join c.member m " +
            "where m.id = :memberId and c.id=:commentId")
    Optional<Comment> findComment(@Param("memberId") Long memberId,
                                  @Param("commentId") Long commentId);


}
