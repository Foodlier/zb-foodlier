package com.zerobase.foodlier.module.comment.comment.repository;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select new com.zerobase.foodlier.module.comment.comment.dto.CommentDto(" +
            "c.id, c.message, c.createdAt, c.modifiedAt) " +
            "from Comment c " +
            "join c.recipe r " +
            "where r.id=:recipeId " +
            "order by c.createdAt desc ")
    Page<CommentDto> findCommentList(@Param("recipeId") Long recipeId,
                                     Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "join c.member m " +
            "where m.id = :memberId and c.id=:commentId ")
    Optional<Comment> findComment(@Param("memberId") Long memberId,
                                  @Param("commentId") Long commentId);


    @Query(
            value = "SELECT recipe_id as recipeId, member_id as memberId, message, created_at as createdAt\n" +
                    "FROM comment\n" +
                    "WHERE comment.member_id = :memberId\n" +
                    "UNION \n" +
                    "SELECT c.recipe_id, r.member_id, r.message, r.created_at\n" +
                    "FROM reply r\n" +
                    "JOIN comment c ON c.id = r.comment_id\n" +
                    "WHERE r.member_id = :memberId",
            countQuery = "SELECT COUNT(*)\n" +
                    "FROM (\n" +
                    "\tSELECT recipe_id, member_id, message, created_at\n" +
                    "\tFROM comment\n" +
                    "\tWHERE comment.member_id = :memberId\n" +
                    "\tUNION \n" +
                    "\tSELECT c.recipe_id, r.member_id, r.message, r.created_at\n" +
                    "\tFROM reply r\n" +
                    "\tJOIN comment c ON c.id = r.comment_id\n" +
                    "\tWHERE r.member_id = :memberId\n" +
                    ") AS count;\n",
            nativeQuery = true
    )
    Page<MyPageCommentDto> findMyCommentList(@Param("memberId")Long memberId,
                                             Pageable pageable);

}