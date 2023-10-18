package com.zerobase.foodlier.module.member.member.domain.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private String roadAddress;
    private String addressDetail;
    private double lat;
    private double lnt;
}
