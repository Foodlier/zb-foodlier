package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DmRoomServiceImpl implements DmRoomService {
    private final DmRoomRepository dmRoomRepository;


    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 수락 시 DM 방이 생성됩니다.
     */
    @Override
    public DmRoom createDmRoom(Request request) {
        return dmRoomRepository.save(DmRoom.builder()
                .request(request)
                .build());
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-02
     * 채팅방 목록을 가져옵니다.
     */
    @Override
    public Page<DmRoomDto> getDmRoomPage(Long id, int pageIdx, int pageSize) {
        Pageable pageable = PageRequest.of(pageIdx, pageSize,
                Sort.by("createdAt").descending());
        return dmRoomRepository.getDmRoomPage(id, pageable);
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-02
     * 해당 채팅방에서 나갑니다.
     */
    @Override
    @Transactional
    public void exitDmRoom(Long id, Long roomId) {
        DmRoom dmRoom = dmRoomRepository.findById(roomId)
                .orElseThrow();
        if (id.equals(dmRoom.getRequest().getMember().getId())) {
            dmRoomRepository.updateDmRoomByMember(roomId);
        } else {
            dmRoomRepository.updateDmRoomByChef(roomId);
        }
    }
}
