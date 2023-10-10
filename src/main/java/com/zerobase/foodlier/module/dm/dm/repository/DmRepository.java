package com.zerobase.foodlier.module.dm.dm.repository;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DmRepository extends JpaRepository<Dm, Long> {
    Page<Dm> findByDmroomAndIdLessThan(DmRoom dmRoom, Long id, Pageable pageable);

    Dm findFirstByDmroomOrderByIdDesc(DmRoom dmRoom);

    List<Dm> findByDmroom(DmRoom dmRoom);
}
