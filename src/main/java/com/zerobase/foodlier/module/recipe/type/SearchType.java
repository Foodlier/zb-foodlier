package com.zerobase.foodlier.module.recipe.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {

    TITLE("title.kor"),
    WRITER("writer.kor"),
    INGREDIENTS("ingredients.kor"),
    ;

    private final String searchKey;

}
