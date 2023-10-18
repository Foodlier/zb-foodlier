package com.zerobase.foodlier.module.review.chef.dto;

import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefReviewResponseDto {

    private String nickname;
    private String profileUrl;
    private String content;
    private int star;

    public static ChefReviewResponseDto from(ChefReview chefReview){
        return ChefReviewResponseDto.builder()
                .nickname(chefReview.getRequest().getMember().getNickname())
                .profileUrl(chefReview.getRequest().getMember().getProfileUrl())
                .content(chefReview.getContent())
                .star(chefReview.getStar())
                .build();
    }

}
