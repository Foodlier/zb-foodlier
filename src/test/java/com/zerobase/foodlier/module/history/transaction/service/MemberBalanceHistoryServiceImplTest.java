package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import com.zerobase.foodlier.module.history.transaction.repository.MemberBalanceHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberBalanceHistoryServiceImplTest {
    @Mock
    private MemberBalanceHistoryRepository memberBalanceHistoryRepository;

    @InjectMocks
    private MemberBalanceHistoryServiceImpl memberBalanceHistoryService;

    @Test
    @DisplayName("요청자 포인트 거래내역 생성 성공")
    void success_createRequestMemberBalanceHistory() {
        //given
        TransactionDto transactionDto = getTransactionDto();
        transactionDto.getRequestMember().setPoint(0);

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
                () -> assertEquals(-1000, value.getChangePoint()),
                () -> assertEquals(0, value.getMember().getPoint()),
                () -> assertEquals(transactionDto.getChefMember().getNickname(),
                        value.getSender()),
                () -> assertEquals("포인트 거래", value.getDescription())
        );
    }

    @Test
    @DisplayName("요리사 포인트 거래내역 생성 성공")
    void success_createChefMemberBalanceHistory() {
        //given
        TransactionDto transactionDto = getTransactionDto();
        transactionDto.getChefMember().setPoint(1000L);

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
                () -> assertEquals("포인트 거래", value.getDescription())
        );
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