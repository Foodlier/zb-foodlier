package com.zerobase.foodlier.module.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import com.zerobase.foodlier.module.transaction.exception.TransactionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static com.zerobase.foodlier.module.transaction.exception.TransactionErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private DmRoomRepository dmRoomRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("제안 성공")
    void success_sendSuggestion() {
        //given
        DmRoom dmRoom = getDmRoom();

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(dmRoom));

        ArgumentCaptor<DmRoom> captor = ArgumentCaptor.forClass(DmRoom.class);

        //when
        String result = transactionService.sendSuggestion(MemberAuthDto.builder()
                        .id(1L).build(), new SuggestionForm(1000),
                1L);

        //then
        verify(dmRoomRepository, times(1))
                .save(captor.capture());

        DmRoom value = captor.getValue();
        assertAll(
                () -> assertTrue(value.getSuggestion().getIsSuggested()),
                () -> assertFalse(value.getSuggestion().getIsAccept()),
                () -> assertEquals(1000,
                        value.getSuggestion().getSuggestedPrice()),
                () -> assertEquals("가격을 제안했습니다.", result)
        );
    }

    @Test
    @DisplayName("제안 실패 - dm 방을 찾을 수 없습니다.")
    void fail_sendSuggestion_dmRoomNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> transactionService.sendSuggestion(MemberAuthDto.builder()
                                .id(1L).build(), new SuggestionForm(1000),
                        1L));

        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("제안 실패 - 요리사가 아닙니다.")
    void fail_sendSuggestion_chefMemberNotMatch() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService
                                .sendSuggestion(MemberAuthDto.builder()
                                                .id(2L)
                                                .build(),
                                        new SuggestionForm(1000),
                                        1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_MATCH, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 실패 - 이미 제안이 요청되었습니다.")
    void fail_sendSuggestion_alreadySuggested() {
        //given
        DmRoom dmRoom = getDmRoom();
        dmRoom.getSuggestion().updateSuggested(true);

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(dmRoom));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService
                                .sendSuggestion(MemberAuthDto.builder()
                                                .id(1L)
                                                .build(),
                                        new SuggestionForm(1000),
                                        1L));

        //then
        assertEquals(ALREADY_SUGGESTED, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 취소 성공")
    void success_cancelSuggestion() {
        //given
        DmRoom dmRoom = getDmRoom();
        dmRoom.getSuggestion().updateSuggested(true);

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(dmRoom));

        ArgumentCaptor<DmRoom> captor = ArgumentCaptor.forClass(DmRoom.class);

        //when
        String result = transactionService
                .cancelSuggestion(MemberAuthDto.builder()
                                .id(1L)
                                .build(),
                        1L);

        //then
        verify(dmRoomRepository, times(1))
                .save(captor.capture());

        DmRoom value = captor.getValue();
        assertAll(
                () -> assertEquals(0,
                        value.getSuggestion().getSuggestedPrice()),
                () -> assertEquals(false,
                        value.getSuggestion().getIsSuggested()),
                () -> assertEquals(false,
                        value.getSuggestion().getIsAccept()),
                () -> assertEquals("제안이 취소되었습니다.", result)
        );
    }

    @Test
    @DisplayName("제안 취소 실패 - dm 방을 찾을 수 없습니다.")
    void fail_cancelSuggestion_dmRoomNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> transactionService.cancelSuggestion(MemberAuthDto.builder()
                        .id(1L).build(), 1L));

        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("제안 취소 실패 - 요리사가 아닙니다.")
    void fail_cancelSuggestion_chefMemberNotMatch() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService
                                .cancelSuggestion(MemberAuthDto.builder()
                                        .id(2L)
                                        .build(), 1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_MATCH, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 취소 실패 - 요청된 제안이 없습니다.")
    void fail_cancelSuggestion_suggestionNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService
                                .cancelSuggestion(MemberAuthDto.builder()
                                        .id(1L)
                                        .build(), 1L));

        //then
        assertEquals(SUGGESTION_NOT_FOUND, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 수락 성공")
    void success_approveSuggestion() {
        //given
        DmRoom dmRoom = getDmRoom();
        dmRoom.getSuggestion().updatePrice(1000);
        dmRoom.getSuggestion().updateSuggested(true);

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(dmRoom));

        //when
        TransactionDto transactionDto =
                transactionService.approveSuggestion(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L);

        //then
        assertAll(
                () -> assertEquals(0L,
                        transactionDto.getRequestMember().getPoint()),
                () -> assertEquals(1000L,
                        transactionDto.getChefMember().getPoint()),
                () -> assertEquals(1000,
                        transactionDto.getChangePoint()),
                () -> assertEquals(true,
                        dmRoom.getSuggestion().getIsAccept()),
                () -> assertTrue(dmRoom.getRequest().isPaid())
        );
    }

    @Test
    @DisplayName("제안 수락 실패 - dm 방을 찾을 수 없습니다.")
    void fail_approveSuggestion_dmRoomNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> transactionService.approveSuggestion(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));

        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("제안 수락 실패 - 요청자가 아닙니다.")
    void fail_approveSuggestion_requestMemberNotMatch() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService.approveSuggestion(MemberAuthDto.builder()
                                .id(2L)
                                .build(), 1L));

        //then
        assertEquals(REQUEST_MEMBER_NOT_MATCH, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 수락 실패 - 요청된 제안이 없습니다.")
    void fail_approveSuggestion_suggestionNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService.approveSuggestion(MemberAuthDto.builder()
                                .id(1L)
                                .build(), 1L));

        //then
        assertEquals(SUGGESTION_NOT_FOUND, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 수락 실패 - 포인트가 부족합니다.")
    void fail_approveSuggestion_notEnoughPoint() {
        //given
        DmRoom dmRoom = getDmRoom();
        dmRoom.getSuggestion().updateSuggested(true);
        dmRoom.getSuggestion().updatePrice(10000);

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(dmRoom));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService.approveSuggestion(MemberAuthDto.builder()
                                .id(1L)
                                .build(), 1L));

        //then
        assertEquals(NOT_ENOUGH_POINT, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 거절 성공")
    void success_rejectSuggestion() {
        //given
        DmRoom dmRoom = getDmRoom();
        dmRoom.getSuggestion().updateSuggested(true);

        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(dmRoom));

        //when
        String result =
                transactionService.rejectSuggestion(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L);

        //then
        assertAll(
                () -> assertEquals(0,
                        dmRoom.getSuggestion().getSuggestedPrice()),
                () -> assertEquals(false,
                        dmRoom.getSuggestion().getIsAccept()),
                () -> assertEquals(false,
                        dmRoom.getSuggestion().getIsSuggested()),
                () -> assertEquals("제안을 거절하였습니다.", result)
        );
    }

    @Test
    @DisplayName("제안 거절 실패 - dm 방을 찾을 수 없습니다.")
    void fail_rejectSuggestion_dmRoomNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> transactionService.rejectSuggestion(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));
        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("제안 거절 실패 - 요청자가 아닙니다.")
    void fail_rejectSuggestion_requestMemberNotMatch() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService.rejectSuggestion(MemberAuthDto.builder()
                                .id(2L)
                                .build(), 1L));

        //then
        assertEquals(REQUEST_MEMBER_NOT_MATCH, transactionException.getErrorCode());
    }

    @Test
    @DisplayName("제안 거절 실패 - 요청된 제안이 없습니다")
    void fail_rejectSuggestion_suggestionNotFound() {
        //given
        given(dmRoomRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(getDmRoom()));

        //when
        TransactionException transactionException =
                assertThrows(TransactionException.class,
                        () -> transactionService.rejectSuggestion(MemberAuthDto.builder()
                                .id(1L)
                                .build(), 1L));

        //then
        assertEquals(SUGGESTION_NOT_FOUND, transactionException.getErrorCode());
    }

    private static DmRoom getDmRoom() {
        return DmRoom.builder()
                .id(1L)
                .request(Request.builder()
                        .member(Member.builder()
                                .id(1L)
                                .point(1000L)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(1L)
                                .member(Member.builder()
                                        .id(1L)
                                        .build())
                                .build())
                        .build())
                .isChefExit(false)
                .isMemberExit(false)
                .suggestion(Suggestion.builder()
                        .suggestedPrice(0)
                        .isSuggested(false)
                        .isAccept(false)
                        .build())
                .build();
    }
}