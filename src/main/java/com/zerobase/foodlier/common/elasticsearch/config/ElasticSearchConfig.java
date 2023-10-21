package com.zerobase.foodlier.common.elasticsearch.config;


import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = RecipeSearchRepository.class)
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private final String connectTo;

    public ElasticSearchConfig(@Value("${elasticsearch.connection}") String connectTo){
        this.connectTo = connectTo;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(connectTo).build();
        return RestClients.create(configuration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}