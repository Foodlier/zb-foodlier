package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.EASY;
import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.HARD;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeSearchRepository recipeSearchRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    @DisplayName("레시피 저장 성공")
    void success_create_recipe() {

        //given
        Member member = getMember();
        RecipeDtoRequest recipeDtoRequest = getRecipeDtoRequest();

        RecipeDocument expectedRecipeDocument = getRecipeDocument(member, recipeDtoRequest);

        given(recipeRepository.save(any()))
                .willReturn(getExpectedRecipe(member));

        given(recipeSearchRepository.save(any(RecipeDocument.class)))
                .willReturn(expectedRecipeDocument);

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);
        ArgumentCaptor<RecipeDocument> recipeDocumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);

        //when
        recipeService.createRecipe(member, recipeDtoRequest);

        //then
        verify(recipeRepository, times(1)).save(recipeCaptor.capture());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentCaptor.capture());
        Recipe recipe = recipeCaptor.getValue();
        RecipeDocument actualRecipeDocument = recipeDocumentCaptor.getValue();

        assertAll(
                () -> assertEquals("title", recipe.getSummary().getTitle()),
                () -> assertEquals("content", recipe.getSummary().getContent()),
                () -> assertEquals("image.jpg", recipe.getMainImageUrl()),
                () -> assertEquals(30, recipe.getExpectedTime()),
                () -> assertEquals(HARD, recipe.getDifficulty()),
                () -> assertEquals("nickname", recipe.getMember().getNickname()),
                () -> assertEquals("email@email.com", recipe.getMember().getEmail())
        );
        assertAll(
                () -> assertEquals(expectedRecipeDocument.getNumberOfHeart(),
                        actualRecipeDocument.getNumberOfHeart()),
                () -> assertEquals(expectedRecipeDocument.getTitle(),
                        actualRecipeDocument.getTitle()),
                () -> assertEquals(expectedRecipeDocument.getWriter(),
                        actualRecipeDocument.getWriter()),
                () -> assertEquals(expectedRecipeDocument.getIngredients(),
                        actualRecipeDocument.getIngredients()),
                () -> assertEquals(expectedRecipeDocument.getNumberOfComment(),
                        actualRecipeDocument.getNumberOfComment())
        );

    }

    @Test
    @DisplayName("레시피 수정 성공 - 일부수정")
    void success_part_update_recipe() {
        //given
        Long id = 1L;
        Member member = getMember();

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

        RecipeDocument expectedFindingRecipeDocument = getRecipeDocument(member);

        RecipeDocument expectedUpdateRecipeDocument = getRecipeDocument(member, updateRecipe);

        given(recipeRepository.findById(any()))
                .willReturn(Optional.ofNullable(getExpectedRecipe(member)));

        given(recipeSearchRepository.findById(anyLong()))
                .willReturn(Optional.of(expectedFindingRecipeDocument));
        given(recipeSearchRepository.save(any()))
                .willReturn(expectedUpdateRecipeDocument);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        ArgumentCaptor<RecipeDocument> recipeDocumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);
        //when
        recipeService.updateRecipe(updateRecipe, id);

        //then
        verify(recipeRepository, times(1)).save(captor.capture());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentCaptor.capture());
        Recipe recipe = captor.getValue();
        RecipeDocument recipeDocument = recipeDocumentCaptor.getValue();

        assertAll(
                () -> assertNull(recipe.getSummary().getTitle()),
                () -> assertEquals("updateContent", recipe.getSummary().getContent()),
                () -> assertEquals("updateImage.jpg", recipe.getMainImageUrl()),
                () -> assertEquals(31, recipe.getExpectedTime()),
                () -> assertEquals(EASY, recipe.getDifficulty()),
                () -> assertEquals("nickname", recipe.getMember().getNickname()),
                () -> assertEquals("email@email.com", recipe.getMember().getEmail())
        );

        assertAll(
                () -> assertEquals(expectedUpdateRecipeDocument.getNumberOfComment(), recipeDocument.getNumberOfComment()),
                () -> assertEquals(expectedUpdateRecipeDocument.getWriter(), recipeDocument.getWriter()),
                () -> assertEquals(expectedUpdateRecipeDocument.getIngredients(), recipeDocument.getIngredients()),
                () -> assertEquals(expectedUpdateRecipeDocument.getNumberOfHeart(), recipeDocument.getNumberOfHeart()),
                () -> assertEquals(expectedUpdateRecipeDocument.getTitle(), recipeDocument.getTitle())
        );

    }

    @Test
    @DisplayName("레시피 수정 성공 - 전체수정")
    void success_full_update_recipe() {
        //given
        Long id = 1L;
        Member member = getMember();

        RecipeDtoRequest updateRecipe = getUpdateRecipeDtoRequest();

        RecipeDocument expectedFindingRecipeDocument = getRecipeDocument(member);

        RecipeDocument expectedUpdateRecipeDocument = getRecipeDocument(member, updateRecipe);

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(getExpectedRecipe(member)));

        given(recipeSearchRepository.findById(any()))
                .willReturn(Optional.of(expectedFindingRecipeDocument));
        given(recipeSearchRepository.save(any()))
                .willReturn(expectedUpdateRecipeDocument);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        ArgumentCaptor<RecipeDocument> recipeDocumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);

        //when
        recipeService.updateRecipe(updateRecipe, id);

        //then
        verify(recipeRepository, times(1)).save(captor.capture());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentCaptor.capture());
        Recipe recipe = captor.getValue();
        RecipeDocument recipeDocument = recipeDocumentCaptor.getValue();

        assertAll(
                () -> assertEquals("updateTitle", recipe.getSummary().getTitle()),
                () -> assertEquals("updateContent", recipe.getSummary().getContent()),
                () -> assertEquals("updateImage.jpg", recipe.getMainImageUrl()),
                () -> assertEquals(31, recipe.getExpectedTime()),
                () -> assertEquals(EASY, recipe.getDifficulty()),
                () -> assertEquals("nickname", recipe.getMember().getNickname()),
                () -> assertEquals("email@email.com", recipe.getMember().getEmail())
        );


        assertAll(
                () -> assertEquals(expectedUpdateRecipeDocument.getNumberOfComment(), recipeDocument.getNumberOfComment()),
                () -> assertEquals(expectedUpdateRecipeDocument.getWriter(), recipeDocument.getWriter()),
                () -> assertEquals(expectedUpdateRecipeDocument.getIngredients(), recipeDocument.getIngredients()),
                () -> assertEquals(expectedUpdateRecipeDocument.getNumberOfHeart(), recipeDocument.getNumberOfHeart()),
                () -> assertEquals(expectedUpdateRecipeDocument.getTitle(), recipeDocument.getTitle())
        );


    }

    @Test
    @DisplayName("레시피 수정 실패 - 레시피 없음")
    void fail_update_recipe_no_such_recipe() {
        //given
        Long id = 1L;
        RecipeDtoRequest recipeDtoRequest = getRecipeDtoRequest();
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
        Member member = getMember();
        given(recipeRepository.findById(id))
                .willReturn(Optional.of(getExpectedRecipe(member)));

        //when
        Recipe recipe = recipeService.getRecipe(id);

        //then
        verify(recipeRepository, times(1)).findById(id);

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
        Member member = getMember();
        given(recipeRepository.findById(id))
                .willReturn(Optional.of(getExpectedRecipe(member)));

        //when
        RecipeDtoResponse recipeDtoResponse = recipeService.getRecipeDetail(id);

        //then
        verify(recipeRepository, times(1)).findById(id);

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
        Member member = getMember();

        given(recipeRepository.findById(id))
                .willReturn(Optional.of(Recipe.builder()
                        .id(id)
                        .mainImageUrl("image.jpg")
                        .recipeDetailList(new ArrayList<>(List.of(new RecipeDetail())))
                        .member(member)
                        .build()));
        given(recipeSearchRepository.existsById(any()))
                .willReturn(true);
        doNothing().when(recipeSearchRepository).deleteById(any());
        //when
        ImageUrlDto imageUrlDto = recipeService.getBeforeImageUrl(id);
        recipeService.deleteRecipe(id);

        //then
        verify(recipeRepository, times(1)).deleteById(id);
        verify(recipeSearchRepository, times(1)).deleteById(any());

        assertEquals("image.jpg", imageUrlDto.getMainImageUrl());
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

    @Test
    @DisplayName("레시피 제목으로 유사한 검색 성공")
    void success_get_recipe_list_by_recipe_title() {

        // given
        Member member = getMember();
        Summary summary = Summary.builder()
                .title("김치제육볶음")
                .content("맛있는 김치제육볶음을 만들어보자!")
                .build();
        Summary otherSummary = Summary.builder()
                .title("치즈제육볶음")
                .content("맛있는 치즈제육볶음을 만들어보자!")
                .build();

        Recipe recipe = Recipe.builder()
                .id(1L)
                .summary(summary)
                .build();
        Recipe otherRecipe = Recipe.builder()
                .id(2L)
                .summary(otherSummary)
                .build();
        RecipeDocument recipeDocument = getDocument(member, "김치제육볶음");
        RecipeDocument otherRecipeDocument = getDocument(member, "치즈제육볶음");

        given(recipeSearchRepository.findByTitle(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(recipeDocument, otherRecipeDocument))));
        when(recipeRepository.findById(any()))
                .thenReturn(Optional.of(recipe), Optional.of(otherRecipe));

        // when
        List<Recipe> recipeList = recipeService.getRecipeByTitle("제육볶음", PageRequest.of(0, 10));
        List<String> titleList = recipeList.stream()
                .map(Recipe::getSummary)
                .map(Summary::getTitle)
                .collect(Collectors.toList());
        // then
        verify(recipeSearchRepository, times(1)).findByTitle(any(), any());
        verify(recipeRepository, times(2)).findById(any());

        assertEquals(titleList.size(), 2);
        assertEquals(titleList.get(0), recipeDocument.getTitle());
        assertEquals(titleList.get(1), otherRecipeDocument.getTitle());
    }

    @Test
    @DisplayName("레시피 제목으로 유사한 레시피 검색 실패 - 검색 객체만 존재하는 경우")
    void fail_find_recipes() {

        // given
        Member member = getMember();
        RecipeDocument recipeDocument = getDocument(member, "김치제육볶음");
        RecipeDocument otherRecipeDocument = getDocument(member, "치즈제육볶음");

        given(recipeSearchRepository.findByTitle(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(recipeDocument, otherRecipeDocument))));
        given(recipeRepository.findById(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        // when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipeByTitle("제육볶음",
                        PageRequest.of(0, 10)));

        // then
        assertEquals(recipeException.getErrorCode(), NO_SUCH_RECIPE);
        assertEquals(recipeException.getDescription(), NO_SUCH_RECIPE.getDescription());

    }

    @Test
    @DisplayName("레시피 제목으로 검색 성공 - 유사한 제목을 못찾은 경우")
    void success_find_recipe_list_no_match() {

        // given
        given(recipeSearchRepository.findByTitle(any(), any()))
                .willReturn(Page.empty());

        // when
        List<Recipe> recipeList = recipeService.getRecipeByTitle("숙주미나리볶음",
                PageRequest.of(0, 10));

        // then
        verify(recipeSearchRepository, times(1)).findByTitle(any(), any());

        assertEquals(recipeList.size(), 0);

    }

    // ===============테스트 객체 정의 메소드 ===================
    private static Member getMember() {
        return Member.builder()
                .id(1L)
                .nickname("nickname")
                .email("email@email.com")
                .build();
    }

    private static Recipe getExpectedRecipe(Member member) {
        return Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title")
                        .content("content")
                        .build())
                .mainImageUrl("image.jpg")
                .expectedTime(30)
                .difficulty(HARD)
                .member(member)
                .build();
    }

    private static RecipeDocument getDocument(Member member, String title) {
        return RecipeDocument.builder()
                .id(1L)
                .title(title)
                .writer(member.getNickname())
                .numberOfHeart(0L)
                .numberOfComment(0L)
                .build();
    }

    private static RecipeDtoRequest getRecipeDtoRequest() {
        return RecipeDtoRequest.builder()
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
    }

    private static RecipeDtoRequest getUpdateRecipeDtoRequest() {
        return RecipeDtoRequest.builder()
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
    }

    private static RecipeDocument getRecipeDocument(Member member) {
        return RecipeDocument
                .builder()
                .title("title")
                .writer(member.getNickname())
                .numberOfComment(0L)
                .ingredients(List.of("양파", "가지", "오이"))
                .numberOfComment(0L)
                .build();
    }

    private static RecipeDocument getRecipeDocument(Member member, RecipeDtoRequest updateRecipe) {
        return RecipeDocument
                .builder()
                .title(updateRecipe.getTitle())
                .writer(member.getNickname())
                .numberOfComment(0L)
                .ingredients(updateRecipe.getRecipeIngredientDtoList().stream()
                        .map(RecipeIngredientDto::getName).collect(Collectors.toList()))
                .numberOfComment(0L)
                .build();
    }
}