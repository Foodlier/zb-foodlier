package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DmRoomServiceImpl implements DmRoomService {
    private final DmRoomRepository dmRoomRepository;
    private final RequestRepository requestRepository;

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 수락 시 DM 방이 생성됩니다.
     */
    @Override
    public void createDmRoom(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));
        dmRoomRepository.save(DmRoom.builder()
                .request(request)
                .build());
    }
}
