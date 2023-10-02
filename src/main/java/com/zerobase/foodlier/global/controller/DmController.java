package com.zerobase.foodlier.global.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
}
