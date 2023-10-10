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
    public ResponseEntity<ListResponse<NotificationDto>> getSimpleNotificationList(@AuthenticationPrincipal MemberAuthDto principal,
                                                                                   @PathVariable int pageIdx,
                                                                                   @PathVariable int pageSize)
    {
        return ResponseEntity.ok(notificationService.getNotificationBy(principal.getId(),
                PageRequest.of(pageIdx, pageSize)));
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal MemberAuthDto principal,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(notificationFacade.subscribe(principal.getEmail(), lastEventId));
    }

}
