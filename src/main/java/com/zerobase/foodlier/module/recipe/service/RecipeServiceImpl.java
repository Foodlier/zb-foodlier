package com.zerobase.foodlier.module.recipe.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDetailDto;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDtoResponse;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeIngredientDto;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    RecipeRepository recipeRepository;

    @Override
    public void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest) {
        Recipe recipe = recipeDtoRequest.toEntity(member);
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
        recipe.setMainImageUrl(recipe.getMainImageUrl());
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
    public Optional<Recipe> getRecipe(Long id) {
        return recipeRepository.findById(id);
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

    @Transactional
    @Override
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeException(NO_SUCH_RECIPE);
        }

        recipeRepository.deleteById(id);
    }

}
