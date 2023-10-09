package com.zerobase.foodlier.module.recipe.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "recipe")
@Mapping(mappingPath = "/elasticsearch/recipe-mapping.json")
@Setting(settingPath = "/elasticsearch/recipe-settings.json")
public class RecipeDocument {

    @Id
    private Long id;
    private String title;
    private List<String> ingredients;
    private String writer;
    private long numberOfHeart;
    private long numberOfComment;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void updateWriter(String writer) {
        this.writer = writer;
    }

    public void updateNumberOfHeart(long numberOfHeart) {
        this.numberOfHeart = numberOfHeart;
    }

    public void plusNumberOfComment() {
        this.numberOfComment++;
    }
    public void minusNumberOfComment() {
        this.numberOfComment--;
    }
}