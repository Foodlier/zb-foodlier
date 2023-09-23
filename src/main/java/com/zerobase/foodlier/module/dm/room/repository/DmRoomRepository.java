package com.zerobase.foodlier.module.dm.room.repository;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {
}
