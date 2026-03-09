package com.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Data
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {

    private BigDecimal threshold = new BigDecimal("100.00");

    private BigDecimal fourInstallmentFeeRate = new BigDecimal("0.05");

    private BigDecimal bankTransferFixedFee = new BigDecimal("1.00");
}
