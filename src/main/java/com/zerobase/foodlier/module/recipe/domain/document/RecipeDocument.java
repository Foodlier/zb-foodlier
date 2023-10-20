package com.zerobase.foodlier.module.recipe.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "recipe")
@Mapping(mappingPath = "/elasticsearch/recipe-mapping.json")
@Setting(settingPath = "/elasticsearch/recipe-settings.json")
public class RecipeDocument {
    private static final int MIN_COMMENT = 0;
    private static final int MIN_HEART = 0;
    private static final String DATE_FORMAT = "uuuu-MM-dd'T'HH:mm:ss.SSS";
    @Id
    private Long id;
    private String title;
    private String content;
    private String ingredients;
    private String writer;
    private long memberId;
    private long numberOfHeart;
    private long numberOfComment;
    private String mainImageUrl;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, pattern = DATE_FORMAT)
    private LocalDateTime createAt;

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContent(String content){
        this.content = content;
    }

    public void updateIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void updateMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public void plusNumberOfHeart() {
        this.numberOfHeart++;
    }

    public void minusNumberOfHeart() {
        this.numberOfHeart = Math.max(MIN_HEART, --this.numberOfHeart);
    }

    public void plusNumberOfComment() {
        this.numberOfComment++;
    }

    public void minusNumberOfComment() {
        this.numberOfComment = Math.max(MIN_COMMENT, --this.numberOfComment);
    }
}