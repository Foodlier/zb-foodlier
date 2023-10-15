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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static com.zerobase.foodlier.module.review.recipe.exception.RecipeReviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class RecipeReviewServiceImpl implements RecipeReviewService{

    private final RecipeReviewRepository recipeReviewRepository;
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합에 대해 후기를 작성합니다. (꿀조합당 1개씩만 작성 가능함)
     */
    @Override
    public void createRecipeReview(Long memberId, Long recipeId,
                                   RecipeReviewRequestDto request){

        Member member = getMember(memberId);
        Recipe recipe = getRecipe(recipeId);

        validateCreateRecipeReview(member, recipe);

        RecipeReview recipeReview = RecipeReview.builder()
                .recipe(recipe)
                .member(member)
                .content(request.getContent())
                .cookUrl(request.getCookImageUrl())
                .star(request.getStar())
                .build();

        recipeReviewRepository.save(recipeReview);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합에 대한 자신의 후기를 가져옵니다.
     */
    @Override
    public RecipeReviewResponseDto getMyRecipeReview(Long memberId, Long recipeId){
        return RecipeReviewResponseDto.from(recipeReviewRepository
                .findByMemberAndRecipe(getMember(memberId), getRecipe(recipeId))
                .orElseThrow(() -> new RecipeReviewException(RECIPE_REVIEW_NOT_FOUND)));
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합의 후기를 페이징 하여 가져옵니다.
     */
    @Override
    public ListResponse<RecipeReviewResponseDto> getRecipeReviewList(Long memberId,
                                                                     Long recipeId,
                                                                     Pageable pageable){
        return ListResponse.from(
                recipeReviewRepository.findRecipe(recipeId, memberId, pageable),
                RecipeReviewResponseDto::from
        );
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-08
     *  공개 프로필에서 해당 멤버에게 달린 레시피 후기를 날짜 내림차순으로 보여줌.
     */
    public ListResponse<RecipeReviewResponseDto> getRecipeReviewForProfile(Long memberId,
                                                                   Pageable pageable){
        Member member = getMember(memberId);
        return ListResponse.from(
                recipeReviewRepository.findByMemberOrderByCreatedAtDesc(member, pageable),
                RecipeReviewResponseDto::from);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합 후기 수정을 위해 상세 정보를 조회합니다 (존재 여부 검토 필요)
     */
    @Override
    public RecipeReviewResponseDto getReviewDetail(Long recipeReviewId){
        return RecipeReviewResponseDto.from(getRecipeReview(recipeReviewId));
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합 후기를 수정합니다.
     */
    @Override
    public ChangedRecipeReviewResponse updateRecipeReview(Long memberId, Long recipeReviewId,
                                   RecipeReviewRequestDto request){
        RecipeReview recipeReview = getRecipeReviewForManage(memberId, recipeReviewId);

        int originStar = recipeReview.getStar();
        String originCookUrl = recipeReview.getCookUrl();

        recipeReview.setContent(request.getContent());
        recipeReview.setStar(request.getStar());

        if(StringUtils.hasText(request.getCookImageUrl())){
            recipeReview.setCookUrl(request.getCookImageUrl());
        }

        recipeReviewRepository.save(recipeReview);
        return ChangedRecipeReviewResponse.builder()
                .recipeId(recipeReview.getRecipe().getId())
                .cookImageUrl(originCookUrl)
                .star(originStar)
                .build();
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-04
     *  꿀조합 후기를 삭제합니다.
     */
    @Override
    public ChangedRecipeReviewResponse deleteRecipeReview(Long memberId, Long recipeReviewId){
        RecipeReview recipeReview = getRecipeReviewForManage(memberId, recipeReviewId);
        Long recipeId = recipeReview.getRecipe().getId();
        String cookImageUrl = recipeReview.getCookUrl();
        int originStar = recipeReview.getStar();
        recipeReviewRepository.delete(recipeReview);

        return ChangedRecipeReviewResponse.builder()
                .recipeId(recipeId)
                .cookImageUrl(cookImageUrl)
                .star(originStar)
                .build();
    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
    private Recipe getRecipe(Long recipeId){
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
    }
    private RecipeReview getRecipeReview(Long recipeReviewId){
        return recipeReviewRepository.findById(recipeReviewId)
                .orElseThrow(() -> new RecipeReviewException(RECIPE_REVIEW_NOT_FOUND));
    }
    private RecipeReview getRecipeReviewForManage(Long memberId, Long recipeReviewId){
        return recipeReviewRepository.findByIdAndMember(recipeReviewId, getMember(memberId))
                .orElseThrow(() -> new RecipeReviewException(RECIPE_REVIEW_NOT_FOUND));
    }

    //====================== Validates ========================

    private void validateCreateRecipeReview(Member member, Recipe recipe){
        if(recipe.getIsQuotation() || !recipe.getIsPublic()){
            throw new RecipeReviewException(QUOTATION_OR_IS_NOT_PUBLIC);
        }
        if(recipeReviewRepository.existsByMemberAndRecipe(member, recipe)){
            throw new RecipeReviewException(ALREADY_WRITTEN_RECIPE_REVIEW);
        }
    }

}
