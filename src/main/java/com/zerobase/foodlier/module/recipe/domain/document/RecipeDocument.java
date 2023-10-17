package com.zerobase.foodlier.module.recipe.domain.document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

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
    private static final String DATE_FORMAT = "uuuu-MM-dd'T'HH:mm:ss.SSS";
    @Id
    private Long id;
    private String title;
    private String ingredients;
    private String writer;
    private long memberId;
    private long numberOfHeart;
    private long numberOfComment;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, pattern = DATE_FORMAT)
    private LocalDateTime createAt;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateIngredients(String ingredients) {
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
        this.numberOfComment = Math.max(MIN_COMMENT, --this.numberOfComment);
    }
}