package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.module.transaction.dto.TransactionDto;

public interface MemberBalanceHistoryService {
    void createRequestMemberBalanceHistory(TransactionDto transactionDto);

    void createChefMemberBalanceHistory(TransactionDto transactionDto);
}
