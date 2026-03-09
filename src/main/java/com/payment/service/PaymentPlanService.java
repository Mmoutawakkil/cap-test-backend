package com.payment.service;

import com.payment.config.PaymentProperties;
import com.payment.dto.PaymentPlanRequest;
import com.payment.dto.PaymentPlanResponse;
import com.payment.exception.IneligibleForInstallmentsException;
import com.payment.exception.OrderNotFoundException;
import com.payment.exception.PaymentPlanAlreadyExistsException;
import com.payment.model.*;
import com.payment.repository.OrderRepository;
import com.payment.repository.PaymentPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentPlanService {

    private final OrderRepository orderRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentProperties paymentProperties;
    private final PaymentPlanMapper paymentPlanMapper;

    @Transactional
    public PaymentPlanResponse createPaymentPlan(Long orderId, PaymentPlanRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        assertEligible(order);
        assertNoPlanExists(orderId);

        PaymentPlan plan = buildPlan(order, request.getPaymentOption());
        PaymentPlan saved = paymentPlanRepository.save(plan);

        return paymentPlanMapper.toResponse(saved);
    }

    public PaymentPlanResponse getPaymentPlan(Long orderId) {
        PaymentPlan plan = paymentPlanRepository.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return paymentPlanMapper.toResponse(plan);
    }

    private void assertEligible(Order order) {
        if (order.getAmount().compareTo(paymentProperties.getThreshold()) <= 0) {
            throw new IneligibleForInstallmentsException(
                    order.getAmount(), paymentProperties.getThreshold());
        }
    }

    private void assertNoPlanExists(Long orderId) {
        if (paymentPlanRepository.existsByOrderId(orderId)) {
            throw new PaymentPlanAlreadyExistsException(orderId);
        }
    }

    private PaymentPlan buildPlan(Order order, PaymentOption option) {
        BigDecimal fee = computeFee(order.getAmount(), option);
        BigDecimal total = order.getAmount().add(fee);

        PaymentPlan plan = PaymentPlan.builder()
                .order(order)
                .paymentOption(option)
                .originalAmount(order.getAmount())
                .feeAmount(fee)
                .totalAmount(total)
                .build();

        List<Installment> schedule = generateSchedule(plan, total, option);
        plan.setInstallments(schedule);

        return plan;
    }

    private BigDecimal computeFee(BigDecimal amount, PaymentOption option) {
        return switch (option) {
            case THREE_INSTALLMENTS -> BigDecimal.ZERO;
            case FOUR_INSTALLMENTS  -> amount
                    .multiply(paymentProperties.getFourInstallmentFeeRate())
                    .setScale(2, RoundingMode.HALF_UP);
            case BANK_TRANSFER      -> paymentProperties.getBankTransferFixedFee();
        };
    }

    private List<Installment> generateSchedule(PaymentPlan plan,
                                                BigDecimal total,
                                                PaymentOption option) {
        int count = option.getNumberOfInstallments();
        BigDecimal installmentAmount = total
                .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

        BigDecimal sumOfOthers = installmentAmount.multiply(BigDecimal.valueOf(count - 1));
        BigDecimal lastAmount = total.subtract(sumOfOthers);

        List<Installment> installments = new ArrayList<>();
        LocalDate baseDate = LocalDate.now();

        for (int i = 0; i <= count; i++) {
            BigDecimal amount = (i == count) ? lastAmount : installmentAmount;
            installments.add(Installment.builder()
                    .paymentPlan(plan)
                    .installmentNumber(i)
                    .amount(amount)
                    .dueDate(baseDate.plusMonths(i))
                    .build());
        }

        return installments;
    }
}
