package com.zerobase.foodlier.global.comment.facade.comment;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.comment.comment.exception.CommentException;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.zerobase.foodlier.module.comment.comment.exception.CommentErrorCode.NO_SUCH_COMMENT;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentFacadeTest {

    @Mock
    private MemberService memberService;

    @Mock
    private RecipeService recipeService;

    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentFacade commentFacade;

    @Test
    @DisplayName("댓글 작성 성공")
    void success_create_comment() {

        // given

        Member member = getMember();

        Recipe futureRecipe = getUpdatedRecipe();

        given(memberService.findByEmail(anyString()))
                .willReturn(member);
        given(recipeService.plusCommentCount(any()))
                .willReturn(futureRecipe);
        given(commentService.createComment(any()))
                .willReturn(Comment.builder()
                        .build());
        // when

        commentFacade.createComment(1L, "test@gmail.com", "맛있어요");

        // then
        verify(memberService, times(1)).findByEmail(anyString());
        verify(recipeService, times(1)).plusCommentCount(any());
        verify(commentService, times(1)).createComment(any());

    }

    @Test
    @DisplayName("댓글 작성 실패 - 회원을 찾을 수 없는 경우")
    void fail_create_comment_NO_SUCH_MEMBER() {

        // given
        given(memberService.findByEmail(anyString()))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        // when

        MemberException memberException = assertThrows(MemberException.class, () -> commentFacade.createComment(1L,
                "test@gmail.com", "맛있어요"));
        // then
        verify(memberService, times(1)).findByEmail(anyString());
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
        assertEquals(MEMBER_NOT_FOUND.getDescription(), memberException.getDescription());


    }

    @Test
    @DisplayName("댓글 작성 실패 - 레시피를 찾을 수 없는 경우")
    void fail_create_comment_NO_SUCH_RECIPE() {

        // given

        Member member = getMember();

        given(memberService.findByEmail(anyString()))
                .willReturn(member);
        given(recipeService.plusCommentCount(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        // when

        RecipeException recipeException = assertThrows(RecipeException.class, () -> commentFacade.createComment(1L,
                "test@gmail.com", "맛있어요"));

        // then
        verify(memberService, times(1)).findByEmail(anyString());
        verify(recipeService, times(1)).plusCommentCount(any());
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE.getDescription(), recipeException.getDescription());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void success_delete_comment() {

        // given

        doNothing().when(recipeService).minusCommentCount(any());
        doNothing().when(commentService).deleteComment(any(), any());

        // when

        commentFacade.deleteComment(1L, 1L, 1L);

        // then
        verify(recipeService, times(1)).minusCommentCount(any());
        verify(commentService, times(1)).deleteComment(any(), any());

    }

    @Test
    @DisplayName("댓글 삭제 실패 - 레시피를 찾을 수 없는 경우")
    void fail_delete_comment_NO_SUCH_MEMBER() {

        // given
        doThrow(new RecipeException(NO_SUCH_RECIPE))
                .when(recipeService).minusCommentCount(any());

        // when

        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> commentFacade.deleteComment(1L, 1L, 1L));
        // then
        verify(recipeService, times(1)).minusCommentCount(any());
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE.getDescription(), recipeException.getDescription());

    }

    @Test
    @DisplayName("댓글 작성 실패 - 댓글을 찾을 수 없는 경우")
    void fail_delete_comment_NO_SUCH_RECIPE() {

        // given

        doNothing().when(recipeService).minusCommentCount(any());
        doThrow(new CommentException(NO_SUCH_COMMENT)).when(commentService).deleteComment(any(), any());

        // when

        CommentException commentException = assertThrows(CommentException.class, () -> commentFacade.deleteComment(1L, 1L,1L));

        // then
        verify(recipeService, times(1)).minusCommentCount(any());
        verify(commentService, times(1)).deleteComment(any(), any());
        assertEquals(NO_SUCH_COMMENT, commentException.getErrorCode());
        assertEquals(NO_SUCH_COMMENT.getDescription(), commentException.getDescription());
    }


    private static Recipe getUpdatedRecipe() {
        return Recipe.builder()
                .id(1L)
                .commentCount(2)
                .summary(Summary.builder()
                        .title("제육")
                        .content("제육 맛있다.")
                        .build())
                .build();
    }

    private static Member getMember(){
        return Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("test@gmail.com")
                .build();
    }
}