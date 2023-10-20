package com.zerobase.foodlier.global.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.comment.facade.comment.CommentFacade;
import com.zerobase.foodlier.global.comment.facade.reply.ReplyFacade;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import com.zerobase.foodlier.module.comment.reply.servcie.ReplyService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.ActionType;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.domain.type.PerformerType;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.CommentNotify;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.ReplyNotify;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CommentController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
public class CommentControllerTest {

    @MockBean
    private CommentFacade commentFacade;
    @MockBean
    private CommentService commentService;
    @MockBean
    private ReplyFacade replyFacade;
    @MockBean
    private ReplyService replyService;
    @MockBean
    private NotificationFacade notificationFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 생성 요청 성공")
    void success_create_comment() throws Exception {

        // given
        String message = "맛있어요";

        String response = "댓글 작성이 완료되었습니다.";

        Member requester = Member.builder()
                .id(1L)
                .nickname("요청자")
                .build();
        Member chef = Member.builder()
                .id(2L)
                .nickname("요리사")
                .build();
        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("제육볶음")
                        .build())
                .member(chef)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .member(requester)
                .recipe(recipe)
                .message(message)
                .isDeleted(false)
                .build();
        NotifyInfoDto notifyInfoDto = NotifyInfoDto.builder()
                .performerType(PerformerType.COMMENTER)
                .actionType(ActionType.COMMENT)
                .notificationType(NotificationType.COMMENT)
                .build();

        CommentNotify chefNotify = CommentNotify.from(comment, notifyInfoDto);
        given(commentFacade.createComment(any(), any(), any()))
                .willReturn(comment);
        doNothing().when(notificationFacade).send(chefNotify);

        // when
        mockMvc.perform(post("/comment/1?message=" + message)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        ArgumentCaptor<CommentNotify> argumentCaptor = ArgumentCaptor.forClass(CommentNotify.class);
        // then
        verify(commentFacade, times(1)).createComment(any(), any(), any());
        verify(notificationFacade, times(1)).send(argumentCaptor.capture());

        CommentNotify captured = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(chefNotify.getMessage(), captured.getMessage()),
                () -> assertEquals(chefNotify.getReceiverEmail(), captured.getReceiverEmail()),
                () -> assertEquals(chefNotify.getReceiver(), captured.getReceiver()),
                () -> assertEquals(chefNotify.getTargetSubjectId(), captured.getTargetSubjectId()),
                () -> assertEquals(chefNotify.getPerformerNickname(), captured.getPerformerNickname()),
                () -> assertEquals(chefNotify.getTargetTitle(), captured.getTargetTitle()),
                () -> assertEquals(chefNotify.getNotificationType(), captured.getNotificationType()),
                () -> assertEquals(chefNotify.getNotifyInfoDto().getAction(), captured.getNotifyInfoDto().getAction()),
                () -> assertEquals(chefNotify.getNotifyInfoDto().getPerformer(), captured.getNotifyInfoDto().getPerformer()),
                () -> assertEquals(chefNotify.getNotifyInfoDto().getNotificationType(), captured.getNotifyInfoDto().getNotificationType()),
                () -> assertEquals(chefNotify.getNotifyInfoDto().getPerformerType(), captured.getNotifyInfoDto().getPerformerType())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 수정 요청 성공")
    void success_update_comment() throws Exception {

        // given
        String message = "맛없어요";

        String response = "댓글 수정이 완료되었습니다.";
        doNothing().when(commentService).updateComment(any(), any(), any());

        // when
        mockMvc.perform(put("/comment/1?modifiedMessage=" + message)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        // then
        verify(commentService, times(1)).updateComment(any(), any(), any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 삭제 요청 성공")
    void success_delete_comment() throws Exception {

        // given
        String response = "댓글이 삭제되었습니다.";
        doNothing().when(commentFacade).deleteComment(any(), any(), anyLong());

        // when
        mockMvc.perform(delete("/comment/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        // then
        verify(commentFacade, times(1)).deleteComment(any(), any(), any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 조회 요청 성공")
    void success_getCommentList() throws Exception {

        // given
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        ListResponse<CommentDto> response = ListResponse.<CommentDto>builder()
                .totalPages(1)
                .hasNextPage(false)
                .totalElements(2)
                .content(List.of(CommentDto.builder()
                                .id(2L)
                                .createdAt(LocalDateTime.now())
                                .message("맛있어요")
                                .modifiedAt(LocalDateTime.now())
                                .memberId(1L)
                                .nickname("테스트 계정 1")
                                .profileImageUrl(null)
                        .build(),
                        CommentDto.builder()
                        .id(1L)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .message("맛있어요")
                        .modifiedAt(LocalDateTime.now().minusDays(1))
                        .memberId(10L)
                        .nickname("테스트 계정 10")
                        .profileImageUrl(null)
                        .build()))
                .build();

        given(commentService.getCommentList(any(),any())).willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(get("/comment/1/0/10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // then
        verify(commentService, times(1)).getCommentList(any(), any());

        perform.andExpectAll(
                status().isOk(),
                jsonPath("$.totalPages").value(response.getTotalPages()),
                jsonPath("$.totalElements").value(response.getTotalElements()),
                jsonPath("$.hasNextPage").value(response.isHasNextPage()),
                jsonPath("$.content.length()").value(response.getContent().size()),

                // 첫번째 댓글 값 검증
                jsonPath("$.content[0].id").value(response.getContent().get(0).getId()),
                jsonPath("$.content[0].createdAt").value(response.getContent().get(0).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[0].message").value(response.getContent().get(0).getMessage()),
                jsonPath("$.content[0].modifiedAt").value(response.getContent().get(0).getModifiedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[0].memberId").value(response.getContent().get(0).getMemberId()),
                jsonPath("$.content[0].nickname").value(response.getContent().get(0).getNickname()),
                jsonPath("$.content[0].profileImageUrl").value(response.getContent().get(0).getProfileImageUrl()),

                // 두번째 댓글 값 검증
                jsonPath("$.content[1].id").value(response.getContent().get(1).getId()),
                jsonPath("$.content[1].createdAt").value(response.getContent().get(1).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[1].message").value(response.getContent().get(1).getMessage()),
                jsonPath("$.content[1].modifiedAt").value(response.getContent().get(1).getModifiedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[1].memberId").value(response.getContent().get(1).getMemberId()),
                jsonPath("$.content[1].nickname").value(response.getContent().get(1).getNickname()),
                jsonPath("$.content[1].profileImageUrl").value(response.getContent().get(1).getProfileImageUrl())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("답글 생성 요청 성공")
    void success_create_reply() throws Exception {

        // given
        String message = "진짜 맛있나요?";

        String response = "답글 작성이 완료되었습니다.";

        Member requester = Member.builder()
                .id(1L)
                .nickname("요청자")
                .build();
        Member chef = Member.builder()
                .id(2L)
                .nickname("요리사")
                .build();
        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("제육볶음")
                        .build())
                .member(chef)
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .isDeleted(false)
                .recipe(recipe)
                .message("맛있어요")
                .build();
        Reply reply = Reply.builder()
                .id(1L)
                .member(requester)
                .comment(comment)
                .message(message)
                .build();

        NotifyInfoDto notifyInfoDto = NotifyInfoDto.builder()
                .performerType(PerformerType.COMMENTER)
                .actionType(ActionType.REPLY)
                .notificationType(NotificationType.RE_COMMENT)
                .build();

        ReplyNotify replyNotify = ReplyNotify.from(reply, notifyInfoDto);
        given(replyFacade.createReply(any(), any(), any(), any()))
                .willReturn(reply);
        doNothing().when(notificationFacade).send(replyNotify);

        // when
        mockMvc.perform(post("/comment/reply/1/1?message=" + message)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        ArgumentCaptor<ReplyNotify> argumentCaptor = ArgumentCaptor.forClass(ReplyNotify.class);
        // then
        verify(replyFacade, times(1)).createReply(any(), any(), any(), any());
        verify(notificationFacade, times(1)).send(argumentCaptor.capture());

        ReplyNotify captured = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(replyNotify.getMessage(), captured.getMessage()),
                () -> assertEquals(replyNotify.getReceiverEmail(), captured.getReceiverEmail()),
                () -> assertEquals(replyNotify.getReceiver(), captured.getReceiver()),
                () -> assertEquals(replyNotify.getTargetSubjectId(), captured.getTargetSubjectId()),
                () -> assertEquals(replyNotify.getPerformerNickname(), captured.getPerformerNickname()),
                () -> assertEquals(replyNotify.getTargetTitle(), captured.getTargetTitle()),
                () -> assertEquals(replyNotify.getNotificationType(), captured.getNotificationType()),
                () -> assertEquals(replyNotify.getNotifyInfoDto().getAction(), captured.getNotifyInfoDto().getAction()),
                () -> assertEquals(replyNotify.getNotifyInfoDto().getPerformer(), captured.getNotifyInfoDto().getPerformer()),
                () -> assertEquals(replyNotify.getNotifyInfoDto().getNotificationType(), captured.getNotifyInfoDto().getNotificationType()),
                () -> assertEquals(replyNotify.getNotifyInfoDto().getPerformerType(), captured.getNotifyInfoDto().getPerformerType())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("답글 수정 요청 성공")
    void success_update_reply() throws Exception {

        // given
        String message = "진짜 맛없어요";

        String response = "답글 수정이 완료되었습니다.";
        doNothing().when(replyService).updateReply(any(), any(), any());

        // when
        mockMvc.perform(put("/comment/reply/1?message=" + message)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        // then
        verify(replyService, times(1)).updateReply(any(), any(), any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("답글 삭제 요청 성공")
    void success_delete_reply() throws Exception {

        // given
        String response = "답글이 삭제되었습니다.";
        doNothing().when(replyFacade).deleteReply(any(), any(), anyLong());

        // when
        mockMvc.perform(delete("/comment/reply/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
        // then
        verify(replyFacade, times(1)).deleteReply(any(), any(), any());
    }
    @Test
    @WithCustomMockUser
    @DisplayName("답글 조회 요청 성공")
    void success_getReplyList() throws Exception {

        // given
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        ListResponse<CommentDto> response = ListResponse.<CommentDto>builder()
                .totalPages(1)
                .hasNextPage(false)
                .totalElements(2)
                .content(List.of(CommentDto.builder()
                                .id(2L)
                                .createdAt(LocalDateTime.now())
                                .message("맛있어요")
                                .modifiedAt(LocalDateTime.now())
                                .memberId(1L)
                                .nickname("테스트 계정 1")
                                .profileImageUrl(null)
                                .build(),
                        CommentDto.builder()
                                .id(1L)
                                .createdAt(LocalDateTime.now().minusDays(1))
                                .message("맛있어요")
                                .modifiedAt(LocalDateTime.now().minusDays(1))
                                .memberId(10L)
                                .nickname("테스트 계정 10")
                                .profileImageUrl(null)
                                .build()))
                .build();

        given(replyService.getReplyList(any(),any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(get("/comment/reply/1/0/10")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // then
        verify(replyService, times(1)).getReplyList(any(), any());

        perform.andExpectAll(
                status().isOk(),
                jsonPath("$.totalPages").value(response.getTotalPages()),
                jsonPath("$.totalElements").value(response.getTotalElements()),
                jsonPath("$.hasNextPage").value(response.isHasNextPage()),
                jsonPath("$.content.length()").value(response.getContent().size()),

                // 첫번째 댓글 값 검증
                jsonPath("$.content[0].id").value(response.getContent().get(0).getId()),
                jsonPath("$.content[0].createdAt").value(response.getContent().get(0).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[0].message").value(response.getContent().get(0).getMessage()),
                jsonPath("$.content[0].modifiedAt").value(response.getContent().get(0).getModifiedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[0].memberId").value(response.getContent().get(0).getMemberId()),
                jsonPath("$.content[0].nickname").value(response.getContent().get(0).getNickname()),
                jsonPath("$.content[0].profileImageUrl").value(response.getContent().get(0).getProfileImageUrl()),

                // 두번째 댓글 값 검증
                jsonPath("$.content[1].id").value(response.getContent().get(1).getId()),
                jsonPath("$.content[1].createdAt").value(response.getContent().get(1).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[1].message").value(response.getContent().get(1).getMessage()),
                jsonPath("$.content[1].modifiedAt").value(response.getContent().get(1).getModifiedAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[1].memberId").value(response.getContent().get(1).getMemberId()),
                jsonPath("$.content[1].nickname").value(response.getContent().get(1).getNickname()),
                jsonPath("$.content[1].profileImageUrl").value(response.getContent().get(1).getProfileImageUrl())
        );
    }
}
