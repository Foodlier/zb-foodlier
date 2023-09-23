package com.zerobase.foodlier.module.member.member.domain.vo;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String roadAddress;
    private String addressDetail;
    private double lat;
    private double lnt;
}
