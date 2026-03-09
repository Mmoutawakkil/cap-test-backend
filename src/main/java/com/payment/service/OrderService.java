package com.payment.service;

import com.payment.config.PaymentProperties;
import com.payment.dto.OrderRequest;
import com.payment.dto.OrderResponse;
import com.payment.exception.OrderNotFoundException;
import com.payment.model.Order;
import com.payment.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentProperties paymentProperties;
    private final PaymentPlanMapper paymentPlanMapper;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .amount(request.getAmount())
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        boolean eligible = order.getAmount()
                .compareTo(paymentProperties.getThreshold()) > 0;

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .amount(order.getAmount())
                .createdAt(order.getCreatedAt())
                .eligibleForInstallments(eligible)
                .paymentPlan(order.getPaymentPlan() != null
                        ? paymentPlanMapper.toResponse(order.getPaymentPlan())
                        : null)
                .build();
    }
}
