package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.exception.HeartException;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.foodlier.module.heart.exception.HeartErrorCode.*;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HeartServiceImplTest {
    @Mock
    private HeartRepository heartRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private HeartServiceImpl heartService;

    @Test
    @DisplayName("좋아요 누르기 성공 -" +
            " 해당 레시피에 좋아요를 한번도 안눌렀을 경우 새로 생성")
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
                .heartOrNot(true)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(recipe));
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));
        given(heartRepository.save(any()))
                .willReturn(heart);

        //when
        heartService.createHeart(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);
        verify(heartRepository, times(1))
                .findByRecipeIdAndMemberId(1L, 1L);
        verify(recipeRepository, times(1))
                .findById(1L);
        verify(memberRepository, times(1))
                .findById(1L);
        verify(heartRepository, times(1))
                .save(captor.capture());

        Heart value = captor.getValue();
        assertAll(
                () -> assertTrue(value.isHeartOrNot()),
                () -> assertEquals(1, value.getRecipe().getHeartCount()),
                () -> assertEquals(1L, value.getRecipe().getId()),
                () -> assertEquals(1L, value.getMember().getId())
        );
    }

    @Test
    @DisplayName("좋아요 누르기 성공 - 좋아요를 취소했다가 다시 좋아요를 한 경우")
    void success_createHeart_heartOrNotTrue() {
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
                .heartOrNot(false)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(heart));

        //when
        heartService.createHeart(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);
        verify(heartRepository, times(1))
                .save(captor.capture());

        Heart value = captor.getValue();
        assertAll(
                () -> assertTrue(value.isHeartOrNot()),
                () -> assertEquals(1, value.getRecipe().getHeartCount()),
                () -> assertEquals(1L, value.getRecipe().getId()),
                () -> assertEquals(1L, value.getMember().getId())
        );
    }

    @Test
    @DisplayName("좋아요 누르기 실패 - 레시피를 찾을 수 없음")
    void fail_createHeart_noSuchRecipe() {
        //given
        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.empty());
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
    @DisplayName("좋아요 누르기 실패 - 멤버를 찾을 수 없음")
    void fail_createHeart_memberNotFound() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .heartCount(0)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.empty());
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
    @DisplayName("좋아요 누르기 실패 - 이미 눌러진 좋아요")
    void fail_createHeart_alreadyHeart() {
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
                .heartOrNot(true)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(heart));

        //when
        HeartException heartException = assertThrows(HeartException.class,
                () -> heartService.createHeart(MemberAuthDto.builder()
                        .id(1L).build(), 1L));


        //then
        assertEquals(ALREADY_HEART, heartException.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void success_heartCancel() {
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
                .heartOrNot(true)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(heart));

        //when
        heartService.heartCancel(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        ArgumentCaptor<Heart> captor = ArgumentCaptor.forClass(Heart.class);
        verify(heartRepository, times(1))
                .save(captor.capture());

        Heart value = captor.getValue();
        assertAll(
                () -> assertFalse(value.isHeartOrNot()),
                () -> assertEquals(0, value.getRecipe().getHeartCount()),
                () -> assertEquals(1L, value.getRecipe().getId()),
                () -> assertEquals(1L, value.getMember().getId())
        );
    }

    @Test
    @DisplayName("좋아요 취소 실패 - 좋아요를 찾을 수 없음")
    void fail_heartCancel_heartNotFound() {
        //given
        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        //when
        HeartException heartException = assertThrows(HeartException.class,
                () -> heartService.heartCancel(MemberAuthDto.builder()
                        .id(1L).build(), 1L));

        //then
        assertEquals(HEART_NOT_FOUND, heartException.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 취소 실패 - 이미 취소된 좋아요")
    void fail_heartCancel_alreadyHeartCancel() {
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
                .heartOrNot(false)
                .build();

        given(heartRepository.findByRecipeIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(heart));

        //when
        HeartException heartException = assertThrows(HeartException.class,
                () -> heartService.heartCancel(MemberAuthDto.builder()
                        .id(1L).build(), 1L));

        //then
        assertEquals(ALREADY_HEART_CANCEL, heartException.getErrorCode());
    }
}