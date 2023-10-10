package com.zerobase.foodlier.module.review.recipe.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.review.recipe.dto.ChangedRecipeReviewResponse;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewRequestDto;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewService {

    void createRecipeReview(Long memberId, Long recipeId,
                                   RecipeReviewRequestDto request);
    RecipeReviewResponseDto getMyRecipeReview(Long memberId, Long recipeId);
    ListResponse<RecipeReviewResponseDto> getRecipeReviewList(Long memberId, Long recipeId,
                                                              Pageable pageable);
    ListResponse<RecipeReviewResponseDto> getRecipeReviewForProfile(Long memberId,
                                                            Pageable pageable);
    RecipeReviewResponseDto getReviewDetail(Long recipeReviewId);
    ChangedRecipeReviewResponse updateRecipeReview(Long memberId, Long recipeReviewId,
                              RecipeReviewRequestDto request);
    ChangedRecipeReviewResponse deleteRecipeReview(Long memberId, Long recipeReviewId);



}
