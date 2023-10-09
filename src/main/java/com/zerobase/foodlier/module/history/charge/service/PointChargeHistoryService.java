package com.zerobase.foodlier.module.history.charge.service;


import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PointChargeHistoryService {

    void createPointChargeHistory(Payment payment);

    void createPointCancelHistory(Payment payment);

    List<PointChargeHistoryDto> getPointHistory(MemberAuthDto memberAuthDto,
                                                Pageable pageable);
}
