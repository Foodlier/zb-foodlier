package com.zerobase.foodlier.module.recipe.service.recipe;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import com.zerobase.foodlier.module.recipe.service.recipe.data.DummyDataGenerator;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.EASY;
import static com.zerobase.foodlier.module.recipe.domain.type.Difficulty.HARD;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.*;
import static com.zerobase.foodlier.module.recipe.type.OrderType.*;
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
    private HeartRepository heartRepository;

    @Mock
    private MemberRepository memberRepository;

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

        RecipeDocument expectedRecipeDocument = getRecipeUpdatedDocument(member, recipeDtoRequest);

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

        RecipeDocument expectedUpdateRecipeDocument = getRecipeUpdatedDocument(member, updateRecipe);

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

        RecipeDocument expectedUpdateRecipeDocument = getRecipeUpdatedDocument(member, updateRecipe);

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
        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .id(2L)
                        .build()));
        given(heartRepository.existsByRecipeAndMember(any(), any()))
                .willReturn(true);

        //when
        RecipeDtoResponse recipeDtoResponse = recipeService
                .getRecipeDetail(MemberAuthDto.builder()
                        .id(2L)
                        .build(), id);

        //then
        verify(recipeRepository, times(1)).findById(id);

        assertEquals("title", recipeDtoResponse.getTitle());
        assertEquals("content", recipeDtoResponse.getContent());
        assertEquals("image.jpg", recipeDtoResponse.getMainImageUrl());
        assertEquals(30, recipeDtoResponse.getExpectedTime());
        assertEquals(HARD, recipeDtoResponse.getDifficulty());
        assertEquals("nickname", recipeDtoResponse.getNickname());
        assertTrue(recipeDtoResponse.isHeart());
    }


    @Test
    @DisplayName("레시피 상세보기 실패 - 레시피 없음")
    void fail_get_recipe_detail_no_such_recipe() {
        //given
        Long id = 1L;
        given(recipeRepository.findById(id)).willReturn(Optional.empty());

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipeDetail(MemberAuthDto.builder()
                        .id(1L)
                        .build(), id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("레시피 상세보기 실패 - 회원을 찾을 수 없음")
    void fail_get_recipe_detail_memberNotFound() {
        //given
        Long id = 1L;
        Member member = getMember();

        given(recipeRepository.findById(id))
                .willReturn(Optional.of(getExpectedRecipe(member)));
        given(memberRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> recipeService.getRecipeDetail(MemberAuthDto.builder()
                        .id(1L)
                        .build(), id));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
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
                () -> recipeService.deleteRecipe(id));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Nested
    @DisplayName("레시피 검색 테스트")
    class RecipeSearchTest {

        @Test
        @DisplayName("레시피 검색 실패 - 검색 요청자가 회원이 아닌 경우")
        void fail_search_recipe_member_not_found() {

            // given
            RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.TITLE,
                    CREATED_AT, "제육");
            given(memberRepository.findById(any()))
                    .willThrow(new MemberException(MEMBER_NOT_FOUND));

            // when
            MemberException memberException = assertThrows(MemberException.class,
                    () -> recipeService.getRecipeList(recipeSearchRequest));


            // then
            assertEquals(memberException.getErrorCode(), MEMBER_NOT_FOUND);
            assertEquals(memberException.getDescription(), MEMBER_NOT_FOUND.getDescription());

        }

        @Nested
        @DisplayName("제목으로 레시피 검색하기")
        class RecipeSearchByTitle {
            @Test
            @DisplayName("레시피 검색 성공 - 최근 순")
            void success_search_recipe_by_create_at() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByTitleAndCreateAt();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.TITLE,
                        CREATED_AT, "제육");

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);
                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));
                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(),
                        any(), any());
                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }


            }

            @Test
            @DisplayName("레시피 검색 성공 - 댓글 순")
            void success_search_recipe_by_comment_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByTitleAndCommentCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.TITLE,
                        COMMENT_COUNT, "제육");

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);
                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));
                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(),
                        any(), any());
                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );
                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }

            @Test
            @DisplayName("레시피 검색 성공 - 좋아요 순")
            void success_search_recipe_by_heart_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByTitleAndHeartCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.TITLE,
                        HEART_COUNT, "제육");

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);
                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));
                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(),
                        any(), any());
                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );
                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }

            }
        }

        @Nested
        @DisplayName("작성자 닉네임으로 레시피 검색하기")
        class RecipeSearchByWriter {
            @Test
            @DisplayName("레시피 검색 성공 - 최신 순")
            void success_search_recipe_by_created_at() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                Member writerTwo = Member.builder()
                        .nickname("세체요가 되고 싶은 요리사")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByWriterAndCreatedAt();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.WRITER,
                        CREATED_AT, writerTwo.getNickname());

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }

                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }

            }

            @Test
            @DisplayName("레시피 검색 성공 - 댓글 순")
            void success_search_recipe_by_comment_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                Member writerTwo = Member.builder()
                        .nickname("세체요가 되고 싶은 요리사")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByWriterAndCommentCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.WRITER,
                        COMMENT_COUNT, writerTwo.getNickname());

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }

            @Test
            @DisplayName("레시피 검색 성공 - 좋아요 순")
            void success_search_recipe_by_heart_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                Member writerTwo = Member.builder()
                        .nickname("세체요가 되고 싶은 요리사")
                        .build();

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByWriterAndHeartCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.WRITER,
                        HEART_COUNT, writerTwo.getNickname());

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }
        }

        @Nested
        @DisplayName("레시피 재료로 레시피 검색하기")
        class RecipeSearchByIngredients {
            @Test
            @DisplayName("레시피 검색 성공 - 최신 순")
            void success_search_recipe_by_created_at() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                String ingredients = "양파 당근 돼지고기";

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByIngredientsAndCreateAt();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.INGREDIENTS,
                        CREATED_AT, ingredients);

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }

            @Test
            @DisplayName("레시피 검색 성공 - 댓글 순")
            void success_search_recipe_by_comment_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                String ingredients = "양파 당근 돼지고기";

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByIngredientsAndCommentCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.INGREDIENTS,
                        CREATED_AT, ingredients);

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }

            @Test
            @DisplayName("레시피 검색 성공 - 좋아요 순")
            void success_search_recipe_by_heart_count() {

                // given
                Member member = Member.builder()
                        .id(10L)
                        .nickname("foodlier")
                        .build();

                String ingredients = "양파 당근 돼지고기";

                PageImpl<RecipeDocument> recipeDocumentPage = DummyDataGenerator.getRecipeDocumentByIngredientsAndHeartCount();
                RecipeSearchRequest recipeSearchRequest = DummyDataGenerator.getRecipeSearchRequest(SearchType.INGREDIENTS,
                        CREATED_AT, ingredients);

                ListResponse<RecipeCardDto> expectedRecipeList = DummyDataGenerator.getListResponse(recipeDocumentPage);

                given(memberRepository.findById(any()))
                        .willReturn(Optional.of(member));

                given(recipeSearchRepository.searchBy(any(), any(), any()))
                        .willReturn(recipeDocumentPage);
                for (int i = 0; i < expectedRecipeList.getContent().size(); i++) {
                    given(heartRepository.existsHeart(anyLong(),any())).willReturn(false);
                }
                // when
                ListResponse<RecipeCardDto> recipeList = recipeService.getRecipeList(recipeSearchRequest);

                // then
                verify(recipeSearchRepository, times(1)).searchBy(any(), any(), any());

                assertAll(
                        () -> assertEquals(expectedRecipeList.getTotalPages(), recipeList.getTotalPages()),
                        () -> assertEquals(expectedRecipeList.getTotalElements(), recipeList.getTotalElements()),
                        () -> assertEquals(expectedRecipeList.isHasNextPage(), recipeList.isHasNextPage())
                );

                for (int i = 0; i < recipeList.getContent().size(); i++) {
                    RecipeCardDto actual = recipeList.getContent().get(i);
                    RecipeCardDto expect = recipeList.getContent().get(i);

                    assertEquals(actual.getId(), expect.getId());
                    assertEquals(actual.getHeartCount(), expect.getHeartCount());
                    assertEquals(actual.getTitle(), expect.getTitle());
                    assertEquals(actual.getContent(), expect.getContent());
                    assertEquals(actual.getNickName(), expect.getNickName());
                    assertEquals(actual.getMainImageUrl(), expect.getMainImageUrl());
                    assertEquals(actual.isHeart(), expect.isHeart());
                }
            }
        }
    }


    @Test
    @DisplayName("별점 추가 성공")
    void success_plusReviewStar() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        Recipe.builder()
                                .id(1L)
                                .recipeStatistics(
                                        RecipeStatistics.builder()
                                                .reviewStarSum(0)
                                                .reviewStarAverage(0)
                                                .reviewCount(0)
                                                .build()
                                )
                                .build()
                ));

        //when
        recipeService.plusReviewStar(1L, 5);

        //then
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository, times(1))
                .save(captor.capture());

        Recipe recipe = captor.getValue();

        assertAll(
                () -> assertEquals(5, recipe.getRecipeStatistics().getReviewStarSum()),
                () -> assertEquals(5, recipe.getRecipeStatistics().getReviewStarAverage()),
                () -> assertEquals(1, recipe.getRecipeStatistics().getReviewCount())
        );
    }

    @Test
    @DisplayName("별점 추가 실패 - 꿀조합 X")
    void fail_plusReviewStar_no_such_recipe() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.plusReviewStar(1L, 5));

        //then
        assertEquals(NO_SUCH_RECIPE, exception.getErrorCode());
    }

    @Test
    @DisplayName("별점 수정 성공")
    void success_updateReviewStar() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        Recipe.builder()
                                .id(1L)
                                .recipeStatistics(
                                        RecipeStatistics.builder()
                                                .reviewStarSum(10)
                                                .reviewStarAverage(5)
                                                .reviewCount(2)
                                                .build()
                                )
                                .build()
                ));

        //when
        recipeService.updateReviewStar(1L, 5, 3);

        //then
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository, times(1))
                .save(captor.capture());

        Recipe recipe = captor.getValue();

        assertAll(
                () -> assertEquals(8, recipe.getRecipeStatistics().getReviewStarSum()),
                () -> assertEquals(4, recipe.getRecipeStatistics().getReviewStarAverage()),
                () -> assertEquals(2, recipe.getRecipeStatistics().getReviewCount())
        );
    }

    @Test
    @DisplayName("별점 수정 실패 - 꿀조합 X")
    void fail_updateReviewStar() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.plusReviewStar(1L, 5));

        //then
        assertEquals(NO_SUCH_RECIPE, exception.getErrorCode());
    }

    @Test
    @DisplayName("별점 차감 성공")
    void success_minusReviewStar() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.of(
                        Recipe.builder()
                                .id(1L)
                                .recipeStatistics(
                                        RecipeStatistics.builder()
                                                .reviewStarSum(10)
                                                .reviewStarAverage(5)
                                                .reviewCount(2)
                                                .build()
                                )
                                .build()
                ));

        //when
        recipeService.minusReviewStar(1L, 5);

        //then
        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository, times(1))
                .save(captor.capture());

        Recipe recipe = captor.getValue();

        assertAll(
                () -> assertEquals(5, recipe.getRecipeStatistics().getReviewStarSum()),
                () -> assertEquals(5, recipe.getRecipeStatistics().getReviewStarAverage()),
                () -> assertEquals(1, recipe.getRecipeStatistics().getReviewCount())
        );
    }

    @Test
    @DisplayName("별점 차감 실패 - 꿀조합 X")
    void fail_minusReviewStar_() {
        //given
        given(recipeRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RecipeException exception = assertThrows(RecipeException.class,
                () -> recipeService.minusReviewStar(1L, 5));

        //then
        assertEquals(NO_SUCH_RECIPE, exception.getErrorCode());
    }

    @Test
    @DisplayName("레시피 댓글 수 증가 성공")
    void success_plusNumberOfComment() {

        // given
        Member member = getMember();
        Recipe recipe = getExpectedRecipe(member);
        Recipe updatedRecipe = getCommentPlusRecipe(member);

        RecipeDocument recipeDocument = getRecipeDocument(member);
        RecipeDocument updatedRecipeDocument = RecipeDocument.builder()
                .ingredients(recipeDocument.getIngredients())
                .numberOfComment(recipeDocument.getNumberOfComment() + 1)
                .numberOfHeart(recipeDocument.getNumberOfHeart())
                .writer(recipeDocument.getWriter())
                .id(recipeDocument.getId())
                .build();
        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(recipe));

        given(recipeSearchRepository.findById(any()))
                .willReturn(Optional.of(recipeDocument));

        given(recipeSearchRepository.save(any()))
                .willReturn(updatedRecipeDocument);

        given(recipeRepository.save(any()))
                .willReturn(updatedRecipe);
        // when

        Recipe recipeResult = recipeService.plusCommentCount(1L);

        // then
        ArgumentCaptor<RecipeDocument> recipeDocumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify(recipeRepository, times(1)).findById(any());
        verify(recipeSearchRepository, times(1)).findById(any());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentCaptor.capture());
        verify(recipeRepository, times(1)).save(recipeCaptor.capture());

        assertAll(
                () -> assertEquals(updatedRecipeDocument.getNumberOfComment(),
                        recipeDocumentCaptor.getValue().getNumberOfComment()),
                () -> assertEquals(recipeResult.getCommentCount(), recipeCaptor.getValue().getCommentCount())
        );
    }

    @Test
    @DisplayName("레시피 댓글 수 증가 실패 - 해당하는 레시피를 찾을 수 없음")
    void fail_plusNumberOfComment_NO_SUCH_RECIPE() {

        // given
        given(recipeRepository.findById(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        // when
        RecipeException recipeException = assertThrows(RecipeException.class, () -> recipeService.plusCommentCount(1L));

        // then

        verify(recipeRepository, times(1)).findById(any());
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE.getDescription(), recipeException.getDescription());
    }

    @Test
    @DisplayName("레시피 댓글 수 증가 실패 - 해당하는 레시피 검색 객체를 찾을 수 없음")
    void fail_plusNumberOfComment_NO_SUCH_RECIPE_DOCUEMENT() {

        // given
        Member member = getMember();
        Recipe recipe = getExpectedRecipe(member);

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(recipe));

        given(recipeSearchRepository.findById(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE_DOCUMENT));

        // when
        RecipeException recipeException = assertThrows(RecipeException.class, () -> recipeService.plusCommentCount(1L));

        // then
        verify(recipeRepository, times(1)).findById(any());
        assertEquals(NO_SUCH_RECIPE_DOCUMENT, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE_DOCUMENT.getDescription(), recipeException.getDescription());
    }

    @Test
    @DisplayName("레시피 댓글 수 감소 성공")
    void success_minusNumberOfComment() {

        // given
        Member member = getMember();
        Recipe recipe = getExpectedRecipe(member);
        Recipe updatedRecipe = getCommentMinusRecipe(member);

        RecipeDocument recipeDocument = getRecipeDocumentOneComment(member);

        RecipeDocument updatedRecipeDocument = RecipeDocument.builder()
                .ingredients(recipeDocument.getIngredients())
                .numberOfComment(recipeDocument.getNumberOfComment() - 1)
                .numberOfHeart(recipeDocument.getNumberOfHeart())
                .writer(recipeDocument.getWriter())
                .id(recipeDocument.getId())
                .build();

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(recipe));

        given(recipeSearchRepository.findById(any()))
                .willReturn(Optional.of(recipeDocument));

        given(recipeSearchRepository.save(any()))
                .willReturn(updatedRecipeDocument);

        given(recipeRepository.save(any()))
                .willReturn(updatedRecipe);
        // when

        recipeService.minusCommentCount(1L);

        // then
        ArgumentCaptor<RecipeDocument> recipeDocumentCaptor = ArgumentCaptor.forClass(RecipeDocument.class);
        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);

        verify(recipeRepository, times(1)).findById(any());
        verify(recipeSearchRepository, times(1)).findById(any());
        verify(recipeSearchRepository, times(1)).save(recipeDocumentCaptor.capture());
        verify(recipeRepository, times(1)).save(recipeCaptor.capture());

        assertAll(
                () -> assertEquals(updatedRecipeDocument.getNumberOfComment(),
                        recipeDocumentCaptor.getValue().getNumberOfComment()),
                () -> assertEquals(updatedRecipe.getCommentCount(), recipeCaptor.getValue().getCommentCount())
        );
    }

    @Test
    @DisplayName("레시피 댓글 수 감소 실패 - 해당하는 레시피를 찾을 수 없음")
    void fail_minusNumberOfComment_NO_SUCH_RECIPE() {

        // given
        given(recipeRepository.findById(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        // when
        RecipeException recipeException = assertThrows(RecipeException.class, () -> recipeService.minusCommentCount(1L));

        // then

        verify(recipeRepository, times(1)).findById(any());
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE.getDescription(), recipeException.getDescription());
    }

    @Test
    @DisplayName("레시피 댓글 수 감소 실패 - 해당하는 레시피 검색 객체를 찾을 수 없음")
    void fail_minusNumberOfComment_NO_SUCH_RECIPE_DOCUEMENT() {

        // given
        Member member = getMember();
        Recipe recipe = getExpectedRecipe(member);

        given(recipeRepository.findById(any()))
                .willReturn(Optional.of(recipe));

        given(recipeSearchRepository.findById(any()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE_DOCUMENT));

        // when
        RecipeException recipeException = assertThrows(RecipeException.class, () -> recipeService.minusCommentCount(1L));

        // then
        verify(recipeRepository, times(1)).findById(any());
        assertEquals(NO_SUCH_RECIPE_DOCUMENT, recipeException.getErrorCode());
        assertEquals(NO_SUCH_RECIPE_DOCUMENT.getDescription(), recipeException.getDescription());
    }

    @Test
    @DisplayName("메인 페이지 생성 순으로 3개 조회 성공")
    void success_getMainPageRecipeList() {
        //given
        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title1")
                        .content("content1")
                        .build())
                .heartCount(1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .member(Member.builder()
                        .id(2L)
                        .nickname("test1")
                        .build())
                .build();
        Recipe recipe2 = Recipe.builder()
                .id(2L)
                .summary(Summary.builder()
                        .title("title2")
                        .content("content2")
                        .build())
                .heartCount(2)
                .createdAt(LocalDateTime.now().minusHours(2))
                .member(Member.builder()
                        .id(2L)
                        .nickname("test2")
                        .build())
                .build();
        Recipe recipe3 = Recipe.builder()
                .id(3L)
                .summary(Summary.builder()
                        .title("title3")
                        .content("content3")
                        .build())
                .heartCount(3)
                .createdAt(LocalDateTime.now().minusHours(3))
                .member(Member.builder()
                        .id(2L)
                        .nickname("test3")
                        .build())
                .build();
        Member member = getMember();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));
        given(recipeRepository.findTop3ByIsPublicIsTrueOrderByCreatedAtDesc())
                .willReturn(List.of(
                        recipe1,
                        recipe2,
                        recipe3
                ));
        given(heartRepository.existsByRecipeAndMember(recipe1, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe2, member))
                .willReturn(false);
        given(heartRepository.existsByRecipeAndMember(recipe3, member))
                .willReturn(true);

        //when
        List<RecipeCardDto> mainPageRecipeList = recipeService.getMainPageRecipeList(MemberAuthDto.builder()
                .id(1L)
                .build());

        //then
        for (int i = 0; i < mainPageRecipeList.size(); i++) {
            assertEquals("title" + (i + 1),
                    mainPageRecipeList.get(i).getTitle());
            assertEquals("content" + (i + 1),
                    mainPageRecipeList.get(i).getContent());
            assertEquals(i + 1,
                    mainPageRecipeList.get(i).getId());
            assertEquals("test" + (i + 1),
                    mainPageRecipeList.get(i).getNickName());
            assertEquals(i + 1,
                    mainPageRecipeList.get(i).getHeartCount());
        }

        assertAll(
                () -> assertTrue(mainPageRecipeList.get(0).isHeart()),
                () -> assertFalse(mainPageRecipeList.get(1).isHeart()),
                () -> assertTrue(mainPageRecipeList.get(2).isHeart())
        );
    }

    @Test
    @DisplayName("메인 페이지 생성순으로 3개 조회 실패 - 회원을 찾을 수 없음")
    void fail_getMainPageRecipeList() {
        //given
        given(memberRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> recipeService.getMainPageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("레시피 페이지에서 생성순으로 조회 성공")
    void success_getRecipePageRecipeList_orderTypeCreatedAt() {
        //given
        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title1")
                        .content("content1")
                        .build())
                .heartCount(1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test1")
                        .build())
                .build();
        Recipe recipe2 = Recipe.builder()
                .id(2L)
                .summary(Summary.builder()
                        .title("title2")
                        .content("content2")
                        .build())
                .heartCount(2)
                .createdAt(LocalDateTime.now().minusHours(2))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test2")
                        .build())
                .build();
        Recipe recipe3 = Recipe.builder()
                .id(3L)
                .summary(Summary.builder()
                        .title("title3")
                        .content("content3")
                        .build())
                .heartCount(3)
                .createdAt(LocalDateTime.now().minusHours(3))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test3")
                        .build())
                .build();
        Member member = getMember();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));
        given(heartRepository.existsByRecipeAndMember(recipe1, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe2, member))
                .willReturn(false);
        given(heartRepository.existsByRecipeAndMember(recipe3, member))
                .willReturn(true);
        given(recipeRepository.findByIsPublicIsTrueOrderByCreatedAtDesc(any()))
                .willReturn(new PageImpl<>(new ArrayList<>(List.of(
                        recipe1,
                        recipe2,
                        recipe3
                ))));

        //when
        ListResponse<RecipeCardDto> recipePageRecipeList = recipeService
                .getRecipePageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10), CREATED_AT);

        //then
        for (int i = 0; i < recipePageRecipeList.getContent().size(); i++) {
            assertEquals("title" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getTitle());
            assertEquals("content" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getContent());
            assertEquals(i + 1,
                    recipePageRecipeList.getContent().get(i).getId());
            assertEquals("test" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getNickName());
        }

        assertAll(
                () -> assertEquals(3, recipePageRecipeList.getTotalElements()),
                () -> assertTrue(recipePageRecipeList.getContent().get(0).isHeart()),
                () -> assertFalse(recipePageRecipeList.getContent().get(1).isHeart()),
                () -> assertTrue(recipePageRecipeList.getContent().get(2).isHeart()),
                () -> assertEquals(1,
                        recipePageRecipeList.getTotalPages()),
                () -> assertFalse(recipePageRecipeList.isHasNextPage())
        );
    }

    @Test
    @DisplayName("레시피 페이지에서 좋아요순으로 조회 성공")
    void success_getRecipePageRecipeList_orderTypeHeartCount() {
        //given
        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title1")
                        .content("content1")
                        .build())
                .heartCount(1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test1")
                        .build())
                .build();
        Recipe recipe2 = Recipe.builder()
                .id(2L)
                .summary(Summary.builder()
                        .title("title2")
                        .content("content2")
                        .build())
                .heartCount(2)
                .createdAt(LocalDateTime.now().minusHours(2))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test2")
                        .build())
                .build();
        Recipe recipe3 = Recipe.builder()
                .id(3L)
                .summary(Summary.builder()
                        .title("title3")
                        .content("content3")
                        .build())
                .heartCount(3)
                .createdAt(LocalDateTime.now().minusHours(3))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test3")
                        .build())
                .build();
        Member member = getMember();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));
        given(heartRepository.existsByRecipeAndMember(recipe1, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe2, member))
                .willReturn(false);
        given(heartRepository.existsByRecipeAndMember(recipe3, member))
                .willReturn(true);
        given(recipeRepository.findByIsPublicIsTrueOrderByHeartCountDesc(any()))
                .willReturn(new PageImpl<>(new ArrayList<>(List.of(
                        recipe1,
                        recipe2,
                        recipe3
                )).stream()
                        .sorted((x, y) -> y.getHeartCount() - x.getHeartCount())
                        .collect(Collectors.toList())));

        //when
        ListResponse<RecipeCardDto> recipePageRecipeList = recipeService
                .getRecipePageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10), HEART_COUNT);

        //then
        for (int i = 0; i < recipePageRecipeList.getContent().size(); i++) {
            assertEquals("title" + (3 - i),
                    recipePageRecipeList.getContent().get(i).getTitle());
            assertEquals("content" + (3 - i),
                    recipePageRecipeList.getContent().get(i).getContent());
            assertEquals(3 - i,
                    recipePageRecipeList.getContent().get(i).getId());
            assertEquals("test" + (3 - i),
                    recipePageRecipeList.getContent().get(i).getNickName());
        }

        assertAll(
                () -> assertEquals(3, recipePageRecipeList.getTotalElements()),
                () -> assertTrue(recipePageRecipeList.getContent().get(0).isHeart()),
                () -> assertFalse(recipePageRecipeList.getContent().get(1).isHeart()),
                () -> assertTrue(recipePageRecipeList.getContent().get(2).isHeart()),
                () -> assertEquals(1,
                        recipePageRecipeList.getTotalPages()),
                () -> assertFalse(recipePageRecipeList.isHasNextPage())
        );
    }

    @Test
    @DisplayName("레시피 페이지에서 댓글순으로 조회 성공")
    void success_getRecipePageRecipeList_orderTypeCommentCount() {
        //given
        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title1")
                        .content("content1")
                        .build())
                .heartCount(1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .commentCount(3)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test1")
                        .build())
                .build();
        Recipe recipe2 = Recipe.builder()
                .id(2L)
                .summary(Summary.builder()
                        .title("title2")
                        .content("content2")
                        .build())
                .heartCount(2)
                .createdAt(LocalDateTime.now().minusHours(2))
                .commentCount(2)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test2")
                        .build())
                .build();
        Recipe recipe3 = Recipe.builder()
                .id(3L)
                .summary(Summary.builder()
                        .title("title3")
                        .content("content3")
                        .build())
                .heartCount(3)
                .commentCount(1)
                .createdAt(LocalDateTime.now().minusHours(3))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test3")
                        .build())
                .build();
        Member member = getMember();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));
        given(heartRepository.existsByRecipeAndMember(recipe1, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe2, member))
                .willReturn(false);
        given(heartRepository.existsByRecipeAndMember(recipe3, member))
                .willReturn(true);
        given(recipeRepository.findByIsPublicIsTrueOrderByCommentCountDesc(any()))
                .willReturn(new PageImpl<>(new ArrayList<>(List.of(
                        recipe1,
                        recipe2,
                        recipe3
                )).stream()
                        .sorted((x, y) -> y.getCommentCount() - x.getCommentCount())
                        .collect(Collectors.toList())));

        //when
        ListResponse<RecipeCardDto> recipePageRecipeList = recipeService
                .getRecipePageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10), COMMENT_COUNT);

        //then
        for (int i = 0; i < recipePageRecipeList.getContent().size(); i++) {
            assertEquals("title" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getTitle());
            assertEquals("content" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getContent());
            assertEquals(i + 1,
                    recipePageRecipeList.getContent().get(i).getId());
            assertEquals("test" + (i + 1),
                    recipePageRecipeList.getContent().get(i).getNickName());
        }

        assertAll(
                () -> assertEquals(3, recipePageRecipeList.getTotalElements()),
                () -> assertTrue(recipePageRecipeList.getContent().get(0).isHeart()),
                () -> assertFalse(recipePageRecipeList.getContent().get(1).isHeart()),
                () -> assertTrue(recipePageRecipeList.getContent().get(2).isHeart()),
                () -> assertEquals(1,
                        recipePageRecipeList.getTotalPages()),
                () -> assertFalse(recipePageRecipeList.isHasNextPage())
        );
    }

    @Test
    @DisplayName("레시피 페이지에서 조회 실패 - 회원을 찾을 수 없음")
    void fail_getRecipePageRecipeList_memberNotFound() {
        //given
        given(memberRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> recipeService.getRecipePageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10), any()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("레시피 페이지에서 조회 실패 - 정의되지 않은 정렬타입")
    void fail_fail_getRecipePageRecipeList_orderTypeNotFound() {
        //given
        Member member = getMember();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> recipeService.getRecipePageRecipeList(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10), INVALID_TYPE));

        //then
        assertEquals(ORDER_TYPE_NOT_FOUND, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("추천 레시피 조회 성공")
    void success_recommendedRecipe() {
        //given
        Member member = getMember();
        Recipe recipe1 = Recipe.builder()
                .id(1L)
                .summary(Summary.builder()
                        .title("title1")
                        .content("content1")
                        .build())
                .heartCount(1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .commentCount(3)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test1")
                        .build())
                .build();
        Recipe recipe2 = Recipe.builder()
                .id(2L)
                .summary(Summary.builder()
                        .title("title2")
                        .content("content2")
                        .build())
                .heartCount(2)
                .createdAt(LocalDateTime.now().minusHours(2))
                .commentCount(2)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test2")
                        .build())
                .build();
        Recipe recipe3 = Recipe.builder()
                .id(3L)
                .summary(Summary.builder()
                        .title("title3")
                        .content("content3")
                        .build())
                .heartCount(3)
                .commentCount(1)
                .createdAt(LocalDateTime.now().minusHours(3))
                .member(Member.builder()
                        .id(1L)
                        .nickname("test3")
                        .build())
                .build();
        Recipe recipe4 = Recipe.builder()
                .id(4L)
                .summary(Summary.builder()
                        .title("title4")
                        .content("content4")
                        .build())
                .heartCount(4)
                .createdAt(LocalDateTime.now().minusHours(4))
                .commentCount(3)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test4")
                        .build())
                .build();
        Recipe recipe5 = Recipe.builder()
                .id(5L)
                .summary(Summary.builder()
                        .title("title5")
                        .content("content5")
                        .build())
                .heartCount(5)
                .createdAt(LocalDateTime.now().minusHours(5))
                .commentCount(2)
                .member(Member.builder()
                        .id(1L)
                        .nickname("test5")
                        .build())
                .build();

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(member));
        given(recipeRepository.findTop5ByIsPublicIsTrueAndCreatedAtAfterOrderByHeartCountDesc(any()))
                .willReturn(Stream.of(
                                recipe1,
                                recipe2,
                                recipe3,
                                recipe4,
                                recipe5
                        )
                        .sorted((x, y) -> y.getHeartCount() - x.getHeartCount())
                        .collect(Collectors.toList()));
        given(heartRepository.existsByRecipeAndMember(recipe1, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe2, member))
                .willReturn(false);
        given(heartRepository.existsByRecipeAndMember(recipe3, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe4, member))
                .willReturn(true);
        given(heartRepository.existsByRecipeAndMember(recipe5, member))
                .willReturn(false);

        //when
        List<RecipeCardDto> recipeListDtoList = recipeService.recommendedRecipe(MemberAuthDto.builder()
                .id(1L)
                .build());

        //then
        for (int i = 0; i < recipeListDtoList.size(); i++) {
            assertEquals("title" + (5 - i),
                    recipeListDtoList.get(i).getTitle());
            assertEquals("content" + (5 - i),
                    recipeListDtoList.get(i).getContent());
            assertEquals(5 - i,
                    recipeListDtoList.get(i).getHeartCount());
            assertEquals(5 - i,
                    recipeListDtoList.get(i).getId());
            assertEquals("test" + (5 - i),
                    recipeListDtoList.get(i).getNickName());
        }

        assertAll(
                () -> assertTrue(recipeListDtoList.get(4).isHeart()),
                () -> assertFalse(recipeListDtoList.get(3).isHeart()),
                () -> assertTrue(recipeListDtoList.get(2).isHeart()),
                () -> assertTrue(recipeListDtoList.get(1).isHeart()),
                () -> assertFalse(recipeListDtoList.get(0).isHeart())
        );
    }

    @Test
    @DisplayName("추천 레시피 조회 실패 - 회원을 찾을 수 없음")
    void fail_recommendedRecipe_memberNotFound() {
        //given
        given(memberRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> recipeService.recommendedRecipe(MemberAuthDto.builder()
                        .id(1L)
                        .build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("좋아요 누른 레시피 목록 가져오기 성공")
    void success_getRecipeForHeart() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .mainImageUrl("http://s3.test.com/main.png")
                .summary(
                        Summary.builder()
                                .title("제육볶음")
                                .content("맛있는 제육볶음")
                                .build()
                )
                .heartCount(100)
                .build();

        given(recipeRepository.findHeart(anyLong(), any()))
                .willReturn(new PageImpl<>(List.of(recipe)));

        //when
        ListResponse<RecipeDtoTopResponse> recipeList = recipeService
                .getRecipeForHeart(1L, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(recipe.getId(), recipeList.getContent().get(0).getRecipeId()),
                () -> assertEquals(recipe.getMainImageUrl(), recipeList.getContent().get(0).getMainImageUrl()),
                () -> assertEquals(recipe.getSummary().getTitle(), recipeList.getContent().get(0).getTitle()),
                () -> assertEquals(recipe.getSummary().getContent(), recipeList.getContent().get(0).getContent()),
                () -> assertEquals(recipe.getHeartCount(), recipeList.getContent().get(0).getHeartCount()),
                () -> assertTrue(recipeList.getContent().get(0).getIsHeart())
        );
    }

    @Test
    @DisplayName("해당 회원이 작성한 꿀조합 목록 반환 성공")
    void success_getRecipeListByMemberId() {
        //given
        Recipe recipe = Recipe.builder()
                .id(1L)
                .mainImageUrl("http://s3.test.com/main.png")
                .summary(
                        Summary.builder()
                                .title("제육볶음")
                                .content("맛있는 제육볶음")
                                .build()
                )
                .heartCount(100)
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(Member.builder().id(1L).build()));

        given(memberRepository.findById(2L))
                .willReturn(Optional.of(Member.builder().id(2L).build()));

        given(recipeRepository.findByMemberAndIsPublicTrueAndIsQuotationFalse(any(), any()))
                .willReturn(new PageImpl<>(List.of(recipe)));

        given(heartRepository.existsByRecipeAndMember(any(), any()))
                .willReturn(true);

        //when
        ListResponse<RecipeDtoTopResponse> recipeList = recipeService
                .getRecipeListByMemberId(1L, 2L, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(recipe.getId(), recipeList.getContent().get(0).getRecipeId()),
                () -> assertEquals(recipe.getMainImageUrl(), recipeList.getContent().get(0).getMainImageUrl()),
                () -> assertEquals(recipe.getSummary().getTitle(), recipeList.getContent().get(0).getTitle()),
                () -> assertEquals(recipe.getSummary().getContent(), recipeList.getContent().get(0).getContent()),
                () -> assertEquals(recipe.getHeartCount(), recipeList.getContent().get(0).getHeartCount()),
                () -> assertTrue(recipeList.getContent().get(0).getIsHeart())
        );
    }

    @Test
    @DisplayName("해당 회원이 작성한 꿀조합 목록 반환 실패 - member를 찾을 수 없음")
    void fail_getRecipeListByMemberId_member_not_found() {
        //given
        given(memberRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> recipeService.getRecipeListByMemberId(1L,
                        2L, PageRequest.of(0, 10)));

        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 회원이 작성한 꿀조합 목록 반환 실패 - target member를 찾을 수 없음")
    void fail_getRecipeListByMemberId_target_member_not_found() {
        //given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(Member.builder().id(1L).build()));

        given(memberRepository.findById(2L))
                .willReturn(Optional.empty());

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> recipeService.getRecipeListByMemberId(1L,
                        2L, PageRequest.of(0, 10)));

        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
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
                .commentCount(1)
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
                .ingredients(String.join(" ", List.of("양파", "가지", "오이")))
                .build();
    }

    private static RecipeDocument getRecipeDocumentOneComment(Member member) {
        return RecipeDocument
                .builder()
                .title("title")
                .writer(member.getNickname())
                .numberOfComment(1L)
                .ingredients(String.join(" ", List.of("양파", "가지", "오이")))
                .build();
    }

    private static RecipeDocument getRecipeUpdatedDocument(Member member, RecipeDtoRequest updateRecipe) {
        return RecipeDocument
                .builder()
                .title(updateRecipe.getTitle())
                .writer(member.getNickname())
                .numberOfComment(0L)
                .ingredients(updateRecipe.getRecipeIngredientDtoList().stream()
                        .map(RecipeIngredientDto::getName)
                        .collect(Collectors.joining(" ")))
                .numberOfComment(0L)
                .build();
    }

    private static Recipe getCommentMinusRecipe(Member member) {
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
                .commentCount(0)
                .build();
    }

    private static Recipe getCommentPlusRecipe(Member member) {
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
                .commentCount(2)
                .build();
    }

}