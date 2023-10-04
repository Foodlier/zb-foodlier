package com.zerobase.foodlier.module.dm.room.repository;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.request.domain.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {
    Optional<DmRoom> findByRequest(Request request);
}
