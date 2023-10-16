package com.zerobase.foodlier.module.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionForm {

    @Min(value = 100, message = "100원부터 제안할 수 있습니다.")
    private int suggestedPrice;
}
