package com.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class InstallmentResponse {

    private Long id;
    private Integer installmentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private boolean paid;
}
