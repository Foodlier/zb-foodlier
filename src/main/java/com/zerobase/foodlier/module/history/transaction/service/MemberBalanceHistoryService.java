package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberBalanceHistoryService {
    void createRequestMemberBalanceHistory(TransactionDto transactionDto);

    void createChefMemberBalanceHistory(TransactionDto transactionDto);

    List<MemberBalanceHistoryDto> getTransactionHistory(MemberAuthDto memberAuthDto,
                                                        Pageable pageable);
}
