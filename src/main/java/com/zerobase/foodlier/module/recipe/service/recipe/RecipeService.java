package com.zerobase.foodlier.module.recipe.service.recipe;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.type.OrderType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest);

    void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id);

    Recipe getRecipe(Long id);

    RecipeDtoResponse getRecipeDetail(Long id);

    void deleteRecipe(Long id);

    ImageUrlDto getBeforeImageUrl(Long id);

    void plusReviewStar(Long recipeId, int star);

    void updateReviewStar(Long recipeId, int originStar, int newStar);

    void minusReviewStar(Long recipeId, int star);

    Recipe plusCommentCount(Long recipeId);

    void minusCommentCount(Long recipeId);

    List<RecipeCardDto> getMainPageRecipeList(MemberAuthDto memberAuthDto);

    ListResponse<RecipeCardDto> getRecipePageRecipeList(MemberAuthDto memberAuthDto,
                                                        Pageable pageable,
                                                        OrderType orderType);

    ListResponse<RecipeCardDto> getRecipeList(RecipeSearchRequest recipeSearchRequest);
    List<RecipeCardDto> recommendedRecipe(MemberAuthDto memberAuthDto);
}