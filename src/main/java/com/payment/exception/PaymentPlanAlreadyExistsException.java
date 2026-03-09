package com.payment.exception;

public class PaymentPlanAlreadyExistsException extends RuntimeException {
    public PaymentPlanAlreadyExistsException(Long orderId) {
        super("A payment plan already exists for order id: " + orderId);
    }
}
