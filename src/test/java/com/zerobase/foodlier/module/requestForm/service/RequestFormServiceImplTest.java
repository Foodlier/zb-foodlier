package com.zerobase.foodlier.module.requestForm.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDetailDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormResponseDto;
import com.zerobase.foodlier.module.requestform.exception.RequestFormException;
import com.zerobase.foodlier.module.requestform.repository.RequestFormRepository;
import com.zerobase.foodlier.module.requestform.service.RequestFormServiceImpl;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.*;
import static com.zerobase.foodlier.module.requestForm.service.RequestFormServiceImplTest.Constants.*;
import static com.zerobase.foodlier.module.requestform.exception.RequestFormErrorCode.REQUEST_FORM_NOT_FOUND;
import static com.zerobase.foodlier.module.requestform.exception.RequestFormErrorCode.REQUEST_FORM_PERMISSION_DENIED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestFormServiceImplTest {

    @Mock
    private RequestFormRepository requestFormRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RequestFormServiceImpl requestFormService;

    static class Constants {
        static Long constRequesterId = 1L;
        static Long constRecipeId = 2L;
        static Long constMemberId = 1L;
        static Long constWrongMemberId = 13L;
        static Long constRequestFormId = 10L;
        static Member constMember = Member.builder()
                .id(constMemberId)
                .address(Address.builder()
                        .roadAddress("road")
                        .addressDetail("detail")
                        .build())
                .nickname("nickname")
                .build();
        static Recipe constRecipe = Recipe.builder()
                .id(constRecipeId)
                .isPublic(true)
                .isDeleted(false)
                .isQuotation(false)
                .mainImageUrl("url")
                .summary(Summary.builder()
                        .title("title")
                        .content("content")
                        .build())
                .heartCount(0)
                .build();
        static Recipe constRecipeNotPublic = Recipe.builder()
                .id(constRecipeId)
                .isPublic(false)
                .isDeleted(false)
                .isQuotation(false)
                .build();
        static Recipe constRecipeDeleted = Recipe.builder()
                .id(constRecipeId)
                .isPublic(true)
                .isDeleted(true)
                .isQuotation(false)
                .build();
        static Recipe constRecipeQuotation = Recipe.builder()
                .id(constRecipeId)
                .isPublic(true)
                .isDeleted(false)
                .isQuotation(true)
                .build();
        static RequestFormDto constRequestFormDto = RequestFormDto.builder()
                .title("title")
                .content("content")
                .expectedPrice(30000L)
                .expectedAt(LocalDateTime.now())
                .ingredientList(new ArrayList<>(List.of("양파")))
                .build();
        static RequestFormDto constUpdateRequestFormDtoIncludeRecipeId = RequestFormDto.builder()
                .recipeId(constRecipeId)
                .title("changeTitle")
                .content("changeContent")
                .expectedPrice(30L)
                .expectedAt(LocalDateTime.now().minusDays(1))
                .ingredientList(new ArrayList<>(List.of("양파볶음")))
                .build();
        static RequestFormDto constRequestFormDtoIncludeRecipeId = RequestFormDto.builder()
                .recipeId(constRecipeId)
                .title("title")
                .content("content")
                .expectedPrice(30000L)
                .expectedAt(LocalDateTime.now())
                .ingredientList(new ArrayList<>(List.of("양파")))
                .build();
        static RequestForm constRequestForm = RequestForm.builder()
                .id(constRequestFormId)
                .title("title")
                .content("content")
                .ingredientList(new ArrayList<>(List.of(Ingredient.builder()
                        .ingredientName("과자")
                        .build())))
                .expectedPrice(3000L)
                .expectedAt(LocalDateTime.now())
                .member(constMember)
                .recipe(constRecipe)
                .build();
        static RequestForm constRequestFormWithWrongMemberId = RequestForm.builder()
                .id(constRequestFormId)
                .title("title")
                .content("content")
                .ingredientList(new ArrayList<>(List.of(Ingredient.builder()
                        .ingredientName("과자")
                        .build())))
                .expectedPrice(3000L)
                .expectedAt(LocalDateTime.now())
                .member(Member.builder()
                        .id(constWrongMemberId)
                        .build())
                .recipe(constRecipe)
                .build();
    }

    @Test
    @DisplayName("요청서 작성 성공 - 레시피 태그하지 않음")
    void success_create_request_form_no_tag() {
        //given
        Long id = constRequesterId;
        Member member = constMember;
        RequestFormDto requestFormDto = constRequestFormDto;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));

        //when
        requestFormService.createRequestForm(1L, requestFormDto);

        //then
        ArgumentCaptor<RequestForm> captor = ArgumentCaptor.forClass(RequestForm.class);
        verify(requestFormRepository, times(1)).save(captor.capture());
        RequestForm requestForm = captor.getValue();

        assertAll(
                () -> assertEquals(requestFormDto.getTitle(), requestForm.getTitle()),
                () -> assertEquals(requestFormDto.getContent(), requestForm.getContent()),
                () -> assertEquals(requestFormDto.getExpectedPrice(), requestForm.getExpectedPrice()),
                () -> assertEquals(requestFormDto.getExpectedAt(), requestForm.getExpectedAt()),
                () -> assertEquals(requestFormDto.getIngredientList().get(0),
                        requestForm.getIngredientList().get(0).getIngredientName()),
                () -> assertEquals(member, requestForm.getMember()),
                () -> assertNull(requestForm.getRecipe())
        );
    }

    @Test
    @DisplayName("요청서 작성 성공 - 레시피 태그")
    void success_create_request_form_tag() {
        //given
        Long id = constRequesterId;
        Member member = constMember;
        Recipe recipe = constRecipe;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(recipe));

        //when
        requestFormService.createRequestForm(1L, requestFormDto);

        //then
        ArgumentCaptor<RequestForm> captor = ArgumentCaptor.forClass(RequestForm.class);
        verify(requestFormRepository, times(1)).save(captor.capture());
        RequestForm requestForm = captor.getValue();

        assertAll(
                () -> assertEquals(requestFormDto.getTitle(), requestForm.getTitle()),
                () -> assertEquals(requestFormDto.getContent(), requestForm.getContent()),
                () -> assertEquals(requestFormDto.getExpectedPrice(), requestForm.getExpectedPrice()),
                () -> assertEquals(requestFormDto.getExpectedAt(), requestForm.getExpectedAt()),
                () -> assertEquals(requestFormDto.getIngredientList().get(0),
                        requestForm.getIngredientList().get(0).getIngredientName()),
                () -> assertEquals(member, requestForm.getMember()),
                () -> assertEquals(recipe, requestForm.getRecipe())
        );
    }

    @Test
    @DisplayName("요청서 작성 실패 - 사용자 없음")
    void fail_create_request_member_not_found() {
        //given
        Long id = constRequesterId;
        RequestFormDto requestFormDto = constRequestFormDto;

        given(memberRepository.findById(id)).willThrow(new MemberException(MEMBER_NOT_FOUND));

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> requestFormService.createRequestForm(id, requestFormDto));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }


    @Test
    @DisplayName("요청서 작성 실패 - 레시피 없음")
    void fail_create_request_no_such_recipe() {
        //given
        Long id = constRequesterId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;
        Member member = constMember;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.createRequestForm(id, requestFormDto));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 작성 실패 - 공개되지 않은 레시피")
    void fail_create_request_not_public_recipe() {
        //given
        Long id = constRequesterId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;
        Member member = constMember;
        Recipe recipe = constRecipeNotPublic;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(recipe));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.createRequestForm(id, requestFormDto));

        //then
        assertEquals(NOT_PUBLIC_RECIPE, recipeException.getErrorCode());

    }

    @Test
    @DisplayName("요청서 작성 실패 - 삭제된 레시피")
    void fail_create_request_is_deleted() {
        //given
        Long id = constRequesterId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;
        Member member = constMember;
        Recipe recipe = constRecipeDeleted;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(recipe));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.createRequestForm(id, requestFormDto));

        //then
        assertEquals(DELETED_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 작성 실패 - 견적서")
    void fail_create_request_() {
        //given
        Long id = constRequesterId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;
        Member member = constMember;
        Recipe recipe = constRecipeQuotation;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(recipe));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.createRequestForm(id, requestFormDto));

        //then
        assertEquals(QUOTATION_CANNOT_BE_TAGGED, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 가져오기 성공")
    void success_get_my_request_form() {
        //given
        Long id = constRequesterId;
        int pageIdx = 0;
        int pageSize = 2;
        Member member = constMember;
        PageRequest pageRequest = PageRequest.of(pageIdx, pageSize);
        Page<RequestForm> requestFormPage = new PageImpl<>(List.of(
                new RequestForm(), new RequestForm()));

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(requestFormRepository.findAllByMemberOrderByCreatedAtDesc(member,
                pageRequest)).willReturn(requestFormPage);
        //when
        Page<RequestFormResponseDto> resultPage = requestFormService
                .getMyRequestForm(id, pageIdx, pageSize);

        //then
        assertEquals(2, resultPage.getTotalElements());
    }

    @Test
    @DisplayName("요청서 가져오기 실패 - 사용자 없음")
    void fail_get_my_request_form_member_not_found() {
        //given
        Long id = constRequesterId;
        int pageIdx = 0;
        int pageSize = 2;

        given(memberRepository.findById(id)).willThrow(new MemberException(MEMBER_NOT_FOUND));

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> requestFormService.getMyRequestForm(id, pageIdx, pageSize));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("상세 요청서 가져오기 성공")
    void success_get_request_form_detail() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestForm requestForm = constRequestForm;

        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(requestForm));

        //when
        RequestFormDetailDto requestFormDetailDto = requestFormService
                .getRequestFormDetail(id, requestFormId);

        //then
        assertAll(
                () -> assertEquals(requestForm.getId(), requestFormDetailDto.getRequestFormId()),
                () -> assertEquals(requestForm.getTitle(), requestFormDetailDto.getTitle()),
                () -> assertEquals(requestForm.getContent(), requestFormDetailDto.getContent()),
                () -> assertEquals(requestForm.getIngredientList().get(0).getIngredientName(),
                        requestFormDetailDto.getIngredientList().get(0)),
                () -> assertEquals(requestForm.getExpectedPrice(), requestFormDetailDto.getExpectedPrice()),
                () -> assertEquals(requestForm.getExpectedAt(), requestFormDetailDto.getExpectedAt()),
                () -> assertEquals(requestForm.getMember().getAddress().getRoadAddress(), requestFormDetailDto.getAddress()),
                () -> assertEquals(requestForm.getMember().getAddress().getAddressDetail(), requestFormDetailDto.getAddressDetail()),
                () -> assertEquals(requestForm.getMember().getAddress().getAddressDetail(), requestFormDetailDto.getAddressDetail()),
                () -> assertEquals(requestForm.getRecipe().getMainImageUrl(), requestFormDetailDto.getMainImageUrl()),
                () -> assertEquals(requestForm.getRecipe().getSummary().getTitle(), requestFormDetailDto.getRecipeTitle()),
                () -> assertEquals(requestForm.getRecipe().getSummary().getContent(), requestFormDetailDto.getRecipeContent()),
                () -> assertEquals(requestForm.getRecipe().getHeartCount(), requestFormDetailDto.getHeartCount()),
                () -> assertEquals(requestForm.getMember().getNickname(), requestFormDetailDto.getRequesterNickname())
        );
    }

    @Test
    @DisplayName("상세 요청서 가져오기 실패 - 요청서 없음")
    void fail_get_request_form_detail_request_form_not_found() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;

        given(requestFormRepository.findById(constRequestFormId))
                .willThrow(new RequestFormException(REQUEST_FORM_NOT_FOUND));

        //when
        RequestFormException requestException = assertThrows(RequestFormException.class,
                () -> requestFormService.getRequestFormDetail(id, requestFormId));

        //then
        assertEquals(REQUEST_FORM_NOT_FOUND, requestException.getErrorCode());
    }

    @Test
    @DisplayName("상세 요청서 가져오기 실패 - 접근권한 없음")
    void fail_get_request_form_detail_request_form_permission_denied() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestForm requestForm = constRequestFormWithWrongMemberId;

        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(requestForm));

        //when
        RequestFormException requestException = assertThrows(RequestFormException.class,
                () -> requestFormService.getRequestFormDetail(id, requestFormId));

        //then
        assertEquals(REQUEST_FORM_PERMISSION_DENIED, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 성공")
    void success_update_request_form() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        Member member = constMember;
        Recipe recipe = constRecipe;
        RequestFormDto updateRequestFormDto = constUpdateRequestFormDtoIncludeRecipeId;
        RequestForm requestForm = constRequestForm;

        given(memberRepository.findById(id)).willReturn(Optional.of(member));
        given(recipeRepository.findById(updateRequestFormDto.getRecipeId()))
                .willReturn(Optional.of(recipe));
        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(requestForm));

        //when
        requestFormService.updateRequestForm(id, updateRequestFormDto, requestFormId);

        //then
        ArgumentCaptor<RequestForm> captor = ArgumentCaptor.forClass(RequestForm.class);
        verify(requestFormRepository, times(1)).save(captor.capture());
        RequestForm updatedRequestForm = captor.getValue();
        assertAll(
                () -> assertEquals(updateRequestFormDto.getTitle(), updatedRequestForm.getTitle()),
                () -> assertEquals(updateRequestFormDto.getContent(), updatedRequestForm.getContent()),
                () -> assertEquals(updateRequestFormDto.getExpectedPrice(), updatedRequestForm.getExpectedPrice()),
                () -> assertEquals(updateRequestFormDto.getExpectedAt(), updatedRequestForm.getExpectedAt()),
                () -> assertEquals(updateRequestFormDto.getIngredientList().get(0),
                        updatedRequestForm.getIngredientList().get(0).getIngredientName())
        );
    }

    @Test
    @DisplayName("요청서 수정 실패 - 사용자 없음")
    void fail_update_request_form_member_not_found() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 레시피 없음")
    void fail_update_request_form_no_such_recipe() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willThrow(new RecipeException(NO_SUCH_RECIPE));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(NO_SUCH_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 공개되지 않은 레시피")
    void fail_update_request_form_not_public_recipe() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(constRecipeNotPublic));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(NOT_PUBLIC_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 삭제된 레시피")
    void fail_update_request_form_deleted_recipe() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(constRecipeDeleted));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(DELETED_RECIPE, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 견적서")
    void fail_update_request_form_quotation() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(constRecipeQuotation));

        //when
        RecipeException recipeException = assertThrows(RecipeException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(QUOTATION_CANNOT_BE_TAGGED, recipeException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 요청서 찾기 실패")
    void fail_update_request_form() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(constRecipe));
        given(requestFormRepository.findById(requestFormId))
                .willThrow(new RequestFormException(REQUEST_FORM_NOT_FOUND));

        //when
        RequestFormException requestFormException = assertThrows(RequestFormException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(REQUEST_FORM_NOT_FOUND, requestFormException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 수정 실패 - 요청자와 요청서 작성자가 다름")
    void fail_update_request_form_request_form_permission_denied() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;
        RequestFormDto requestFormDto = constRequestFormDtoIncludeRecipeId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(recipeRepository.findById(requestFormDto.getRecipeId()))
                .willReturn(Optional.of(constRecipe));
        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(constRequestFormWithWrongMemberId));

        //when
        RequestFormException requestFormException = assertThrows(RequestFormException.class,
                () -> requestFormService.updateRequestForm(id, requestFormDto, requestFormId));

        //then
        assertEquals(REQUEST_FORM_PERMISSION_DENIED, requestFormException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 삭제 성공")
    void success_delete_request_form() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(constRequestForm));

        //when
        requestFormService.deleteRequestForm(id, requestFormId);

        //then
        verify(requestFormRepository, times(1)).deleteById(requestFormId);
    }

    @Test
    @DisplayName("요청서 삭제 실패 - 사용자 없음")
    void fail_delete_request_form_member_not_found() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;

        given(memberRepository.findById(id))
                .willThrow(new MemberException(MEMBER_NOT_FOUND));

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> requestFormService.deleteRequestForm(id, requestFormId));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 삭제 실패 - 요청서 없음")
    void fail_delete_request_form_request_form_not_found() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(requestFormRepository.findById(requestFormId))
                .willThrow(new RequestFormException(REQUEST_FORM_NOT_FOUND));

        //when
        RequestFormException requestFormException = assertThrows(RequestFormException.class,
                () -> requestFormService.deleteRequestForm(id, requestFormId));

        //then
        assertEquals(REQUEST_FORM_NOT_FOUND, requestFormException.getErrorCode());
    }

    @Test
    @DisplayName("요청서 삭제 실패 - 요청자와 요청서 작성자 다름")
    void fail_delete_request_form_request_form_permission_denied() {
        //given
        Long id = constRequesterId;
        Long requestFormId = constRequestFormId;

        given(memberRepository.findById(id))
                .willReturn(Optional.of(constMember));
        given(requestFormRepository.findById(requestFormId))
                .willReturn(Optional.of(constRequestFormWithWrongMemberId));

        //when
        RequestFormException requestFormException = assertThrows(RequestFormException.class,
                () -> requestFormService.deleteRequestForm(id, requestFormId));

        //then
        assertEquals(REQUEST_FORM_PERMISSION_DENIED, requestFormException.getErrorCode());
    }
}
