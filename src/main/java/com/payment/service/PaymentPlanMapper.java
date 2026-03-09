package com.payment.service;

import com.payment.dto.InstallmentResponse;
import com.payment.dto.PaymentPlanResponse;
import com.payment.model.Installment;
import com.payment.model.PaymentPlan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentPlanMapper {

    public PaymentPlanResponse toResponse(PaymentPlan plan) {
        return PaymentPlanResponse.builder()
                .id(plan.getId())
                .orderId(plan.getOrder().getId())
                .paymentOption(plan.getPaymentOption())
                .paymentOptionLabel(plan.getPaymentOption().getLabel())
                .originalAmount(plan.getOriginalAmount())
                .feeAmount(plan.getFeeAmount())
                .totalAmount(plan.getTotalAmount())
                .createdAt(plan.getCreatedAt())
                .installments(toInstallmentResponses(plan.getInstallments()))
                .build();
    }

    private List<InstallmentResponse> toInstallmentResponses(List<Installment> installments) {
        return installments.stream()
                .map(this::toInstallmentResponse)
                .toList();
    }

    private InstallmentResponse toInstallmentResponse(Installment installment) {
        return InstallmentResponse.builder()
                .id(installment.getId())
                .installmentNumber(installment.getInstallmentNumber())
                .amount(installment.getAmount())
                .dueDate(installment.getDueDate())
                .paid(installment.isPaid())
                .build();
    }
}
