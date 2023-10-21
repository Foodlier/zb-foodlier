package com.zerobase.foodlier.global.notification.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.NotificationDto;
import com.zerobase.foodlier.module.notification.service.notification.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = NotificationController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
public class NotificationControllerTest {

    @MockBean
    private NotificationFacade notificationFacade;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("알림 구독 성공")
    void success_notification_subscribe() throws Exception {

        // given
        SseEmitter sseEmitter = new SseEmitter(60 * 1000 * 60L);

        given(notificationFacade.subscribe(any()))
                .willReturn(sseEmitter);
        // when
        mockMvc.perform(get("/notification/subscribe").contentType("text/event-stream")
                        .with(csrf()))
                .andExpect(status().isOk());

        // then
        verify(notificationFacade, times(1)).subscribe(any());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("알림 목록 조회 성공")
    void success_getNotificationList() throws Exception {

        // given
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        ListResponse<NotificationDto> response = ListResponse.<NotificationDto>builder()
                .totalPages(1)
                .hasNextPage(false)
                .totalElements(2)
                .content(List.of(
                        NotificationDto.builder()
                                .id(2L)
                                .targetId(1L)
                                .notificationType(NotificationType.COMMENT)
                                .sentAt(LocalDateTime.now())
                                .content("사용자 찜닭 러버 님이 세젤맛 찜닭 게시물에 댓글을 남겼습니다")
                                .build(),
                        NotificationDto.builder()
                                .id(1L)
                                .targetId(1L)
                                .notificationType(NotificationType.HEART)
                                .sentAt(LocalDateTime.now().minusDays(2))
                                .content("사용자 찜닭 러버 님이 세젤맛 찜닭 게시물에 좋아요를 눌렀습니다")
                                .build()

                ))
                .build();
        given(notificationService.getNotificationBy(anyLong(), any()))
                .willReturn(response);
        // when
        ResultActions resultActions = mockMvc.perform(get("/notification/0/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        // then
        resultActions.andExpectAll(
                status().isOk(),
                jsonPath("$.totalPages").value(response.getTotalPages()),
                jsonPath("$.totalElements").value(response.getTotalElements()),
                jsonPath("$.hasNextPage").value(response.isHasNextPage()),
                jsonPath("$.content.length()").value(response.getContent().size()),

                // 첫번째 알림 값 검증
                jsonPath("$.content[0].id").value(response.getContent().get(0).getId()),
                jsonPath("$.content[0].sentAt").value(response.getContent().get(0).getSentAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[0].content").value(response.getContent().get(0).getContent()),
                jsonPath("$.content[0].notificationType").value(response.getContent().get(0).getNotificationType().name()),
                jsonPath("$.content[0].targetId").value(response.getContent().get(0).getTargetId()),

                // 두번째 알림 값 검증
                jsonPath("$.content[1].id").value(response.getContent().get(1).getId()),
                jsonPath("$.content[1].sentAt").value(response.getContent().get(1).getSentAt()
                        .format(DateTimeFormatter.ofPattern(datePattern))),
                jsonPath("$.content[1].content").value(response.getContent().get(1).getContent()),
                jsonPath("$.content[1].notificationType").value(response.getContent().get(1).getNotificationType().name()),
                jsonPath("$.content[1].targetId").value(response.getContent().get(1).getTargetId())
        );

    }


    @Test
    @WithCustomMockUser
    @DisplayName("알림 read 상태 변경 성공")
    void success_change_read_status() throws Exception {

        // given
        String response = "알림을 읽었습니다";
        doNothing().when(notificationService).updateNotificationStatus(anyLong(), any());

        // when
        mockMvc.perform(patch("/notification/read/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        // then
        verify(notificationService, times(1)).updateNotificationStatus(anyLong(),anyLong());

    }

}
