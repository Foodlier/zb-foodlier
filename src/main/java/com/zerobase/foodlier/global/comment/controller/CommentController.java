package com.zerobase.foodlier.global.comment.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.comment.facade.comment.CommentFacade;
import com.zerobase.foodlier.global.comment.facade.reply.ReplyFacade;
import com.zerobase.foodlier.module.comment.comment.dto.CommentPagingDto;
import com.zerobase.foodlier.module.comment.comment.service.CommentServiceImpl;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyPagingDto;
import com.zerobase.foodlier.module.comment.reply.servcie.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentFacade commentFacade;
    private final CommentServiceImpl commentService;
    private final ReplyFacade replyFacade;
    private final ReplyService replyService;

    // ======================== 댓글 api ======================================
    @PostMapping("/{recipeId}")
    public ResponseEntity<String> createComment(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                @PathVariable Long recipeId,
                                                @RequestBody String message)
    {
        commentFacade.createComment(recipeId, memberAuthDto.getEmail(), message);
        return ResponseEntity.ok("댓글 작성이 완료되었습니다.");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                @PathVariable Long commentId,
                                                @RequestBody String modifiedMessage)
    {
        commentService.updateComment(memberAuthDto.getId(), commentId, modifiedMessage);
        return ResponseEntity.ok("댓글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{recipeId}/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                @PathVariable Long recipeId,
                                                @PathVariable Long commentId) {
        commentFacade.deleteComment(recipeId, commentId, memberAuthDto.getId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    @GetMapping("/{recipeId}/{pageIdx}/{pageSize}")
    public ResponseEntity<CommentPagingDto> getCommentList(@PathVariable int pageIdx,
                                                           @PathVariable int pageSize,
                                                           @PathVariable Long recipeId)
    {
        return ResponseEntity.ok(commentService.getCommentList(recipeId,
                PageRequest.of(pageIdx, pageSize)));
    }


    // ======================== 답글 api ======================================
    @PostMapping("/reply/{recipeId}/{commentId}")
    public ResponseEntity<String> createReply(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                              @PathVariable Long recipeId,
                                              @PathVariable Long commentId,
                                              @RequestBody String message)
    {
        replyFacade.createReply(commentId, recipeId, memberAuthDto.getEmail(), message);
        return ResponseEntity.ok("답글 작성이 완료되었습니다.");
    }

    @PutMapping("/reply/{replyId}")
    public ResponseEntity<String> updateReply(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                              @PathVariable Long replyId,
                                              @RequestBody String message)
    {
        replyService.updateReply(memberAuthDto.getId(), replyId, message);
        return ResponseEntity.ok("답글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/reply/{recipeId}/{replyId}")
    public ResponseEntity<String> deleteRely(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                             @PathVariable Long recipeId,
                                             @PathVariable Long replyId)
    {
        replyFacade.deleteReply(replyId, recipeId, memberAuthDto.getId());
        return ResponseEntity.ok("답글이 삭제되었습니다.");
    }

    @GetMapping("/reply/{commentId}/{pageIdx}/{pageSize}")
    public ResponseEntity<ReplyPagingDto> getReplyList(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                       @PathVariable Long commentId,
                                                       @PathVariable int pageIdx,
                                                       @PathVariable int pageSize)
    {
        return ResponseEntity.ok(replyService.getReplyList(commentId,
                PageRequest.of(pageIdx, pageSize)));
    }


}
