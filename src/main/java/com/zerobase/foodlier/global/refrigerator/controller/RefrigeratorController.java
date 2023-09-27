package com.zerobase.foodlier.global.refrigerator.controller;

import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import com.zerobase.foodlier.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refrigerator")
public class RefrigeratorController {
    private final RequestService requestService;
    private final DmRoomService dmRoomService;

    @PatchMapping("/send?requestId={requestId}&chefMemberId={chefMemberId}")
    public ResponseEntity<String> sendRequest(
            @PathVariable(name = "requestId") Long requestId,
            @PathVariable(name = "chefMemberId") Long chefMemberId
    ) {
        requestService.sendRequest(requestId, chefMemberId);
        return ResponseEntity.ok("요청서가 전송되었습니다.");
    }

    @PatchMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.cancelRequest(requestId);
        return ResponseEntity.ok("요청이 취소되었습니다.");
    }

    @PatchMapping("/approve/{requestId}")
    public ResponseEntity<String> approveRequest(
            @PathVariable(name = "requestId") Long requestId
    ) {
        dmRoomService.createDmRoom(requestId);
        return ResponseEntity.ok("요청을 수락하였습니다.");
    }

    @PatchMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.rejectRequest(requestId);
        return ResponseEntity.ok("요청을 거절하였습니다.");
    }
}
