package com.zerobase.foodlier.module.member.chef.dto;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopChefDto {

    private Long memberId;
    private Long chefMemberId;
    private String nickname;
    private String profileUrl;

    public static TopChefDto from(ChefMember chefMember){
        return TopChefDto
                .builder()
                .memberId(chefMember.getMember().getId())
                .chefMemberId(chefMember.getId())
                .nickname(chefMember.getMember().getNickname())
                .profileUrl(chefMember.getMember().getProfileUrl())
                .build();
    }

}
