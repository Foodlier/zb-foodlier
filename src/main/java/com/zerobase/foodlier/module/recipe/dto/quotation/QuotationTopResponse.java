package com.zerobase.foodlier.module.recipe.dto.quotation;

import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationTopResponse {

    private Long quotationId;
    private String title;
    private String content;
    private Difficulty difficulty;
    private int expectedTime;

}
