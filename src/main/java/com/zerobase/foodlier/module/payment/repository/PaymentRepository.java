package com.zerobase.foodlier.module.payment.repository;

import com.zerobase.foodlier.module.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByPaymentKey(String paymentKey);
}
