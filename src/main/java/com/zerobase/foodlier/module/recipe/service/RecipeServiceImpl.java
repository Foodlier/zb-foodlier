package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.dto.*;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest) {
        Recipe recipe = Recipe.builder()
                .summary(Summary.builder()
                        .title(recipeDtoRequest.getTitle())
                        .content(recipeDtoRequest.getContent())
                        .build())
                .mainImageUrl(recipeDtoRequest.getMainImageUrl())
                .expectedTime(recipeDtoRequest.getExpectedTime())
                .recipeStatistics(new RecipeStatistics())
                .difficulty(recipeDtoRequest.getDifficulty())
                .isPublic(true)
                .member(member)
                .recipeIngredientList(recipeDtoRequest.getRecipeIngredientDtoList()
                        .stream()
                        .map(RecipeIngredientDto::toEntity)
                        .collect(Collectors.toList()))
                .recipeDetailList(recipeDtoRequest.getRecipeDetailDtoList()
                        .stream()
                        .map(RecipeDetailDto::toEntity)
                        .collect(Collectors.toList()))
                .build();
        recipeRepository.save(recipe);
    }


    @Transactional
    @Override
    public void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.setSummary(Summary.builder()
                .title(recipeDtoRequest.getTitle())
                .content(recipeDtoRequest.getContent())
                .build());
        recipe.setMainImageUrl(recipeDtoRequest.getMainImageUrl());
        recipe.setExpectedTime(recipeDtoRequest.getExpectedTime());
        recipe.setDifficulty(recipeDtoRequest.getDifficulty());
        recipe.setRecipeDetailList(recipeDtoRequest.getRecipeDetailDtoList()
                .stream()
                .map(RecipeDetailDto::toEntity)
                .collect(Collectors.toList()));
        recipe.setRecipeIngredientList(recipeDtoRequest.getRecipeIngredientDtoList()
                .stream()
                .map(RecipeIngredientDto::toEntity)
                .collect(Collectors.toList()));

        recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
    }

    /**
     * 2023-09-26
     * 황태원
     * 레시피 상세보기, 레시피 수정 시 정보 로드
     */
    @Override
    public RecipeDtoResponse getRecipeDetail(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        return RecipeDtoResponse.fromEntity(recipe);
    }

    @Override
    public ImageUrlDto deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        recipeRepository.deleteById(id);
        return ImageUrlDto.builder()
                .mainImageUrl(recipe.getMainImageUrl())
                .recipeDetailList(recipe.getRecipeDetailList())
                .build();
    }

}
