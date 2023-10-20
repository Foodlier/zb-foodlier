package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, Long>,
        RecipeSearchCustomRepository {


}
