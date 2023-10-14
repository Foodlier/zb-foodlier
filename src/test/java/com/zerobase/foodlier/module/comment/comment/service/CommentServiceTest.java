package com.zerobase.foodlier.module.comment.comment.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.dto.CommentDto;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode;
import com.zerobase.foodlier.module.comment.comment.exception.CommentException;
import com.zerobase.foodlier.module.comment.comment.repository.CommentRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.ALREADY_DELETED;
import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.NO_SUCH_COMMENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    @DisplayName("댓글 생성 성공")
    void success_create_comment() {

        // given
        Comment expectedComment = getExpectedComment();

        given(commentRepository.save(any()))
                .willReturn(expectedComment);

        // when
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        commentService.createComment(expectedComment);

        // then
        verify(commentRepository, times(1)).save(commentArgumentCaptor.capture());

        assertAll(
                () -> assertEquals(expectedComment.getId(), commentArgumentCaptor.getValue().getId()),
                () -> assertEquals(expectedComment.getRecipe(), commentArgumentCaptor.getValue().getRecipe()),
                () -> assertEquals(expectedComment.getMember(), commentArgumentCaptor.getValue().getMember()),
                () -> assertEquals(expectedComment.getRecipe(), commentArgumentCaptor.getValue().getRecipe())
        );

    }

    @Test
    @DisplayName("댓글 수정 성공")
    void success_update_comment() {

        // given
        Comment expectedComment = getExpectedComment();
        Comment updateComment = getExpectedUpdatedComment();
        given(commentRepository.findComment(anyLong(), anyLong()))
                .willReturn(Optional.of(expectedComment));
        given(commentRepository.save(any()))
                .willReturn(updateComment);

        // when
        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        commentService.updateComment(1L, 1L, updateComment.getMessage());

        // then
        verify(commentRepository, times(1)).findComment(anyLong(), anyLong());
        verify(commentRepository, times(1)).save(commentArgumentCaptor.capture());

        assertAll(
                () -> assertEquals(updateComment.getMessage(), commentArgumentCaptor.getValue().getMessage())
        );

    }

    @Test
    @DisplayName("댓글 수정 실패")
    void fail_update_comment() {

        // given
        given(commentRepository.findComment(anyLong(), anyLong()))
                .willThrow(new CommentException(CommentErrorCode.NO_SUCH_COMMENT));

        // when
        CommentException commentException = assertThrows(CommentException.class,
                () -> commentService.updateComment(1L, 1L, "제육볶음 존맛탱"));

        // then
        assertAll(
                () -> assertEquals(commentException.getDescription(), CommentErrorCode.NO_SUCH_COMMENT.getDescription()),
                () -> assertEquals(commentException.getErrorCode(), CommentErrorCode.NO_SUCH_COMMENT)
        );
    }

    @Test
    @DisplayName("삭제 성공")
    void success_delete_comment(){

        // given
        Comment deleteComment = getExpectedDeleteComment();
        Comment comment = mock(Comment.class);

        given(commentRepository.findComment(anyLong(), anyLong()))
                .willReturn(Optional.of(comment));

        when(comment.isDeleted()).thenReturn(false);
        doNothing().when(comment).updateMessage(any());
        doNothing().when(comment).delete();

        given(commentRepository.save(any()))
                .willReturn(deleteComment);

        // when
        commentService.deleteComment(1L, 1L);

        // then
        verify(comment, times(1)).isDeleted();
        verify(comment, times(1)).updateMessage(any());
        verify(comment, times(1)).delete();
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("삭제 실패 - 이미 삭제됨")
    void fail_delete_comment(){

        // given
        Comment comment = mock(Comment.class);

        given(commentRepository.findComment(anyLong(), anyLong()))
                .willReturn(Optional.of(comment));

        when(comment.isDeleted()).thenReturn(true);

        // when
        CommentException commentException = assertThrows(CommentException.class, () -> commentService.deleteComment(1L, 1L));

        // then
        verify(comment, times(1)).isDeleted();
        assertEquals(commentException.getErrorCode(), ALREADY_DELETED);
        assertEquals(commentException.getDescription(), ALREADY_DELETED.getDescription());
    }

    @Test
    @DisplayName("댓글 조회 성공")
    void success_find_comment() {

        // given
        Comment expectedComment = getExpectedComment();
        given(commentRepository.findById(anyLong()))
                .willReturn(Optional.of(expectedComment));

        // when
        Comment comment = commentService.findComment(1L);

        // then
        assertEquals(expectedComment.getRecipe(), comment.getRecipe());
        assertEquals(expectedComment.getId(), comment.getId());
        assertEquals(expectedComment.getMessage(), comment.getMessage());
    }

    @Test
    @DisplayName("댓글 조회 실패")
    void fail_find_comment() {

        // given
        given(commentRepository.findById(anyLong()))
                .willThrow(new CommentException(NO_SUCH_COMMENT));

        // when
        CommentException commentException = assertThrows(CommentException.class, () -> commentService.findComment(1L));

        // then
        verify(commentRepository, times(1)).findById(anyLong());
        assertEquals(NO_SUCH_COMMENT, commentException.getErrorCode());
        assertEquals(NO_SUCH_COMMENT.getDescription(), commentException.getDescription());
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void success_getCommentList(){

        // given
        PageImpl<CommentDto> commentDtos = getCommentDtoPages();
        ListResponse<CommentDto> expectedPagingDto = ListResponse.from(commentDtos);
        given(commentRepository.findCommentList(anyLong(), any()))
                .willReturn(commentDtos);

        // when
        ListResponse<CommentDto> commentPagingDto = commentService.getCommentList(1L, PageRequest.of(0, 10));

        // then
        verify(commentRepository, times(1)).findCommentList(anyLong(), any());
        assertEquals(commentPagingDto.getContent(), expectedPagingDto.getContent());
        assertEquals(commentPagingDto.getTotalPages(), expectedPagingDto.getTotalPages());
        assertEquals(commentPagingDto.isHasNextPage(), expectedPagingDto.isHasNextPage());
    }

    @Test
    @DisplayName("내 댓글 조회하기 성공")
    void success_getMyCommentList(){
        //given
        MyPageCommentDto myPageCommentDto = new MyPageCommentDto() {
            @Override
            public Long getRecipeId() {
                return 1L;
            }

            @Override
            public String getMessage() {
                return "이건 최고의 꿀조합이 분명합니다!";
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.of(2023, 10, 1,
                        9, 0, 0);
            }
        };
        given(commentRepository.findMyCommentList(anyLong(), any()))
                .willReturn(new PageImpl<>(List.of(myPageCommentDto)));

        //when
        ListResponse<MyPageCommentDto> response = commentService
                .getMyCommentList(1L, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(myPageCommentDto.getRecipeId(), response.getContent().get(0).getRecipeId()),
                () -> assertEquals(myPageCommentDto.getMessage(), response.getContent().get(0).getMessage()),
                () -> assertEquals(myPageCommentDto.getCreatedAt(), response.getContent().get(0).getCreatedAt())
        );

    }

    private static Comment getExpectedComment() {
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.toString()))
                .isDeleted(false)
                .password("123456")
                .build();
        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("제육볶음")
                        .build())
                .build();
        return Comment.builder()
                .member(member)
                .recipe(recipe)
                .isDeleted(false)
                .message("너무 맛있는 제육볶음인 것 같아요!")
                .build();
    }

    private static Comment getExpectedUpdatedComment() {
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.toString()))
                .isDeleted(false)
                .password("123456")
                .build();
        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("제육볶음")
                        .build())
                .build();
        return Comment.builder()
                .member(member)
                .recipe(recipe)
                .isDeleted(false)
                .message("제육볶음 존맛탱!")
                .build();
    }

    private static Comment getExpectedDeleteComment() {
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.toString()))
                .isDeleted(false)
                .password("123456")
                .build();
        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("제육볶음")
                        .build())
                .build();
        return Comment.builder()
                .member(member)
                .recipe(recipe)
                .isDeleted(true)
                .message("제육볶음 존맛탱!")
                .build();
    }

    private static PageImpl<CommentDto> getCommentDtoPages() {

        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                CommentDto.builder()
                        .message("맛있어요")
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .build(),
                CommentDto.builder()
                        .message("진짜 맛있어요")
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .build()
        )));

    }


}