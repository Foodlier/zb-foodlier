package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;

public interface MemberService {
    SignInResponse signIn(SignInForm form);
}
