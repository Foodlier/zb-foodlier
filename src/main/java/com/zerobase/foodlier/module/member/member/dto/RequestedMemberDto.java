package com.zerobase.foodlier.module.member.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedMemberDto {

    private Long memberId;

    private String profileUrl;

    private String nickname;

    private Double distance;

    private Double lat;

    private Double lnt;

    private Long requestId;

    private String title;

    private String content;

    private Long expectedPrice;

    private String mainImageUrl;

}
