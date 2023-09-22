package com.zerobase.foodlier.module.dm.dm.repository;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DmRepository extends JpaRepository<Dm, Long> {
}
