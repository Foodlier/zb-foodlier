package com.zerobase.foodlier.common.redis.repository;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
