package com.zerobase.foodlier.module.review.chef.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefReviewForm {

    @NotBlank(message = "요리사 후기 내용을 입력해주세요.")
    private String content;
    @Range(min = 1, max = 5, message = "1~5의 정수만 입력가능합니다.")
    private int star;

}
