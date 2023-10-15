package com.zerobase.foodlier.module.dm.room.repository;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long>,DmRoomRepositoryCustom {

}
