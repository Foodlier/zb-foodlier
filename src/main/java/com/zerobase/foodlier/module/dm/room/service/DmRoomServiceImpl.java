package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DmRoomServiceImpl implements DmRoomService {

    private final DmRoomRepository dmRoomRepository;
    private final DmRepository dmRepository;
    private final RequestRepository requestRepository;
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
            dmRoom.setMemberExit(true);
        } else {
            dmRoom.setChefExit(true);
        }

        if (dmRoom.isMemberExit() && dmRoom.isChefExit()) {
            List<Dm> dmList = dmRepository.findByDmroom(dmRoom);
            dmRepository.deleteAll(dmList);

            Request request = requestRepository.findById(dmRoom.getRequest().getId())
                    .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));
            request.setDmRoom(null);
            requestRepository.save(request);

            dmRoomRepository.delete(dmRoom);
        } else {
            dmRoomRepository.save(dmRoom);
        }
    }
}
