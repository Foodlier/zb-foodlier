package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthLoginParams;

public interface OAuthInfoService {
    OAuthInfoResponse request(OAuthLoginParams params);
}
