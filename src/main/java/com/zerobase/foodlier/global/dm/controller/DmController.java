package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmRoomService dmRoomService;

    @GetMapping("/room/{pageIdx}/{pageSize}")
    public ResponseEntity<Page<DmRoomDto>> getDmRoomPage(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize) {
        return ResponseEntity.ok(dmRoomService.getDmRoomPage(memberAuthDto.getId(), pageIdx, pageSize));
    }

    @PutMapping("/room/exit/{roomId}")
    public ResponseEntity<String> exitDmRoom(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long roomId) {
        dmRoomService.exitDmRoom(memberAuthDto.getId(), roomId);
        return ResponseEntity.ok("성공적으로 채팅방을 나갔습니다.");
    }

}
