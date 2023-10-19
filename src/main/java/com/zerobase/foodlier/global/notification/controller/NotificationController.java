package com.zerobase.foodlier.global.notification.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationFacade notificationFacade;
    private final NotificationService notificationService;

    @GetMapping("/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<NotificationDto>> getSimpleNotificationList(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                                                   @PathVariable int pageIdx,
                                                                                   @PathVariable int pageSize)
    {
        return ResponseEntity.ok(notificationService.getNotificationBy(memberAuthDto.getId(),
                PageRequest.of(pageIdx, pageSize)));
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal MemberAuthDto memberAuthDto) {
        return ResponseEntity.ok(notificationFacade.subscribe(memberAuthDto));
    }

    @PatchMapping(value = "/read/{notificationId}")
    public ResponseEntity<String> changeReadStatus(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                   @PathVariable Long notificationId) {
        notificationService.updateNotificationStatus(memberAuthDto.getId(),notificationId);
        return ResponseEntity.ok("알림을 읽었습니다");
    }
}
