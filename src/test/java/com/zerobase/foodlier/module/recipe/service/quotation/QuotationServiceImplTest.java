package com.zerobase.foodlier.module.recipe.service.quotation;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDetailResponse;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDetailDto;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import com.zerobase.foodlier.module.recipe.exception.quotation.QuotationException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.quotation.QuotationErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuotationServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private QuotationServiceImpl quotationService;

    @Nested
    @DisplayName("createQuotation() 테스트")
    class createQuotationTest{

        @Test
        @DisplayName("견적서 생성 성공")
        void success_createQuotation(){
            //given

            Member member = Member.builder().id(1L).build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            QuotationDtoRequest request = QuotationDtoRequest.builder()
                    .title("제육볶음")
                    .content("아주 맛있는 제육볶음 견적서")
                    .recipeIngredientDtoList(
                            List.of(
                                    RecipeIngredientDto.builder()
                                            .name("앞다리살")
                                            .count(1)
                                            .unit("근")
                                            .build()
                            )
                    )
                    .difficulty(Difficulty.MEDIUM)
                    .recipeDetailDtoList(
                            List.of(
                                    "프라이팬에 넣고 볶아줍니다."
                            )
                    )
                    .expectedTime(10)
                    .build();

            //when
            quotationService.createQuotation(1L, request);

            //then
            ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
            verify(recipeRepository, times(1))
                    .save(captor.capture());

            Recipe quotation = captor.getValue();

            assertAll(
                    () -> assertEquals(request.getTitle(), quotation.getSummary().getTitle()),
                    () -> assertEquals(request.getContent(), quotation.getSummary().getContent()),
                    () -> assertEquals(request.getExpectedTime(), quotation.getExpectedTime()),
                    () -> assertEquals(request.getDifficulty(), quotation.getDifficulty()),
                    () -> assertFalse(quotation.getIsPublic()),
                    () -> assertEquals(member.getId(), quotation.getMember().getId()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(), quotation.getRecipeIngredientList().get(0).getName()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(), quotation.getRecipeIngredientList().get(0).getUnit()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(), quotation.getRecipeIngredientList().get(0).getCount()),
                    () -> assertEquals(request.getRecipeDetailDtoList().get(0), quotation.getRecipeDetailList().get(0).getCookingOrder()),
                    () -> assertEquals(true, quotation.getIsQuotation())
            );
        }

        @Test
        @DisplayName("견적서 생성 실패 - 회원 X")
        void fail_createQuotation(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> quotationService.createQuotation(1L, QuotationDtoRequest.builder().build()));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("getQuotationListForRefrigerator() 테스트")
    class getQuotationListForRefrigeratorTest{

        @Test
        @DisplayName("냉장고를 부탁해 페이지에서 - 견적서 조회하기")
        void success_getQuotationListForRefrigerator(){
            //given
            QuotationTopResponse topResponse = QuotationTopResponse.builder()
                    .quotationId(1L)
                    .title("제육볶음")
                    .content("최상의 맛 제육볶음")
                    .difficulty(Difficulty.MEDIUM)
                    .expectedTime(10)
                    .build();

            given(recipeRepository.findQuotationListForRefrigerator(anyLong(), any()))
                    .willReturn(
                            new PageImpl<>(
                                    new ArrayList<>(
                                            Arrays.asList(
                                                topResponse
                                            )
                                    ),
                                    PageRequest.of(0, 10),
                                    1
                            )
                    );

            //when
            List<QuotationTopResponse> responseList = quotationService
                    .getQuotationListForRefrigerator(1L,
                            PageRequest.of(0, 10));

            //then
            assertAll(
                    () -> assertEquals(topResponse.getQuotationId(), responseList.get(0).getQuotationId()),
                    () -> assertEquals(topResponse.getTitle(), responseList.get(0).getTitle()),
                    () -> assertEquals(topResponse.getContent(), responseList.get(0).getContent()),
                    () -> assertEquals(topResponse.getDifficulty(), responseList.get(0).getDifficulty()),
                    () -> assertEquals(topResponse.getExpectedTime(), responseList.get(0).getExpectedTime())
            );

        }

    }

    @Nested
    @DisplayName("getQuotationListForRecipe() 테스트")
    class getQuotationListForRecipeTest{

        @Test
        @DisplayName("꿀조합 작성 페이지에서 - 견적서 조회하기")
        void success_getQuotationListForRecipe(){
            //given
            QuotationTopResponse topResponse = QuotationTopResponse.builder()
                    .quotationId(1L)
                    .title("김치찌개")
                    .content("최상의 맛 김치찌개")
                    .difficulty(Difficulty.MEDIUM)
                    .expectedTime(10)
                    .build();

            given(recipeRepository.findQuotationListForRecipe(anyLong(), any()))
                    .willReturn(
                            new PageImpl<>(
                                    new ArrayList<>(
                                            Arrays.asList(
                                                    topResponse
                                            )
                                    ),
                                    PageRequest.of(0, 10),
                                    1
                            )
                    );

            //when
            List<QuotationTopResponse> responseList = quotationService
                    .getQuotationListForRecipe(1L,
                            PageRequest.of(0, 10));

            //then
            assertAll(
                    () -> assertEquals(topResponse.getQuotationId(), responseList.get(0).getQuotationId()),
                    () -> assertEquals(topResponse.getTitle(), responseList.get(0).getTitle()),
                    () -> assertEquals(topResponse.getContent(), responseList.get(0).getContent()),
                    () -> assertEquals(topResponse.getDifficulty(), responseList.get(0).getDifficulty()),
                    () -> assertEquals(topResponse.getExpectedTime(), responseList.get(0).getExpectedTime())
            );
        }

    }

    @Nested
    @DisplayName("getQuotationDetail() 테스트")
    class getQuotationDetailTest{

        @Test
        @DisplayName("견적서 상세 정보 조회 성공")
        void success_getQuotationDetail(){
            //given
            Recipe quotation = Recipe.builder()
                    .id(1L)
                    .summary(
                            Summary.builder()
                                    .title("김치찌개")
                                    .content("최상의 김치찌개")
                                    .build()
                    )
                    .expectedTime(10)
                    .difficulty(Difficulty.MEDIUM)
                    .recipeIngredientList(
                            List.of(
                                    RecipeIngredient.builder()
                                            .name("김치")
                                            .count(1)
                                            .unit("포기")
                                            .build()
                            )
                    )
                    .recipeDetailList(
                            List.of(
                                    RecipeDetail.builder()
                                            .cookingOrder("뚝배기에 김치를 넣고 끓입니다.")
                                            .build()
                            )
                    )
                    .build();
            given(recipeRepository.existsByIdAndMemberForQuotation(anyLong(), anyLong()))
                    .willReturn(true);

            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.of(quotation));

            //when
            QuotationDetailResponse response = quotationService
                    .getQuotationDetail(1L, 1L);

            //then
            assertAll(
                    () -> assertEquals(quotation.getId(), response.getQuotationId()),
                    () -> assertEquals(quotation.getSummary().getTitle(), response.getTitle()),
                    () -> assertEquals(quotation.getSummary().getContent(), response.getContent()),
                    () -> assertEquals(quotation.getExpectedTime(), response.getExpectedTime()),
                    () -> assertEquals(quotation.getRecipeIngredientList().get(0).getName(),
                            response.getRecipeIngredientDtoList().get(0).getName()),
                    () -> assertEquals(quotation.getRecipeIngredientList().get(0).getCount(),
                            response.getRecipeIngredientDtoList().get(0).getCount()),
                    () -> assertEquals(quotation.getRecipeIngredientList().get(0).getUnit(),
                            response.getRecipeIngredientDtoList().get(0).getUnit()),
                    () -> assertEquals(quotation.getRecipeDetailList().get(0).getCookingOrder(), response.getRecipeDetailDtoList().get(0))
            );

        }

        @Test
        @DisplayName("견적서 상세 정보 조회 실패 - 권한 없음")
        void fail_getQuotationDetail_has_not_quotation_read_permission(){

            //given
            given(recipeRepository.existsByIdAndMemberForQuotation(anyLong(), anyLong()))
                    .willReturn(false);

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.getQuotationDetail(1L, 1L));

            //then
            assertEquals(HAS_NOT_QUOTATION_READ_PERMISSION, exception.getErrorCode());

        }

        @Test
        @DisplayName("견적서 상세 정보 조회 실패 - 견적서 X")
        void fail_getQuotationDetail_quotation_not_found(){
            //given
            given(recipeRepository.existsByIdAndMemberForQuotation(anyLong(), anyLong()))
                    .willReturn(true);
            given(recipeRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.getQuotationDetail(1L, 1L));

            //then
            assertEquals(QUOTATION_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("convertToRecipe() 테스트")
    class convertToRecipeTest{

        @Test
        @DisplayName("견적서를 꿀조합으로 변환 성공")
        void success_convertToRecipe(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(Recipe.builder().build()));

            given(recipeRepository.isAbleToConvert(any()))
                    .willReturn(true);

            RecipeDtoRequest request = RecipeDtoRequest.builder()
                    .title("제육볶음")
                    .content("아주 맛있는 제육볶음 견적서")
                    .mainImageUrl("https://s3.test.com/mainImage.png")
                    .recipeIngredientDtoList(
                            List.of(
                                    RecipeIngredientDto.builder()
                                            .name("앞다리살")
                                            .count(1)
                                            .unit("근")
                                            .build()
                            )
                    )
                    .difficulty(Difficulty.MEDIUM)
                    .recipeDetailDtoList(
                            List.of(
                                    RecipeDetailDto.builder()
                                            .cookingOrder("김치찌개~~")
                                            .cookingOrderImageUrl("https://s3.test.com/image.png")
                                            .build()
                            )
                    )
                    .expectedTime(10)
                    .build();

            //when
            quotationService.convertToRecipe(1L, 1L, request);

            //then
            ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
            verify(recipeRepository, times(1)).save(captor.capture());
            Recipe quotation = captor.getValue();

            assertAll(
                    () -> assertEquals(request.getTitle(), quotation.getSummary().getTitle()),
                    () -> assertEquals(request.getContent(), quotation.getSummary().getContent()),
                    () -> assertEquals(request.getMainImageUrl(), quotation.getMainImageUrl()),
                    () -> assertEquals(request.getExpectedTime(), quotation.getExpectedTime()),
                    () -> assertEquals(request.getDifficulty(), quotation.getDifficulty()),
                    () -> assertEquals(request.getRecipeDetailDtoList().get(0).getCookingOrder(),
                            quotation.getRecipeDetailList().get(0).getCookingOrder()),
                    () -> assertEquals(request.getRecipeDetailDtoList().get(0).getCookingOrderImageUrl(),
                            quotation.getRecipeDetailList().get(0).getCookingOrderImageUrl()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(),
                            quotation.getRecipeIngredientList().get(0).getName()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(),
                            quotation.getRecipeIngredientList().get(0).getCount()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(),
                            quotation.getRecipeIngredientList().get(0).getUnit()),
                    () -> assertFalse(quotation.getIsQuotation())
            );

        }

        @Test
        @DisplayName("견적서를 꿀조합으로 변환 실패 - 회원 X")
        void fail_convertToRecipe_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> quotationService.convertToRecipe(1L, 1L,
                            RecipeDtoRequest.builder().build()));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("견적서를 꿀조합으로 변환 실패 - 견적서 X")
        void fail_convertToRecipe_quotation_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.empty());

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.convertToRecipe(1L, 1L,
                            RecipeDtoRequest.builder().build()));

            //then
            assertEquals(QUOTATION_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("견적서를 꿀조합으로 변환 실패 - 거래가 성사 되지 않아 변환이 불가능함")
        void fail_convertToRecipe_cannot_convert_to_recipe(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(Recipe.builder().build()));

            given(recipeRepository.isAbleToConvert(any()))
                    .willReturn(false);

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.convertToRecipe(1L, 1L,
                            RecipeDtoRequest.builder().build()));

            //then
            assertEquals(CANNOT_CONVERT_TO_RECIPE, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("updateQuotation() 테스트")
    class updateQuotationTest{

        @Test
        @DisplayName("견적서 수정 성공")
        void success_updateQuotation(){
            //given
            Member member = Member.builder().id(1L).build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(Recipe.builder()
                            .id(1L)
                            .member(member)
                            .isQuotation(true)
                            .isPublic(true)
                            .build()));

            QuotationDtoRequest request = QuotationDtoRequest.builder()
                    .title("제육볶음")
                    .content("아주 맛있는 제육볶음 견적서")
                    .recipeIngredientDtoList(
                            List.of(
                                    RecipeIngredientDto.builder()
                                            .name("앞다리살")
                                            .count(1)
                                            .unit("근")
                                            .build()
                            )
                    )
                    .difficulty(Difficulty.MEDIUM)
                    .recipeDetailDtoList(
                            List.of(
                                    "프라이팬에 넣고 볶아줍니다."
                            )
                    )
                    .expectedTime(10)
                    .build();

            //when
            quotationService.updateQuotation(1L, 1L, request);

            //then
            ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
            verify(recipeRepository, times(1))
                    .save(captor.capture());

            Recipe quotation = captor.getValue();

            assertAll(
                    () -> assertEquals(request.getTitle(), quotation.getSummary().getTitle()),
                    () -> assertEquals(request.getContent(), quotation.getSummary().getContent()),
                    () -> assertEquals(request.getExpectedTime(), quotation.getExpectedTime()),
                    () -> assertEquals(request.getDifficulty(), quotation.getDifficulty()),
                    () -> assertTrue(quotation.getIsPublic()),
                    () -> assertEquals(1L, quotation.getMember().getId()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(), quotation.getRecipeIngredientList().get(0).getName()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(), quotation.getRecipeIngredientList().get(0).getUnit()),
                    () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(), quotation.getRecipeIngredientList().get(0).getCount()),
                    () -> assertEquals(request.getRecipeDetailDtoList().get(0), quotation.getRecipeDetailList().get(0).getCookingOrder()),
                    () -> assertEquals(true, quotation.getIsQuotation())
            );
        }

        @Test
        @DisplayName("견적서 수정 실패 - 회원 X")
        void fail_updateQuotation_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> quotationService.updateQuotation(1L, 1L,
                            QuotationDtoRequest.builder().build()));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("견적서 수정 실패 - 견적서 X")
        void fail_updateQuotation_quotation_not_found(){
            //given
            Member member = Member.builder().id(1L).build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.empty());
            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.updateQuotation(1L, 1L,
                            QuotationDtoRequest.builder().build()));

            //then
            assertEquals(QUOTATION_NOT_FOUND, exception.getErrorCode());
        }


    }

    @Nested
    @DisplayName("deleteQuotation() 테스트")
    class deleteQuotationTest{

        @Test
        @DisplayName("견적서 삭제 성공")
        void success_deleteQuotation(){
            //given
            Recipe quotation = Recipe.builder()
                    .id(1L)
                    .isQuotation(true)
                    .build();
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));
            given(recipeRepository.isNotAbleToDeleteForQuotation(anyLong(), anyLong()))
                    .willReturn(false);
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(quotation));

            //when
            quotationService.deleteQuotation(1L, 1L);

            //then
            ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
            verify(recipeRepository, times(1)).delete(captor.capture());

            assertEquals(quotation, captor.getValue());

        }

        @Test
        @DisplayName("견적서 삭제 실패 - 회원 X")
        void fail_deleteQuotation_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> quotationService.deleteQuotation(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("견적서 삭제 실패 - 요청에 포함되어, 삭제 불가능한 상태")
        void fail_deleteQuotation_cannot_delete_is_locked(){
            //given
            given(recipeRepository.isNotAbleToDeleteForQuotation(anyLong(), anyLong()))
                    .willReturn(true);

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.deleteQuotation(1L, 1L));

            //then
            assertEquals(CANNOT_DELETE_IS_LOCKED, exception.getErrorCode());
        }

        @Test
        @DisplayName("견적서 삭제 실패 - 견적서 X")
        void fail_deleteQuotation_quotation_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));
            given(recipeRepository.isNotAbleToDeleteForQuotation(anyLong(), anyLong()))
                    .willReturn(false);
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.empty());
            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.deleteQuotation(1L, 1L));

            //then
            assertEquals(QUOTATION_NOT_FOUND, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("getQuotationForSend() 테스트")
    class getQuotationForSendTest{

        @Test
        @DisplayName("전송을 위한 견적서 가져오기 성공")
        void success_getQuotationForSend(){
            //given
            Recipe quotation = Recipe.builder()
                    .id(1L)
                    .isQuotation(true)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().id(1L).build()));
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(quotation));
            given(requestRepository.existsByRecipe(any()))
                    .willReturn(false);

            //when
            Recipe response = quotationService.getQuotationForSend(1L, 1L);

            assertEquals(quotation, response);

        }

        @Test
        @DisplayName("전송을 위한 견적서 가져오기 실패 - 회원 X")
        void fail_getQuotationForSend_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> quotationService.getQuotationForSend(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("전송을 위한 견적서 가져오기 실패 - 견적서 X")
        void fail_getQuotationForSend_quotation_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().id(1L).build()));
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.empty());

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.getQuotationForSend(1L, 1L));

            //then
            assertEquals(QUOTATION_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("전송을 위한 견적서 가져오기 실패 - 이미 보낸 견적서")
        void fail_getQuotationForSend_already_sent_quotation(){
            //given
            Recipe quotation = Recipe.builder()
                    .id(1L)
                    .isQuotation(true)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().id(1L).build()));
            given(recipeRepository.findByIdAndMemberAndIsQuotationTrue(anyLong(), any()))
                    .willReturn(Optional.of(quotation));
            given(requestRepository.existsByRecipe(any()))
                    .willReturn(true);

            //when
            QuotationException exception = assertThrows(QuotationException.class,
                    () -> quotationService.getQuotationForSend(1L, 1L));

            //then
            assertEquals(ALREADY_SENT_QUOTATION, exception.getErrorCode());
        }

    }
}