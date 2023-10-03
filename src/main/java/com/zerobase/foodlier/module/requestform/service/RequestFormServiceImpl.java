package com.zerobase.foodlier.module.requestform.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDetailDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormResponseDto;
import com.zerobase.foodlier.module.requestform.exception.RequestFormException;
import com.zerobase.foodlier.module.requestform.repository.RequestFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.*;
import static com.zerobase.foodlier.module.requestform.exception.RequestFormErrorCode.REQUEST_FORM_NOT_FOUND;
import static com.zerobase.foodlier.module.requestform.exception.RequestFormErrorCode.REQUEST_FORM_PERMISSION_DENIED;

@Service
@RequiredArgsConstructor
public class RequestFormServiceImpl implements RequestFormService {

    private final RequestFormRepository requestFormRepository;
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private static final Long ZERO_RECIPE_ID = 0L;

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 요청서 작성, 삭제된 레시피, 공개되지 않은 레시피, 견적서를 체크
     */
    @Override
    @Transactional
    public void createRequestForm(Long id, RequestFormDto requestFormDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Recipe recipe = null;

        if (!Objects.isNull(requestFormDto.getRecipeId()) &&
                !requestFormDto.getRecipeId().equals(ZERO_RECIPE_ID)) {
            recipe = recipeRepository.findById(requestFormDto.getRecipeId())
                    .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

            checkValidation(recipe);
        }

        requestFormRepository.save(RequestForm.builder()
                .title(requestFormDto.getTitle())
                .content(requestFormDto.getContent())
                .expectedPrice(requestFormDto.getExpectedPrice())
                .expectedAt(requestFormDto.getExpectedAt())
                .ingredientList(requestFormDto.getIngredientList()
                        .stream()
                        .map(ingredient -> Ingredient.builder()
                                .ingredientName(ingredient)
                                .build())
                        .collect(Collectors.toList()))
                .member(member)
                .recipe(recipe)
                .build());
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 작성한 요청서 반환
     */
    @Override
    public Page<RequestFormResponseDto> getMyRequestForm(
            Long id, int pageIdx, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIdx, pageSize);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Page<RequestForm> requestFormPage = requestFormRepository
                .findAllByMemberOrderByCreatedAtDesc(member, pageRequest);
        return requestFormPage.map(RequestFormResponseDto::fromEntity);
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 상세 요청서 반환
     */
    @Override
    public RequestFormDetailDto getRequestFormDetail(Long id, Long requestFormId) {
        RequestForm requestForm = requestFormRepository.findById(requestFormId)
                .orElseThrow(() -> new RequestFormException(REQUEST_FORM_NOT_FOUND));
        checkPermission(id, requestForm.getMember().getId());
        RequestFormDetailDto.RequestFormDetailDtoBuilder builder = RequestFormDetailDto.builder()
                .requestFormId(requestForm.getId())
                .requesterNickname(requestForm.getMember().getNickname())
                .title(requestForm.getTitle())
                .content(requestForm.getContent())
                .ingredientList(requestForm.getIngredientList().stream()
                        .map(Ingredient::getIngredientName)
                        .collect(Collectors.toList()))
                .expectedPrice(requestForm.getExpectedPrice())
                .expectedAt(requestForm.getExpectedAt())
                .address(requestForm.getMember().getAddress().getRoadAddress())
                .addressDetail(requestForm.getMember().getAddress().getAddressDetail());

        if (!Objects.isNull(requestForm.getRecipe())) {
            builder.mainImageUrl(requestForm.getRecipe().getMainImageUrl())
                    .recipeTitle(requestForm.getRecipe().getSummary().getTitle())
                    .recipeContent(requestForm.getRecipe().getSummary().getContent())
                    .heartCount(requestForm.getRecipe().getHeartCount());
        }

        return builder.build();
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 요청서 업데이트, 태그된 레시피 또한 업데이트, 변경 시 권한 검증
     */
    @Override
    @Transactional
    public void updateRequestForm(Long id, RequestFormDto requestFormDto, Long requestFormId) {
        memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Recipe recipe = null;

        if (!Objects.isNull(requestFormDto.getRecipeId()) &&
                !requestFormDto.getRecipeId().equals(ZERO_RECIPE_ID)) {
            recipe = recipeRepository.findById(requestFormDto.getRecipeId())
                    .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

            checkValidation(recipe);
        }
        RequestForm requestForm = requestFormRepository.findById(requestFormId)
                .orElseThrow(() -> new RequestFormException(REQUEST_FORM_NOT_FOUND));
        checkPermission(id, requestForm.getMember().getId());

        requestForm.setTitle(requestFormDto.getTitle());
        requestForm.setContent(requestFormDto.getContent());
        requestForm.setExpectedPrice(requestFormDto.getExpectedPrice());
        requestForm.setExpectedAt(requestFormDto.getExpectedAt());
        requestForm.setIngredientList(requestFormDto.getIngredientList()
                .stream()
                .map(ingredient -> Ingredient.builder()
                        .ingredientName(ingredient)
                        .build())
                .collect(Collectors.toList()));
        requestForm.setRecipe(recipe);

        requestFormRepository.save(requestForm);
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 요청서 삭제, 삭제 시 권한 검증
     */
    @Override
    @Transactional
    public void deleteRequestForm(Long id, Long requestFormId) {
        memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        RequestForm requestForm = requestFormRepository.findById(requestFormId)
                .orElseThrow(() -> new RequestFormException(REQUEST_FORM_NOT_FOUND));
        checkPermission(id, requestForm.getMember().getId());

        requestFormRepository.deleteById(requestFormId);
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 요청서 작성후 해당 요청서에 태그된 레시피가 태그 가능한 레시피인지 검증
     */
    private void checkValidation(Recipe recipe) {
        if (!recipe.getIsPublic()) {
            throw new RecipeException(NOT_PUBLIC_RECIPE);
        }
        if (recipe.getIsDeleted()) {
            throw new RecipeException(DELETED_RECIPE);
        }
        if (recipe.getIsQuotation()) {
            throw new RecipeException(QUOTATION_CANNOT_BE_TAGGED);
        }
    }

    /**
     * 작성일 : 2023-09-29
     * 작성자 : 황태원
     * 요청서 가져오기, 수정, 삭제시 해당 요청서에 대한 권한 검증
     */
    private void checkPermission(Long id, Long memberId) {
        if (!Objects.equals(id, memberId)) {
            throw new RequestFormException(REQUEST_FORM_PERMISSION_DENIED);
        }
    }
}
