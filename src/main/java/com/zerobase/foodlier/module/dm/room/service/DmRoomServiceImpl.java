package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .suggestion(Suggestion.builder()
                        .suggestedPrice(0)
                        .isAccept(false)
                        .isSuggested(false)
                        .build())
                .build());
    }
}
