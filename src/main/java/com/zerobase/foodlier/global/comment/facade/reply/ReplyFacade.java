package com.zerobase.foodlier.global.comment.facade.reply;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.servcie.ReplyService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyFacade {
    private static final String SUBJECT = "나의 ";
    private final ReplyService replyService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final RecipeService recipeService;
    private final NotificationFacade notificationFacade;
    @Transactional
    @RedissonLock(group = "comment", key = "#recipeId")
    public Reply createReply(Long commentId, Long recipeId, String userEmail, String message) {

        Member member = memberService.findByEmail(userEmail);

        Recipe recipe = recipeService.plusCommentCount(recipeId);
        Comment comment = commentService.findComment(commentId);
        return replyService.createReply(Reply.builder()
                .message(message)
                .comment(comment)
                .member(member)
                .build());

    }

    @Transactional
    @RedissonLock(group = "comment", key = "#recipeId")
    public void deleteReply(Long replyId, Long recipeId, Long memberId) {
        recipeService.minusCommentCount(recipeId);
        replyService.deleteReply(memberId,replyId);
    }


}
