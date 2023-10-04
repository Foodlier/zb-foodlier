package com.zerobase.foodlier.module.review.recipe.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeReviewForm {

    private String title;
    private String content;
    @Range(min = 1, max = 5, message = "1~5의 정수만 입력가능합니다.")
    private int star;
    private MultipartFile cookImage;

}
