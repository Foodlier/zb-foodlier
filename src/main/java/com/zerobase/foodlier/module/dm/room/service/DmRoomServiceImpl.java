package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DmRoomServiceImpl implements DmRoomService {

    private final DmRoomRepository dmRoomRepository;
    private static final String SORT_BY_CREATED_AT = "createdAt";


    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 수락 시 DM 방이 생성됩니다.
     */
    @Override
    public DmRoom createDmRoom(Request request) {
        return dmRoomRepository.save(DmRoom.builder()
                .request(request)
                .suggestion(Suggestion.builder()
                        .suggestedPrice(0)
                        .isAccept(false)
                        .isSuggested(false)
                        .build())
                .build());
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-02
     * 채팅방 목록을 가져옵니다.
     */
    @Override
    public List<DmRoomDto> getDmRoomList(Long id, int pageIdx, int pageSize) {
        Pageable pageable = PageRequest.of(pageIdx, pageSize,
                Sort.by(SORT_BY_CREATED_AT).descending());
        List<DmRoomDto> DmRoomDtoList = dmRoomRepository.getDmRoomPage(id, pageable);

        if (DmRoomDtoList.isEmpty()) {
            throw new DmRoomException(DM_ROOM_NOT_FOUND);
        }
        return DmRoomDtoList;
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
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));
        if (id.equals(dmRoom.getRequest().getMember().getId())) {
            dmRoomRepository.updateDmRoomByMember(roomId);
        } else {
            dmRoomRepository.updateDmRoomByChef(roomId);
        }
    }
}
