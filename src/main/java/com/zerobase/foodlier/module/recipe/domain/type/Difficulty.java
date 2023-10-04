package com.zerobase.foodlier.module.recipe.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Difficulty {

    EASY("하"),
    MEDIUM("중"),
    HARD("상");

    private final String korean;
}
