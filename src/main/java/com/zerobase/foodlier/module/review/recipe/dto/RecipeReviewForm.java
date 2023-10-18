package com.zerobase.foodlier.module.review.recipe.dto;

import com.zerobase.foodlier.common.validator.image.ImageFile;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeReviewForm {

    @NotBlank(message = "꿀조합 후기 내용을 입력해주세요.")
    private String content;
    @Range(min = 1, max = 5, message = "1~5의 정수만 입력가능합니다.")
    private int star;
    @ImageFile
    private MultipartFile cookImage;

}
