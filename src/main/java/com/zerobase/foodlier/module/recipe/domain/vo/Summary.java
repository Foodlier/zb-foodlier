package com.zerobase.foodlier.module.recipe.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Summary {

    private String title;
    private String content;

}
