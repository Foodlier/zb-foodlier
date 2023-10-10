package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import org.springframework.data.domain.Pageable;

public interface MemberBalanceHistoryService {
    void createRequestMemberBalanceHistory(TransactionDto transactionDto);

    void createChefMemberBalanceHistory(TransactionDto transactionDto);

    ListResponse<MemberBalanceHistoryDto> getTransactionHistory(MemberAuthDto memberAuthDto,
                                                                Pageable pageable);
}
