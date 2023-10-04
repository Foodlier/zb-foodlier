package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;

public interface HeartService {
    @RedissonLock
    void createHeart(MemberAuthDto memberAuthDto, Long recipeId);

    @RedissonLock
    void deleteHeart(MemberAuthDto memberAuthDto, Long recipeId);
}
