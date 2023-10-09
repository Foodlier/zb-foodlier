package com.zerobase.foodlier.module.history.charge.service;


import com.zerobase.foodlier.module.payment.domain.model.Payment;

public interface PointChargeHistoryService {

    void createPointChargeHistory(Payment payment);

    void createPointCancelHistory(Payment payment);
}
