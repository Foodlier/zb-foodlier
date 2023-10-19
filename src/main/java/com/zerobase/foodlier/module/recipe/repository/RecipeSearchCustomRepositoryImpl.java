package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecipeSearchCustomRepositoryImpl implements RecipeSearchCustomRepository{

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Page<RecipeDocument> searchBy(SearchType searchType, String searchText, Pageable pageable) {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(searchType.getSearchKey(), searchText);
        if(searchType == SearchType.TITLE){
            queryBuilder = QueryBuilders.matchPhrasePrefixQuery(searchType.getSearchKey(), searchText);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().must(queryBuilder))
                .withPageable(pageable)
                .build();
        SearchHits<RecipeDocument> findingResult = elasticsearchRestTemplate.search(searchQuery, RecipeDocument.class);

        return new PageImpl<>(findingResult.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList()));
    }

}
