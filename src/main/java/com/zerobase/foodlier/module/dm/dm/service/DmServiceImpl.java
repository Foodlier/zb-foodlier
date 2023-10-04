package com.zerobase.foodlier.module.dm.dm.service;

import com.zerobase.foodlier.module.dm.dm.dto.MessageDto;
import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DmServiceImpl implements DmService {

    private final DmRepository dmRepository;
    private final DmRoomRepository dmRoomRepository;

    @Override
    public void createDm(MessageDto message) {
        DmRoom dmRoom = dmRoomRepository.findById(message.getRoomId())
                .orElseThrow();

        dmRepository.save(Dm.builder()
                .text(message.getMessage())
                .flag(message.getWriter())
                .dmroom(dmRoom)
                .build());
    }
}
