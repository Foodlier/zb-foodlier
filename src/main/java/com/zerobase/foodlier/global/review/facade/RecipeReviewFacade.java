package com.zerobase.foodlier.global.review.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.review.recipe.dto.ChangedRecipeReviewResponse;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewRequestDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class RecipeReviewFacade {

    private final RecipeReviewService recipeReviewService;
    private final RecipeService recipeService;
    private final S3Service s3Service;

    public void createRecipeReview(Long memberId, Long recipeId,
                                   RecipeReviewForm request){

        String cookImageUrl = request.getCookImage() != null ?
                s3Service.getImageUrl(request.getCookImage()) : null;

        recipeReviewService.createRecipeReview(memberId, recipeId,
                RecipeReviewRequestDto.from(request, cookImageUrl));

        recipeService.plusReviewStar(recipeId, request.getStar());

    }

    public void updateRecipeReview(Long memberId, Long recipeReviewId,
                                   RecipeReviewForm request){

        //우선적으로 이미지 업로드
        String cookImageUrl = request.getCookImage() != null ?
                s3Service.getImageUrl(request.getCookImage()) : null;

        ChangedRecipeReviewResponse updatedResponse = recipeReviewService
                .updateRecipeReview(memberId, recipeReviewId,
                RecipeReviewRequestDto.from(request, cookImageUrl));

        if(updatedResponse.getCookImageUrl() != null &&
                request.getCookImage() != null){
            s3Service.deleteImage(updatedResponse.getCookImageUrl());
        }

        recipeService.updateReviewStar(updatedResponse.getRecipeId(),
                updatedResponse.getStar(),
                request.getStar());

    }

    public void deleteRecipeReview(Long memberId, Long recipeReviewId){

        ChangedRecipeReviewResponse deletedResponse = recipeReviewService
                .deleteRecipeReview(memberId, recipeReviewId);

        if(deletedResponse.getCookImageUrl() != null){
            s3Service.deleteImage(deletedResponse.getCookImageUrl());
        }

        recipeService.minusReviewStar(deletedResponse.getRecipeId(),
                deletedResponse.getStar());

    }



}
