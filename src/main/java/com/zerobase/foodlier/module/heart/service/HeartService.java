package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.domain.model.Heart;

public interface HeartService {
    @RedissonLock
    Heart createHeart(MemberAuthDto memberAuthDto, Long recipeId);

    @RedissonLock
    void deleteHeart(MemberAuthDto memberAuthDto, Long recipeId);
}
