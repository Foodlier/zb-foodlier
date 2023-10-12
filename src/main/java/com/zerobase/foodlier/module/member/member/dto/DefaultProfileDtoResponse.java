package com.zerobase.foodlier.module.member.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultProfileDtoResponse {

    private Long memberId;
    private String nickname;
    private String profileUrl;
    private long receivedHeart;
    private Boolean isChef;
    private Long chefMemberId;

}
