package com.zerobase.foodlier.module.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;

public interface TransactionService {

    String sendSuggestion(MemberAuthDto memberAuthDto,
                            SuggestionForm form,
                            Long requestMemberId);

    String approveSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId);

    String rejectSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId);
}
