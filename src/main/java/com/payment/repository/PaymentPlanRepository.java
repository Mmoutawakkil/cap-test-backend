package com.payment.repository;

import com.payment.model.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
    Optional<PaymentPlan> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}
