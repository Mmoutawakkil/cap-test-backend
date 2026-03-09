package com.payment.dto;

import com.payment.model.PaymentOption;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PaymentPlanResponse {

    private Long id;
    private Long orderId;
    private PaymentOption paymentOption;
    private String paymentOptionLabel;
    private BigDecimal originalAmount;
    private BigDecimal feeAmount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<InstallmentResponse> installments;
}
