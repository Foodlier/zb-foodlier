package com.zerobase.foodlier.module.review.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeReviewRequestDto {

    private String title;
    private String content;
    private int star;
    private String cookImageUrl;

    public static RecipeReviewRequestDto from(RecipeReviewForm form, String cookImageUrl){
        return RecipeReviewRequestDto.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .star(form.getStar())
                .cookImageUrl(cookImageUrl)
                .build();
    }
}
