package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.history.transaction.repository.MemberBalanceHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.history.type.TransactionType.POINT_RECEIVE;
import static com.zerobase.foodlier.module.history.type.TransactionType.POINT_SEND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberBalanceHistoryServiceTest {
    @Mock
    private MemberBalanceHistoryRepository memberBalanceHistoryRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberBalanceHistoryService memberBalanceHistoryService;

    @Test
    @DisplayName("요청자 포인트 거래내역 생성 성공")
    void success_createRequestMemberBalanceHistory() {
        //given
        TransactionDto transactionDto = getTransactionDto();
        transactionDto.getRequestMember().pointMinus(1000L);

        ArgumentCaptor<MemberBalanceHistory> captor =
                ArgumentCaptor.forClass(MemberBalanceHistory.class);

        //when
        memberBalanceHistoryService.createRequestMemberBalanceHistory(transactionDto);

        //then
        verify(memberBalanceHistoryRepository, times(1))
                .save(captor.capture());

        MemberBalanceHistory value = captor.getValue();
        assertAll(
                () -> assertEquals(transactionDto.getRequestMember(),
                        value.getMember()),
                () -> assertEquals(1000, value.getChangePoint()),
                () -> assertEquals(0, value.getMember().getPoint()),
                () -> assertEquals(transactionDto.getChefMember().getNickname(),
                        value.getSender()),
                () -> assertEquals("포인트 출금", value.getTransactionType().getDescription())
        );
    }

    @Test
    @DisplayName("요리사 포인트 거래내역 생성 성공")
    void success_createChefMemberBalanceHistory() {
        //given
        TransactionDto transactionDto = getTransactionDto();
        transactionDto.getChefMember().pointPlus(1000L);

        ArgumentCaptor<MemberBalanceHistory> captor =
                ArgumentCaptor.forClass(MemberBalanceHistory.class);

        //when
        memberBalanceHistoryService
                .createChefMemberBalanceHistory(transactionDto);

        //then
        verify(memberBalanceHistoryRepository, times(1))
                .save(captor.capture());

        MemberBalanceHistory value = captor.getValue();
        assertAll(
                () -> assertEquals(transactionDto.getChefMember(),
                        value.getMember()),
                () -> assertEquals(1000, value.getChangePoint()),
                () -> assertEquals(1000, value.getMember().getPoint()),
                () -> assertEquals(transactionDto.getRequestMember().getNickname(),
                        value.getSender()),
                () -> assertEquals("포인트 입금", value.getTransactionType().getDescription())
        );
    }

    @Test
    @DisplayName("거래 내역 조회 성공")
    void success_getTransactionHistory() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Member.builder()
                        .id(1L)
                        .point(1000L)
                        .build()));
        given(memberBalanceHistoryRepository
                .findByMemberOrderByCreatedAtDesc(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(List.of(
                        MemberBalanceHistory.builder()
                                .changePoint(1000)
                                .currentPoint(0)
                                .sender("chef member")
                                .transactionType(POINT_SEND)
                                .createdAt(LocalDateTime.of(2023, 10, 9, 5, 30,30))
                                .build(),
                        MemberBalanceHistory.builder()
                                .changePoint(1000)
                                .currentPoint(1000)
                                .sender("member")
                                .transactionType(POINT_RECEIVE)
                                .createdAt(LocalDateTime.of(2023, 10, 9, 6, 40,15))
                                .build()
                ))));

        //when
        ListResponse<MemberBalanceHistoryDto> transactionHistoryList =
                memberBalanceHistoryService.getTransactionHistory(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(1000,
                        transactionHistoryList.getContent().get(0).getChangePoint()),
                () -> assertEquals(0,
                        transactionHistoryList.getContent().get(0).getCurrentPoint()),
                () -> assertEquals("chef member",
                        transactionHistoryList.getContent().get(0).getSender()),
                () -> assertEquals("포인트 출금",
                        transactionHistoryList.getContent().get(0).getDescription()),
                () -> assertEquals(LocalDateTime.of(2023, 10, 9, 5, 30,30),
                        transactionHistoryList.getContent().get(0).getTransactionAt()),

                () -> assertEquals(1000,
                        transactionHistoryList.getContent().get(1).getChangePoint()),
                () -> assertEquals(1000,
                        transactionHistoryList.getContent().get(1).getCurrentPoint()),
                () -> assertEquals("member",
                        transactionHistoryList.getContent().get(1).getSender()),
                () -> assertEquals("포인트 입금",
                        transactionHistoryList.getContent().get(1).getDescription()),
                () -> assertEquals(LocalDateTime.of(2023, 10, 9, 6, 40,15),
                        transactionHistoryList.getContent().get(1).getTransactionAt())
                );
    }

    @Test
    @DisplayName("거래 내역 조회 실패 - 회원을 찾을 수 없음")
    void fail_getPointHistory_memberNotFound() {
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException memberException = assertThrows(MemberException.class,
                () -> memberBalanceHistoryService.getTransactionHistory(MemberAuthDto.builder()
                        .id(1L)
                        .build(), PageRequest.of(0, 10)));

        //then
        assertEquals(MEMBER_NOT_FOUND, memberException.getErrorCode());
    }

    private static TransactionDto getTransactionDto() {
        return TransactionDto.builder()
                .requestMember(Member.builder()
                        .id(1L)
                        .point(1000L)
                        .build())
                .chefMember(Member.builder()
                        .id(2L)
                        .build())
                .changePoint(1000)
                .build();
    }

}