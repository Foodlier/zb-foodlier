package com.zerobase.foodlier.module.review.chef.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefReviewForm {

    private String content;
    @Range(min = 1, max = 5, message = "1~5의 정수만 입력가능합니다.")
    private int star;

}
