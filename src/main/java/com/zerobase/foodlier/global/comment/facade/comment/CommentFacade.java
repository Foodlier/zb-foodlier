package com.zerobase.foodlier.global.comment.facade.comment;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.comment.comment.service.CommentServiceImpl;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentFacade {

    private final CommentService commentService;
    private final RecipeService recipeService;
    private final MemberService memberService;
    @Transactional
    @RedissonLock(group = "comment", key = "#recipeId")
    public Comment createComment(Long recipeId, String userEmail, String message) {

        Member member = memberService.findByEmail(userEmail);

        Recipe recipe = recipeService.plusCommentCount(recipeId);

        return commentService.createComment(Comment.builder()
                .message(message)
                .isDeleted(false)
                .recipe(recipe)
                .member(member)
                .build());
    }

    @Transactional
    @RedissonLock(group = "comment", key = "#recipeId")
    public void deleteComment(Long recipeId, Long commentId, Long memberId) {
        recipeService.minusCommentCount(recipeId);
        commentService.deleteComment(memberId, commentId);
    }

}
