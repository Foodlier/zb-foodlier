package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.*;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.EASY;
import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.HARD;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    @DisplayName("레시피 저장 성공")
    void success_create_recipe() {
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
        RecipeDtoRequest recipeDtoRequest = RecipeDtoRequest.builder()
                .title("title")
                .content("content")
                .mainImageUrl("image.jpg")
                .recipeDetailDtoList(
                        new ArrayList<>(List.of(new RecipeDetailDto())))
                .recipeIngredientDtoList(
                        new ArrayList<>(List.of(new RecipeIngredientDto())))
                .expectedTime(30)
                .difficulty(HARD)
                .build();
        given(recipeRepository.save(any()))
                .willReturn(Recipe.builder()
                        .id(1L)
                        .summary(Summary.builder()
                                .title("title")
                                .content("content")
                                .build())
                        .mainImageUrl("image.jpg")
                        .expectedTime(30)
                        .difficulty(HARD)
                        .member(member)
                        .build());

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //when
        recipeService.createRecipe(member, recipeDtoRequest);

        //then
        verify(recipeRepository, times(1)).save(captor.capture());
        Recipe recipe = captor.getValue();

        assertEquals("title", recipe.getSummary().getTitle());
        assertEquals("content", recipe.getSummary().getContent());
        assertEquals("image.jpg", recipe.getMainImageUrl());
        assertEquals(30, recipe.getExpectedTime());
        assertEquals(HARD, recipe.getDifficulty());
        assertEquals("nickname", recipe.getMember().getNickname());
        assertEquals("email@email.com", recipe.getMember().getEmail());
    }

    @Test
    @DisplayName("레시피 수정 성공 - 일부수정")
    void success_part_update_recipe() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
        RecipeDtoRequest updateRecipe = RecipeDtoRequest.builder()
                .title(null)
                .content("updateContent")
                .mainImageUrl("updateImage.jpg")
                .recipeDetailDtoList(
                        new ArrayList<>(List.of(new RecipeDetailDto())))
                .recipeIngredientDtoList(
                        new ArrayList<>(List.of(new RecipeIngredientDto())))
                .expectedTime(31)
                .difficulty(EASY)
                .build();
        given(recipeRepository.findById(id))
                .willReturn(Optional.ofNullable(Recipe.builder()
                        .id(1L)
                        .summary(Summary.builder()
                                .title("title")
                                .content("content")
                                .build())
                        .mainImageUrl("image.jpg")
                        .expectedTime(30)
                        .difficulty(HARD)
                        .member(member)
                        .build()));

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //when
        recipeService.updateRecipe(updateRecipe, id);

        //then
        verify(recipeRepository, times(1)).save(captor.capture());
        Recipe recipe = captor.getValue();

        assertNull(recipe.getSummary().getTitle());
        assertEquals("updateContent", recipe.getSummary().getContent());
        assertEquals("updateImage.jpg", recipe.getMainImageUrl());
        assertEquals(31, recipe.getExpectedTime());
        assertEquals(EASY, recipe.getDifficulty());
        assertEquals("nickname", recipe.getMember().getNickname());
        assertEquals("email@email.com", recipe.getMember().getEmail());
    }

    @Test
    @DisplayName("레시피 수정 성공 - 전체수정")
    void success_full_update_recipe() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
        RecipeDtoRequest updateRecipe = RecipeDtoRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .mainImageUrl("updateImage.jpg")
                .recipeDetailDtoList(
                        new ArrayList<>(List.of(new RecipeDetailDto())))
                .recipeIngredientDtoList(
                        new ArrayList<>(List.of(new RecipeIngredientDto())))
                .expectedTime(31)
                .difficulty(EASY)
                .build();
        given(recipeRepository.findById(id))
                .willReturn(Optional.ofNullable(Recipe.builder()
                        .id(1L)
                        .summary(Summary.builder()
                                .title("title")
                                .content("content")
                                .build())
                        .mainImageUrl("image.jpg")
                        .expectedTime(30)
                        .difficulty(HARD)
                        .member(member)
                        .build()));

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        //when
        recipeService.updateRecipe(updateRecipe, id);

        //then
        verify(recipeRepository, times(1)).save(captor.capture());
        Recipe recipe = captor.getValue();

        assertEquals("updateTitle", recipe.getSummary().getTitle());
        assertEquals("updateContent", recipe.getSummary().getContent());
        assertEquals("updateImage.jpg", recipe.getMainImageUrl());
        assertEquals(31, recipe.getExpectedTime());
        assertEquals(EASY, recipe.getDifficulty());
        assertEquals("nickname", recipe.getMember().getNickname());
        assertEquals("email@email.com", recipe.getMember().getEmail());
    }

    @Test
    @DisplayName("레시피 수정 실패 - 레시피 없음")
    void fail_update_recipe_no_such_recipe() {
        //given
        Long id = 1L;
        RecipeDtoRequest recipeDtoRequest = RecipeDtoRequest.builder()
                .title("title")
                .content("content")
                .mainImageUrl("image.jpg")
                .recipeDetailDtoList(
                        new ArrayList<>(List.of(new RecipeDetailDto())))
                .recipeIngredientDtoList(
                        new ArrayList<>(List.of(new RecipeIngredientDto())))
                .expectedTime(30)
                .difficulty(HARD)
                .build();
        given(recipeRepository.findById(id)).willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.updateRecipe(recipeDtoRequest, id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("아이디로 레시피 찾기 성공")
    void success_get_recipe() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
        given(recipeRepository.findById(id))
                .willReturn(Optional.of(Recipe.builder()
                        .id(1L)
                        .summary(Summary.builder()
                                .title("title")
                                .content("content")
                                .build())
                        .mainImageUrl("image.jpg")
                        .expectedTime(30)
                        .difficulty(HARD)
                        .member(member)
                        .build()));

        //when
        Recipe recipe = recipeService.getRecipe(id);

        //then
        verify(recipeRepository, times(1)).findById(anyLong());

        assertEquals("title", recipe.getSummary().getTitle());
        assertEquals("content", recipe.getSummary().getContent());
        assertEquals("image.jpg", recipe.getMainImageUrl());
        assertEquals(30, recipe.getExpectedTime());
        assertEquals(HARD, recipe.getDifficulty());
        assertEquals("nickname", recipe.getMember().getNickname());
        assertEquals("email@email.com", recipe.getMember().getEmail());
    }

    @Test
    @DisplayName("아이디로 레시피 찾기 실패 - 레시피 없음")
    void fail_get_recipe_no_such_recipe() {
        //given
        Long id = 1L;
        given(recipeRepository.findById(id)).willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipe(id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("레시피 상세보기 성공")
    void success_get_recipe_detail() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
        given(recipeRepository.findById(id))
                .willReturn(Optional.of(Recipe.builder()
                        .id(1L)
                        .summary(Summary.builder()
                                .title("title")
                                .content("content")
                                .build())
                        .mainImageUrl("image.jpg")
                        .expectedTime(30)
                        .difficulty(HARD)
                        .member(member)
                        .build()));

        //when
        RecipeDtoResponse recipeDtoResponse = recipeService.getRecipeDetail(id);

        //then
        verify(recipeRepository, times(1)).findById(anyLong());

        assertEquals("title", recipeDtoResponse.getTitle());
        assertEquals("content", recipeDtoResponse.getContent());
        assertEquals("image.jpg", recipeDtoResponse.getMainImageUrl());
        assertEquals(30, recipeDtoResponse.getExpectedTime());
        assertEquals(HARD, recipeDtoResponse.getDifficulty());
        assertEquals("nickname", recipeDtoResponse.getNickname());
    }

    @Test
    @DisplayName("레시피 상세보기 실패 - 레시피 없음")
    void fail_get_recipe_detail_no_such_recipe() {
        //given
        Long id = 1L;
        given(recipeRepository.findById(id)).willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipeDetail(id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("레시피 삭제하기 성공")
    void success_delete_recipe() {
        //given
        Long id = 1L;
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();

        given(recipeRepository.findById(id))
                .willReturn(Optional.of(Recipe.builder()
                        .id(id)
                        .mainImageUrl("image.jpg")
                        .recipeDetailList(new ArrayList<>(List.of(new RecipeDetail())))
                        .member(member)
                        .build()));

        //when
        ImageUrlDto imageUrlDto = recipeService.deleteRecipe(id);

        //then
        verify(recipeRepository, times(1)).deleteById(id);

        assertEquals("image.jpg", imageUrlDto.getMainImageUrl());
        assertEquals(RecipeDetail.class, imageUrlDto.getRecipeDetailList().get(0).getClass());
    }

    @Test
    @DisplayName("레시피 삭제하기 실패 - 레시피 없음")
    void fail_delete_recipe_no_such_recipe() {
        //given
        Long id = 1L;
        given(recipeRepository.findById(id)).willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipeDetail(id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }
}