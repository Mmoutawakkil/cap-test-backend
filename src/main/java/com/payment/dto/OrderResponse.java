package com.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private String customerName;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private boolean eligibleForInstallments;
    private PaymentPlanResponse paymentPlan;
}
