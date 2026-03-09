package com.payment.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentOption {

    THREE_INSTALLMENTS("3 installments – no fees", 3),
    FOUR_INSTALLMENTS("4 installments – 5% fees", 4),
    BANK_TRANSFER("Bank transfer – +1 € fixed fee", 1);

    private final String label;
    private final int numberOfInstallments;
}
