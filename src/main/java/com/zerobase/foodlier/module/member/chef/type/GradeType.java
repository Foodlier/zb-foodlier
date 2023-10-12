package com.zerobase.foodlier.module.member.chef.type;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;


@RequiredArgsConstructor
public enum GradeType {
    BRONZE(0, 200),
    SILVER(201, 500),
    GOLD(501, 800),
    PLATINUM(801, Long.MAX_VALUE);

    private final long minExp;
    private final long maxExp;

    public static GradeType getGrade(long exp) {
        return Arrays.stream(values())
                .filter(grade -> exp >= grade.minExp && exp <= grade.maxExp)
                .findFirst()
                .orElseGet(() -> BRONZE);
    }
}
