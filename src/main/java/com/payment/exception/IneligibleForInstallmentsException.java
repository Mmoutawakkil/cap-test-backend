package com.payment.exception;

import java.math.BigDecimal;

public class IneligibleForInstallmentsException extends RuntimeException {
    public IneligibleForInstallmentsException(BigDecimal amount, BigDecimal threshold) {
        super(String.format(
            "Order amount %.2f € does not exceed the installment threshold of %.2f €",
            amount, threshold
        ));
    }
}
