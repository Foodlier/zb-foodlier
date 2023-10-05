package com.zerobase.foodlier.global.point.facade;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.service.MemberBalanceHistoryService;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import com.zerobase.foodlier.module.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionFacade {
    private final TransactionService transactionService;
    private final MemberBalanceHistoryService memberBalanceHistoryService;

    public void pointTransactionAndSaveHistory(MemberAuthDto memberAuthDto,
                                               Long dmRoomId) {
        TransactionDto transactionDto =
                transactionService.approveSuggestion(memberAuthDto, dmRoomId);
        memberBalanceHistoryService.createRequestMemberBalanceHistory(transactionDto);
        memberBalanceHistoryService.createChefMemberBalanceHistory(transactionDto);
    }
}
