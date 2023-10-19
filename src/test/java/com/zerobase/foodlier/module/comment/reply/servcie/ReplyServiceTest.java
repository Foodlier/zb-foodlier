package com.zerobase.foodlier.module.comment.reply.servcie;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.dto.ReplyDto;
import com.zerobase.foodlier.module.comment.reply.exception.ReplyException;
import com.zerobase.foodlier.module.comment.reply.reposiotry.ReplyRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.zerobase.foodlier.module.comment.reply.exception.ReplyErrorCode.NO_SUCH_REPLY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    private ReplyService replyService;

    @BeforeEach
    void setup() {
        replyService = new ReplyService(replyRepository);
    }

    @Test
    @DisplayName("답글 작성 성공")
    void success_create_reply() {

        // given
        Member member = getMember();

        Recipe recipe = getRecipe();

        Comment comment = getComment(member, recipe);

        Reply reply = getReply(comment);

        given(replyRepository.save(any()))
                .willReturn(reply);

        // when

        replyService.createReply(any());

        // then
        verify(replyRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("답글 수정 성공")
    void success_update_reply() {

        // given
        Member member = getMember();

        Recipe recipe = getRecipe();

        Comment comment = getComment(member, recipe);

        Reply reply = getReply(comment);

        Reply updatedReply = getUpdatedReply(comment);

        given(replyRepository.findReply(any(), any()))
                .willReturn(Optional.of(reply));

        given(replyRepository.save(any()))
                .willReturn(updatedReply);
        // when

        replyService.updateReply(member.getId(), reply.getId(), updatedReply.getMessage());

        // then
        verify(replyRepository, times(1)).findReply(any(), any());
        verify(replyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("답글 수정 실패 - 답글이 존재하지 않는 경우")
    void fail_update_reply() {

        // given
        Member member = getMember();

        Recipe recipe = getRecipe();

        Comment comment = getComment(member, recipe);

        Reply reply = getReply(comment);

        Reply updatedReply = getUpdatedReply(comment);

        given(replyRepository.findReply(any(), any()))
                .willThrow(new ReplyException(NO_SUCH_REPLY));

        // when

        ReplyException replyException = assertThrows(ReplyException.class,
                () -> replyService.updateReply(member.getId(),
                        reply.getId(), updatedReply.getMessage()));
        // then
        verify(replyRepository, times(1)).findReply(any(), any());
        assertEquals(replyException.getErrorCode(), NO_SUCH_REPLY);
        assertEquals(replyException.getDescription(), NO_SUCH_REPLY.getDescription());
    }

    @Test
    @DisplayName("답글 삭제 성공")
    void success_delete_reply() {

        // given
        Member member = getMember();

        Recipe recipe = getRecipe();

        Comment comment = getComment(member, recipe);

        Reply reply = getReply(comment);
        given(replyRepository.findReply(any(), any()))
                .willReturn(Optional.of(reply));
        doNothing().when(replyRepository).deleteById(any());
        // when

        replyService.deleteReply(member.getId(), reply.getId());
        // then
        verify(replyRepository, times(1)).findReply(any(), any());
        verify(replyRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("답글 삭제 실패 - 이미 삭제된 경우")
    void fail_delete_reply() {

        // given
        Member member = getMember();

        Recipe recipe = getRecipe();

        Comment comment = getComment(member, recipe);

        Reply reply = getReply(comment);

        Reply updatedReply = getUpdatedReply(comment);

        given(replyRepository.findReply(any(), any()))
                .willThrow(new ReplyException(NO_SUCH_REPLY));

        // when

        ReplyException replyException = assertThrows(ReplyException.class,
                () -> replyService.updateReply(member.getId(),
                        reply.getId(), updatedReply.getMessage()));
        // then
        verify(replyRepository, times(1)).findReply(any(), any());
        assertEquals(replyException.getErrorCode(), NO_SUCH_REPLY);
        assertEquals(replyException.getDescription(), NO_SUCH_REPLY.getDescription());
    }


    @Test
    @DisplayName("답글 목록 조회")
    void success_getReplyList() {

        // given
        PageImpl<ReplyDto> findingResult = getReplyList();
        given(replyRepository.findReplyList(any(), any()))
                .willReturn(findingResult);
        ListResponse<ReplyDto> expectedPagingDto = ListResponse.from(findingResult);
        // when

        ListResponse<ReplyDto> replyPagingDto = replyService.getReplyList(any(), any());

        // then
        assertAll(
                () -> assertEquals(replyPagingDto.getContent().size(), expectedPagingDto.getContent().size()),
                () -> assertEquals(replyPagingDto.getTotalPages(), expectedPagingDto.getTotalPages()),
                () -> assertEquals(replyPagingDto.isHasNextPage(), expectedPagingDto.isHasNextPage()),
                () -> assertEquals(replyPagingDto.getTotalElements(), expectedPagingDto.getTotalElements())
        );

        for (int i = 0; i < expectedPagingDto.getContent().size(); i++) {
            assertEquals(replyPagingDto.getContent().get(i).getMessage(),
                    expectedPagingDto.getContent().get(i).getMessage());
            assertEquals(replyPagingDto.getContent().get(i).getCreatedAt(),
                    expectedPagingDto.getContent().get(i).getCreatedAt());
        }

    }

    private static Member getMember() {
        return Member.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();
    }

    private static Recipe getRecipe() {
        return Recipe.builder()
                .id(1L)
                .commentCount(2)
                .build();
    }

    private static Comment getComment(Member member, Recipe recipe) {
        return Comment.builder()
                .id(1L)
                .isDeleted(false)
                .message("맛있네요")
                .member(member)
                .recipe(recipe)
                .build();
    }

    private static Reply getReply(Comment comment) {
        return Reply.builder()
                .id(1L)
                .comment(comment)
                .message("엥? 무슨 말씀이세요!")
                .build();
    }

    private static Reply getUpdatedReply(Comment comment) {
        return Reply.builder()
                .id(1L)
                .comment(comment)
                .message("엥?")
                .build();
    }

    private static PageImpl<ReplyDto> getReplyList() {

        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                ReplyDto.builder()
                        .message("저는 별로던데...")
                        .createdAt(LocalDateTime.now())
                        .build(),
                ReplyDto.builder()
                        .message("진짜 맛있어요?")
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .build()

        )));
    }
}