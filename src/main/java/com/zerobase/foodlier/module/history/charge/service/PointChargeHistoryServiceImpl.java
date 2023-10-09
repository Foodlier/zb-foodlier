package com.zerobase.foodlier.module.history.charge.service;

import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointChargeHistoryServiceImpl implements PointChargeHistoryService{
    private final PointChargeHistoryRepository pointChargeHistoryRepository;

    @Override
    public void createPointChargeHistory(Payment payment){
        pointChargeHistoryRepository.save(PointChargeHistory.builder()
                .paymentKey(payment.getPaymentKey())
                .member(payment.getMember())
                .chargePoint(payment.getAmount())
                .build());
    }

    @Override
    public void createPointCancelHistory(Payment payment){
        pointChargeHistoryRepository.save(PointChargeHistory.builder()
                .paymentKey(payment.getPaymentKey())
                .member(payment.getMember())
                .chargePoint(-payment.getAmount())
                .build());
    }
}
