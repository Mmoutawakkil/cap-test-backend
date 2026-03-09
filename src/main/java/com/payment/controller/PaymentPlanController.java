package com.payment.controller;

import com.payment.dto.PaymentPlanRequest;
import com.payment.dto.PaymentPlanResponse;
import com.payment.service.PaymentPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/payment-plan")
@RequiredArgsConstructor
public class PaymentPlanController {

    private final PaymentPlanService paymentPlanService;

    @PostMapping
    public ResponseEntity<PaymentPlanResponse> createPaymentPlan(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentPlanRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentPlanService.createPaymentPlan(orderId, request));
    }

    @GetMapping
    public ResponseEntity<PaymentPlanResponse> getPaymentPlan(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentPlanService.getPaymentPlan(orderId));
    }
}
