package com.zerobase.foodlier.module.member.member.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultProfileDtoResponse {

    private Long memberId;
    private String nickname;
    private String profileUrl;
    private Integer receivedHeart;
    private Boolean isChef;
    private Long chefMemberId;

}
