package com.zerobase.foodlier.common.redis.repository;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    boolean existsByEmail(String email);
    Optional<RefreshToken> findByEmail(String email);

}
