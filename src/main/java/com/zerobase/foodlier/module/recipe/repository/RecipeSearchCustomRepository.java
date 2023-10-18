package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeSearchCustomRepository {
    Page<RecipeDocument> searchBy(SearchType searchType, String searchText, Pageable pageable);
}
