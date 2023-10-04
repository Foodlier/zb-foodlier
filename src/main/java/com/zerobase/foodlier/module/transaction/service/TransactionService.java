package com.zerobase.foodlier.module.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;

public interface TransactionService {

    String sendSuggestion(MemberAuthDto memberAuthDto,
                            SuggestionForm form,
                            Long requestMemberId);

    TransactionDto approveSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId);

    String rejectSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId);
}
