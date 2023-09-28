package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeSearchRepository extends ElasticsearchRepository<RecipeDocument, Long> {
    @Query("{\"bool\": {\"must\": [{\"match_phrase_prefix\": {\"title.kor\": \"?0\"}}]}}")
    Page<RecipeDocument> findByTitle(String title, Pageable pageable);

}
