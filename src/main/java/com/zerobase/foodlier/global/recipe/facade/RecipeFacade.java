package com.zerobase.foodlier.global.recipe.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeImageResponse;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_PERMISSION;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;

@Component
@RequiredArgsConstructor
public class RecipeFacade {
    RecipeService recipeService;
    MemberService memberService;
    S3Service s3Service;

    /**
     * 2023-09-25
     * 황태원
     * 꿀조합 게시글 등록 시 사진을 s3에 등록 후 url을 return
     */
    public RecipeImageResponse uploadRecipeImage(
            MultipartFile mainImage,
            List<MultipartFile> cookingOrderImageList) {
        return RecipeImageResponse.builder()
                .mainImage(s3Service.getImageUrl(mainImage))
                .cookingOrderImageList(s3Service
                        .getImageUrlList(cookingOrderImageList))
                .build();
    }

    /**
     * 2023-09-26
     * 황태원
     * 꿀조합 게시글 수정 시 사진을 s3에 등록 후 url을 return
     */
    public RecipeImageResponse updateRecipeImage(
            String email,
            MultipartFile mainImage,
            List<MultipartFile> cookingOrderImageList, Long id) {
        checkPermission(email, id);

        return RecipeImageResponse.builder()
                .mainImage(s3Service.getImageUrl(mainImage))
                .cookingOrderImageList(s3Service
                        .getImageUrlList(cookingOrderImageList))
                .build();
    }

    /**
     * 2023-09-25
     * 황태원
     * 꿀조합 게시글 작성
     */
    public void createRecipe(String email, RecipeDtoRequest recipeDtoRequest) {
        Member member = memberService.getMember(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        recipeService.createRecipe(member, recipeDtoRequest);
    }

    /**
     * 2023-09-26
     * 황태원
     * 꿀조합 게시글 수정
     */
    @Transactional
    public void updateRecipe(String email, RecipeDtoRequest recipeDtoRequest, Long id) {
        checkPermission(email, id);
        recipeService.updateRecipe(recipeDtoRequest, id);
        deleteRecipeImage(id);
    }

    /**
     * 2023-09-26
     * 황태원
     * 꿀조합 게시글 삭제, 삭제 후 s3이미지 삭제
     */
    @Transactional
    public void deleteRecipe(String email, Long id) {
        checkPermission(email, id);

        recipeService.deleteRecipe(id);
        deleteRecipeImage(id);
    }

    /**
     * 2023-09-26
     * 황태원
     * 꿀조합 게시글 삭제 및 수정 후 s3이미지 삭제
     */
    private void deleteRecipeImage(Long id) {
        Recipe recipe = recipeService.getRecipe(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        s3Service.deleteImage(recipe.getMainImageUrl());
        recipe.getRecipeDetailList()
                .forEach(recipeDetail
                        -> s3Service.deleteImage(recipeDetail.getCookingOrderImageUrl()));
    }

    /**
     * 2023-09-26
     * 황태원
     * 꿀조합 게시글 수정, 삭제권한 체크
     */
    public void checkPermission(String email, Long id) {
        Member member = memberService.getMember(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Recipe recipe = recipeService.getRecipe(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        if (!member.getId().equals(recipe.getMember().getId())) {
            throw new RecipeException(NO_PERMISSION);
        }
    }

}
