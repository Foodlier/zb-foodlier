package com.zerobase.foodlier.module.review.recipe.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import com.zerobase.foodlier.module.review.recipe.dto.ChangedRecipeReviewResponse;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewRequestDto;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import com.zerobase.foodlier.module.review.recipe.exception.RecipeReviewException;
import com.zerobase.foodlier.module.review.recipe.repository.RecipeReviewRepository;
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

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static com.zerobase.foodlier.module.review.recipe.exception.RecipeReviewErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecipeReviewServiceImplTest {
    @Mock
    private RecipeReviewRepository recipeReviewRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private RecipeReviewServiceImpl recipeReviewService;

    private Member getMember(){
        return Member.builder()
                .id(1L)
                .nickname("tom")
                .profileUrl("/image/default.png")
                .build();
    }
    private Recipe getRecipe(){
        return Recipe.builder()
                .id(1L)
                .isQuotation(false)
                .isPublic(true)
                .build();
    }

    private RecipeReview getRecipeReview(){

         return RecipeReview.builder()
                .id(1L)
                .content("더 이상의 말은 필요없다. 최고의 치킨임")
                .star(5)
                .cookUrl("https://s3.test.com/image.png")
                .createdAt(LocalDateTime.of(2023, 10, 1, 9, 0, 0))
                .build();

    }

    @Nested
    @DisplayName("createRecipeReview() 테스트")
    class createRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 작성 성공")
        void success_createRecipeReview(){
            //given
            Member member = getMember();
            Recipe recipe = getRecipe();
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(recipe));
            given(recipeReviewRepository.existsByMemberAndRecipe(any(), any()))
                    .willReturn(false);

            RecipeReviewRequestDto request = RecipeReviewRequestDto.builder()
                    .content("넘나 맛있는 김치찌개!")
                    .star(5)
                    .cookImageUrl("https://s3.test.com/image1.png")
                    .build();

            //when
            recipeReviewService.createRecipeReview(1L, 1L, request);

            //then
            ArgumentCaptor<RecipeReview> captor = ArgumentCaptor.forClass(RecipeReview.class);
            verify(recipeReviewRepository, times(1)).save(captor.capture());
            RecipeReview recipeReview = captor.getValue();
            assertAll(
                    () -> assertEquals(recipe, recipeReview.getRecipe()),
                    () -> assertEquals(member, recipeReview.getMember()),
                    () -> assertEquals(request.getContent(), recipeReview.getContent()),
                    () -> assertEquals(request.getStar(), recipeReview.getStar()),
                    () -> assertEquals(request.getCookImageUrl(), recipeReview.getCookUrl())
            );
        }

        @Test
        @DisplayName("꿀조합 후기 작성 실패 - 회원 X")
        void fail_createRecipeReview_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> recipeReviewService.createRecipeReview(1L,
                            1L, any()));
            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("꿀조합 후기 작성 실패 - 꿀조합 X")
        void fail_createRecipeReview_no_such_recipe(){
            //given
            Member member = getMember();
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            RecipeException exception = assertThrows(RecipeException.class,
                    () -> recipeReviewService.createRecipeReview(1L,
                            1L, any()));
            //then
            assertEquals(NO_SUCH_RECIPE, exception.getErrorCode());
        }

        @Test
        @DisplayName("꿀조합 후기 작성 실패 - 견적서 또는 비공개")
        void fail_createRecipeReview_quotation_or_is_not_public(){
            //given
            Member member = getMember();
            Recipe recipe = Recipe.builder()
                    .id(1L)
                    .isQuotation(true)
                    .isPublic(false)
                    .build();
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(recipe));
            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.createRecipeReview(1L,
                            1L, any()));
            //then
            assertEquals(QUOTATION_OR_IS_NOT_PUBLIC, exception.getErrorCode());
        }

        @Test
        @DisplayName("꿀조합 후기 작성 실패 - 이미 작성된 후기")
        void fail_createRecipeReview_already_written_recipe_review(){
            //given
            Member member = getMember();
            Recipe recipe = getRecipe();
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(recipe));
            given(recipeReviewRepository.existsByMemberAndRecipe(any(), any()))
                    .willReturn(true);
            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.createRecipeReview(1L,
                            1L, any()));
            //then
            assertEquals(ALREADY_WRITTEN_RECIPE_REVIEW, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("getMyRecipeReview() 테스트")
    class getMyRecipeReviewTest{

        @Test
        @DisplayName("내가 작성한 꿀조합 리뷰 조회 성공")
        void success_getMyRecipeReview(){
            //given
            Member member = getMember();
            RecipeReview recipeReview = getRecipeReview();
            Recipe recipe = getRecipe();
            recipeReview.updateMember(member);
            recipeReview.updateRecipe(recipe);
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(getRecipe()));
            given(recipeReviewRepository.findByMemberAndRecipe(any(), any()))
                    .willReturn(Optional.of(recipeReview));

            //when
            RecipeReviewResponseDto response = recipeReviewService.getMyRecipeReview(
                    1L, 1L
            );

            //then
            assertAll(
                    () -> assertEquals(recipe.getId(), response.getRecipeId()),
                    () -> assertEquals(recipeReview.getId(), response.getRecipeReviewId()),
                    () -> assertEquals(member.getNickname(), response.getNickname()),
                    () -> assertEquals(member.getProfileUrl(), response.getProfileUrl()),
                    () -> assertEquals(recipeReview.getContent(), response.getContent()),
                    () -> assertEquals(recipeReview.getStar(), response.getStar()),
                    () -> assertEquals(recipeReview.getCookUrl(), response.getCookUrl()),
                    () -> assertEquals(recipeReview.getCreatedAt(), response.getCreatedAt())
            );
        }

        @Test
        @DisplayName("내가 작성한 꿀조합 리뷰 조회 실패 - 회원 X")
        void fail_getMyRecipeReview_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> recipeReviewService.getMyRecipeReview(1L,
                            1L));
            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("내가 작성한 꿀조합 리뷰 조회 실패 - 꿀조합 X")
        void fail_getMyRecipeReview_no_such_recipe(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            RecipeException exception = assertThrows(RecipeException.class,
                    () -> recipeReviewService.getMyRecipeReview(1L,
                            1L));
            //then
            assertEquals(NO_SUCH_RECIPE, exception.getErrorCode());
        }

        @Test
        @DisplayName("내가 작성한 꿀조합 리뷰 조회 실패 - 꿀조합 리뷰 X")
        void fail_getMyRecipeReview_recipe_review_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(getRecipe()));
            given(recipeReviewRepository.findByMemberAndRecipe(any(), any()))
                    .willReturn(Optional.empty());

            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.getMyRecipeReview(1L,
                            1L));
            //then
            assertEquals(RECIPE_REVIEW_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("getRecipeReview() 테스트")
    class getRecipeReviewListTest{

        @Test
        @DisplayName("꿀조합 후기 목록 가져오기 성공")
        void success_getRecipeReviewList(){
            //given
            Member member = getMember();
            RecipeReview recipeReview = getRecipeReview();
            Recipe recipe = getRecipe();
            recipeReview.updateMember(member);
            recipeReview.updateRecipe(recipe);
            given(recipeReviewRepository.findRecipe(anyLong(), anyLong(), any()))
                    .willReturn(
                            new PageImpl<>(
                                    new ArrayList<>(
                                            List.of(
                                                    recipeReview
                                            )
                                    ),
                                    PageRequest.of(0, 10),
                                    1
                            )
                    );

            //when
            ListResponse<RecipeReviewResponseDto> responseList = recipeReviewService
                    .getRecipeReviewList(1L, 1L, PageRequest.of(0, 10));

            //then
            assertAll(
                    () -> assertEquals(recipe.getId(), responseList.getContent().get(0).getRecipeId()),
                    () -> assertEquals(recipeReview.getId(), responseList.getContent().get(0).getRecipeReviewId()),
                    () -> assertEquals(member.getNickname(), responseList.getContent().get(0).getNickname()),
                    () -> assertEquals(member.getProfileUrl(), responseList.getContent().get(0).getProfileUrl()),
                    () -> assertEquals(recipeReview.getContent(), responseList.getContent().get(0).getContent()),
                    () -> assertEquals(recipeReview.getStar(), responseList.getContent().get(0).getStar()),
                    () -> assertEquals(recipeReview.getCookUrl(), responseList.getContent().get(0).getCookUrl()),
                    () -> assertEquals(recipeReview.getCreatedAt(), responseList.getContent().get(0).getCreatedAt())
            );
        }

    }

    @Nested
    @DisplayName("getReviewDetail() 테스트")
    class getReviewDetailTest{

        @Test
        @DisplayName("리뷰 상세 조회 성공")
        void success_getReviewDetail(){
            //given
            Member member = getMember();
            RecipeReview recipeReview = getRecipeReview();
            Recipe recipe = getRecipe();
            recipeReview.updateMember(member);
            recipeReview.updateRecipe(recipe);
            given(recipeReviewRepository.findById(anyLong()))
                    .willReturn(Optional.of(recipeReview));

            //when
            RecipeReviewResponseDto response = recipeReviewService.getReviewDetail(
                    1L
            );

            //then
            assertAll(
                    () -> assertEquals(recipe.getId(), response.getRecipeId()),
                    () -> assertEquals(recipeReview.getId(), response.getRecipeReviewId()),
                    () -> assertEquals(member.getNickname(), response.getNickname()),
                    () -> assertEquals(member.getProfileUrl(), response.getProfileUrl()),
                    () -> assertEquals(recipeReview.getContent(), response.getContent()),
                    () -> assertEquals(recipeReview.getStar(), response.getStar()),
                    () -> assertEquals(recipeReview.getCookUrl(), response.getCookUrl()),
                    () -> assertEquals(recipeReview.getCreatedAt(), response.getCreatedAt())
            );
        }

        @Test
        @DisplayName("리뷰 상세 조회 실패 - 꿀조합 후기 X")
        void success_getReviewDetail_recipe_review_not_found(){
            //given
            given(recipeReviewRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.getReviewDetail(1L));
            //then
            assertEquals(RECIPE_REVIEW_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("updateRecipeReview() 테스트")
    class updateRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 수정 성공")
        void success_updateRecipeReview(){
            //given

            RecipeReview returningReview = getRecipeReview();
            Recipe recipe = getRecipe();
            returningReview.updateRecipe(recipe);

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeReviewRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.of(returningReview));

            RecipeReviewRequestDto request = RecipeReviewRequestDto.builder()
                    .content("수정된 내용")
                    .star(3)
                    .cookImageUrl("https://s3.test.com/updatedImage.png")
                    .build();
            //when
            ChangedRecipeReviewResponse response = recipeReviewService
                    .updateRecipeReview(1L, 1L, request);

            //then
            ArgumentCaptor<RecipeReview> captor = ArgumentCaptor.forClass(RecipeReview.class);
            verify(recipeReviewRepository, times(1)).save(captor.capture());
            RecipeReview recipeReview = captor.getValue();

            //저장되는값 검증
            assertAll(
                    () -> assertEquals(request.getContent(), recipeReview.getContent()),
                    () -> assertEquals(request.getStar(), recipeReview.getStar()),
                    () -> assertEquals(request.getCookImageUrl(), recipeReview.getCookUrl())
            );

            //반환값 검증
            assertAll(
                    () -> assertEquals(recipe.getId(), response.getRecipeId()),
                    () -> assertEquals(5, response.getStar()),
                    () -> assertEquals("https://s3.test.com/image.png", response.getCookImageUrl())
            );

        }

        @Test
        @DisplayName("꿀조합 후기 수정 실패 - 회원 X")
        void fail_updateRecipeReview_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> recipeReviewService.updateRecipeReview(1L, 1L,
                            RecipeReviewRequestDto.builder().build()));
            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("꿀조합 후기 수정 실패 - 꿀조합 후기 X")
        void fail_updateRecipeReview_recipe_review_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeReviewRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.empty());
            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.updateRecipeReview(1L, 1L,
                            RecipeReviewRequestDto.builder().build()));
            //then
            assertEquals(RECIPE_REVIEW_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("deleteRecipeReview() 테스트")
    class deleteRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 삭제 성공")
        void success_deleteRecipeReview(){
            //given

            Recipe recipe = getRecipe();
            RecipeReview recipeReview = getRecipeReview();
            recipeReview.updateRecipe(recipe);

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeReviewRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.of(recipeReview));

            //when
            ChangedRecipeReviewResponse response = recipeReviewService
                    .deleteRecipeReview(1L, 1L);

            //then
            verify(recipeReviewRepository, times(1)).delete(recipeReview);

            assertAll(
                    () -> assertEquals(recipe.getId(), response.getRecipeId()),
                    () -> assertEquals(recipeReview.getCookUrl(), response.getCookImageUrl()),
                    () -> assertEquals(recipeReview.getStar(), response.getStar())
            );
        }

        @Test
        @DisplayName("꿀조합 후기 삭세 실패 - 회원 X")
        void fail_deleteRecipeReview_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> recipeReviewService.deleteRecipeReview(1L,
                            1L));
            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("꿀조합 후기 삭제 실패 - 꿀조합 후기 X")
        void fail_deleteRecipeReview_recipe_review_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(getMember()));
            given(recipeReviewRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.empty());
            //when
            RecipeReviewException exception = assertThrows(RecipeReviewException.class,
                    () -> recipeReviewService.deleteRecipeReview(1L,
                            1L));
            //then
            assertEquals(RECIPE_REVIEW_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Test
    @DisplayName("공개 프로필 꿀조합 후기 조회 성공")
    void success_getRecipeReviewForProfile(){
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .profileUrl("http://s3.test.com/member.png")
                .build();

        RecipeReview recipeReview = RecipeReview.builder()
                .id(1L)
                .recipe(Recipe.builder().id(1L).build())
                .member(member)
                .content("content")
                .star(5)
                .cookUrl("http://s3.test.com/cook.png")
                .createdAt(LocalDateTime.now())
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));

        given(recipeReviewRepository.findByMemberOrderByCreatedAtDesc(any(), any()))
                .willReturn(new PageImpl<>(List.of(recipeReview)));

        //when
        ListResponse<RecipeReviewResponseDto> response = recipeReviewService
                .getRecipeReviewForProfile(1L,
                PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(1L, response.getContent().get(0).getRecipeId()),
                () -> assertEquals(recipeReview.getId(), response.getContent().get(0).getRecipeReviewId()),
                () -> assertEquals(member.getNickname(), response.getContent().get(0).getNickname()),
                () -> assertEquals(member.getProfileUrl(), response.getContent().get(0).getProfileUrl()),
                () -> assertEquals(recipeReview.getContent(), response.getContent().get(0).getContent()),
                () -> assertEquals(recipeReview.getStar(), response.getContent().get(0).getStar()),
                () -> assertEquals(recipeReview.getCookUrl(), response.getContent().get(0).getCookUrl()),
                () -> assertEquals(recipeReview.getCreatedAt(), response.getContent().get(0).getCreatedAt())
        );

    }

    @Test
    @DisplayName("공개 프로필 꿀조합 후기 조회 실패 - 회원 X")
    void fail_getRecipeReviewForProfile(){

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> recipeReviewService
                        .getRecipeReviewForProfile(1L,
                                PageRequest.of(0, 10)));

        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

}