package com.zerobase.foodlier.global.point.facade;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.service.MemberBalanceHistoryService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import com.zerobase.foodlier.module.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionFacadeTest {
    @Mock
    private TransactionService transactionService;

    @Mock
    private MemberBalanceHistoryService memberBalanceHistoryService;

    @InjectMocks
    private TransactionFacade transactionFacade;

    @Test
    @DisplayName("포인트 거래 및 내역 생성 성공")
    void success_pointTransactionAndSaveHistory() {
        //given
        TransactionDto transactionDto = TransactionDto.builder()
                .requestMember(Member.builder()
                        .id(1L)
                        .point(1000L)
                        .build())
                .chefMember(Member.builder()
                        .id(2L)
                        .build())
                .changePoint(1000)
                .build();

        given(transactionService.approveSuggestion(any(), anyLong()))
                .willReturn(transactionDto);

        //when
        transactionFacade.pointTransactionAndSaveHistory(MemberAuthDto.builder()
                .id(1L)
                .build(), 1L);

        //then
        verify(memberBalanceHistoryService, times(1))
                .createChefMemberBalanceHistory(transactionDto);
        verify(memberBalanceHistoryService, times(1))
                .createRequestMemberBalanceHistory(transactionDto);
    }
}