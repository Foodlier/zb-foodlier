package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.exception.HeartException;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.foodlier.module.heart.exception.HeartErrorCode.HEART_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HeartServiceTest {
    @Mock
    private HeartRepository heartRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RecipeSearchRepository recipeSearchRepository;

    @InjectMocks
    private HeartService heartService;

    @Test
    @DisplayName("좋아요 생성 성공")
    void success_createHeart_createNewHeart() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .heartCount(0)
                .build();
        Member member = Member.builder()
                .id(1L)
                .build();
        Heart heart = Heart.builder()
                .recipe(recipe)
                .member(member)
                .build();
        RecipeDocument recipeDocument = RecipeDocument.builder()
                .id(1L)
                .numberOfHeart(0L)
                .build();
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(recipe));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));
        given(heartRepository.existsByRecipeAndMember(any(), any()))
                .willReturn(false);
        given(recipeSearchRepository.findById(anyLong()))
                .willReturn(Optional.of(recipeDocument));
        given(heartRepository.save(any()))
                .willReturn(heart);

        //when
        heartService.createHeart(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);
        ArgumentCaptor<RecipeDocument> recipeDocumentArgumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);
        verify(recipeRepository, times(1))
                .findById(1L);
        verify(memberRepository, times(1))
                .findById(1L);
        verify(heartRepository, times(1))
                .save(captor.capture());
        verify(recipeSearchRepository, times(1)).findById(anyLong());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentArgumentCaptor.capture());
        Heart value = captor.getValue();
        RecipeDocument recipeDocumentCaptured = recipeDocumentArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(1, value.getRecipe().getHeartCount()),
                () -> assertEquals(1L, value.getRecipe().getId()),
                () -> assertEquals(1L, value.getMember().getId()),
                () -> assertEquals(1L, recipeDocumentCaptured.getId()),
                () -> assertEquals(1, recipeDocumentCaptured.getNumberOfHeart())
        );

    }

    @Test
    @DisplayName("좋아요 생성 실패 - 레시피를 찾을 수 없음")
    void fail_createHeart_noSuchRecipe() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> heartService.createHeart(MemberAuthDto.builder()
                        .id(1L).build(), 1L));


        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 생성 실패 - 멤버를 찾을 수 없음")
    void fail_createHeart_memberNotFound() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .heartCount(0)
                .build();

        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(recipe));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> heartService.createHeart(MemberAuthDto.builder()
                        .id(1L).build(), 1L));


        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void success_deleteHeart() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .heartCount(1)
                .build();
        Member member = Member.builder()
                .id(1L)
                .build();
        Heart heart = Heart.builder()
                .recipe(recipe)
                .member(member)
                .build();
        RecipeDocument recipeDocument = RecipeDocument.builder()
                .id(1L)
                .numberOfHeart(1L)
                .build();
        RecipeDocument updatedRecipeDocument = RecipeDocument.builder()
                .id(1L)
                .numberOfHeart(0L)
                .build();
        given(heartRepository.findHeart(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(heart));
        given(recipeSearchRepository.findById(anyLong()))
                .willReturn(Optional.of(recipeDocument));
        given(recipeSearchRepository.save(any()))
                .willReturn(updatedRecipeDocument);
        //when
        ArgumentCaptor<RecipeDocument> recipeDocumentArgumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);
        heartService.deleteHeart(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        verify(heartRepository, times(1))
                .delete(any());
        verify(recipeSearchRepository, times(1)).findById(anyLong());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentArgumentCaptor.capture());

        RecipeDocument capturedRecipeDocument = recipeDocumentArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(0, capturedRecipeDocument.getNumberOfHeart()),
                () -> assertEquals(1L, capturedRecipeDocument.getId())
        );

    }

    @Test
    @DisplayName("좋아요 삭제 실패 - 좋아요를 찾을 수 없음")
    void fail_deleteHeart_heartNotFound() {
        //given
        given(heartRepository.findHeart(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        //when
        HeartException heartException = assertThrows(HeartException.class,
                () -> heartService.deleteHeart(MemberAuthDto.builder()
                        .id(1L).build(), 1L));

        //then
        assertEquals(HEART_NOT_FOUND, heartException.getErrorCode());
    }
}