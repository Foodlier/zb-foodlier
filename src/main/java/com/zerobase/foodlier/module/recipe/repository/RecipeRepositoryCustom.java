package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepositoryCustom {
    Page<Recipe> findHeart(Long memberId, Pageable pageable);

    Page<QuotationTopResponse> findQuotationListForRefrigerator(Long memberId,
                                                                Pageable pageable);

    Page<QuotationTopResponse> findQuotationListForRecipe(Long memberId,
                                                          Pageable pageable);

    boolean existsByIdAndMemberForQuotation(Long memberId, Long quotationId);

    boolean isNotAbleToDeleteForQuotation(Long memberId, Long quotationId);

    boolean isAbleToConvert(Recipe quotation);
}
