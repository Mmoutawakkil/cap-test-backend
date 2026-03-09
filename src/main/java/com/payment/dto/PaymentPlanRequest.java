package com.payment.dto;

import com.payment.model.PaymentOption;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentPlanRequest {

    @NotNull(message = "Payment option is required")
    private PaymentOption paymentOption;
}
