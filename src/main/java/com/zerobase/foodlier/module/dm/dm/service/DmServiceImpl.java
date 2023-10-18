package com.zerobase.foodlier.module.dm.dm.service;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageResponseDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.exception.DmException;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.dm.dm.exception.DmErrorCode.NO_SUCH_DM;
import static com.zerobase.foodlier.module.dm.dm.type.MessageType.SUGGESTION;
import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DmServiceImpl implements DmService {

    private final DmRepository dmRepository;
    private final DmRoomRepository dmRoomRepository;
    private static final int ZERO = 0;
    private static final int PAGE_SIZE = 30;
    private static final String SORT_BY_CREATED_AT = "createdAt";
    private static final Long BEFORE_LAST_DM = 1L;

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-06
     * 채팅을 작성합니다. 채팅의 타입이 제안일 경우 dm방 상태를 바꿉니다.
     */
    @Async("dmExecutor")
    @Override
    public CompletableFuture<MessageSubDto> createDm(MessagePubDto message) {
        DmRoom dmRoom = dmRoomRepository.findById(message.getRoomId())
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));

        if (message.getMessageType() == SUGGESTION) {
            dmRoom.updateSuggestion(
                    Suggestion.builder()
                            .isSuggested(true)
                            .isAccept(false)
                            .suggestedPrice(Integer.parseInt(message.getMessage()))
                            .build());
            dmRoomRepository.save(dmRoom);
        }

        Dm dm = dmRepository.save(Dm.builder()
                .text(message.getMessage())
                .flag(message.getWriter())
                .dmroom(dmRoom)
                .messageType(message.getMessageType())
                .build());

        return CompletableFuture.completedFuture(MessageSubDto.builder()
                .roomId(message.getRoomId())
                .dmId(dm.getId())
                .message(dm.getText())
                .writer(dm.getFlag())
                .messageType(dm.getMessageType())
                .createdAt(LocalDateTime.now())
                .build());
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-10-06
     * 채팅방의 채팅 내역을 가져옵니다.
     * 채팅방을 입장하였을 경우 해당 채팅방의 최신 채팅을 가져옵니다.
     * 채팅방에 입장한 후 채팅을 로드할경우 직전에 불러 온 채팅번호를 기준으로 가져옵니다.
     * 로드이후 불러 올 채팅의 유무 또한 같이 가져오며 불러올 채팅이 없을경우 에러를 반환합니다.
     */
    @Override
    public MessageResponseDto getDmList(Long id, Long roomId, Long dmId) {
        DmRoom dmRoom = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));
        Pageable pageable = PageRequest.of(ZERO, PAGE_SIZE,
                Sort.by(SORT_BY_CREATED_AT).descending());

        dmId = getDmId(dmId, dmRoom);

        Page<Dm> dmPage = dmRepository.findByDmroomAndIdLessThan(dmRoom, dmId, pageable);
        validateDmPage(dmPage);

        return buildMessageResponseDto(dmPage);
    }

    private Long getDmId(Long dmId, DmRoom dmRoom) {
        if (dmId == ZERO) {
            return dmRepository.findFirstByDmroomOrderByIdDesc(dmRoom).getId()
                    + BEFORE_LAST_DM;
        }
        return dmId;
    }

    private void validateDmPage(Page<Dm> dmPage) {
        if (dmPage.isEmpty()) {
            throw new DmException(NO_SUCH_DM);
        }
    }

    private MessageResponseDto buildMessageResponseDto(Page<Dm> dmPage) {
        List<MessageSubDto> messageList = dmPage.getContent().stream()
                .map(dm -> MessageSubDto.builder()
                        .roomId(dm.getDmroom().getId())
                        .dmId(dm.getId())
                        .writer(dm.getFlag())
                        .message(dm.getText())
                        .messageType(dm.getMessageType())
                        .createdAt(dm.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return MessageResponseDto.builder()
                .hasNext(dmPage.hasNext())
                .lastMessageId(messageList.get(messageList.size() - 1).getDmId())
                .messageList(messageList)
                .build();
    }

}
