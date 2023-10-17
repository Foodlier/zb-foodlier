package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageResponseDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmRoomService dmRoomService;
    private final DmService dmService;

    @GetMapping("/room/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<DmRoomDto>> getDmRoomPage(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(dmRoomService.getDmRoomList(
                memberAuthDto.getId(), pageIdx, pageSize));
    }

    @PutMapping("/room/exit/{roomId}")
    public ResponseEntity<String> exitDmRoom(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long roomId
    ) {
        dmRoomService.exitDmRoom(memberAuthDto.getId(), roomId);
        return ResponseEntity.ok("성공적으로 채팅방을 나갔습니다.");
    }

    @GetMapping("/message")
    public ResponseEntity<MessageResponseDto> getDmList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestParam("roomId") Long roomId,
            @RequestParam("dmId") Long dmId
    ) {
        return ResponseEntity.ok(dmService.getDmList(memberAuthDto.getId(), roomId, dmId));
    }
}
