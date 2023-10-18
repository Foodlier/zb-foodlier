package com.zerobase.foodlier.module.recipe.domain.vo;

import lombok.*;

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
