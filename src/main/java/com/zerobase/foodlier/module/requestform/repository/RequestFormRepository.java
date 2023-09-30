package com.zerobase.foodlier.module.requestform.repository;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestFormRepository extends JpaRepository<RequestForm, Long> {

    Page<RequestForm> findAllByMemberOrderByCreatedAtDesc(Member member, PageRequest pageRequest);
}
