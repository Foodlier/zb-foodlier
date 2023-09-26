package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;

import java.util.Optional;

public interface MemberService {
    void createMember();
    Optional<Member> getMember(String email);
}
