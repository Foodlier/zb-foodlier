package com.zerobase.foodlier.common.redis.repository;

import com.zerobase.foodlier.common.redis.domain.model.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
}
