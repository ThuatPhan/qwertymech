package org.example.paymentservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "payment.vnpay")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {
    String payUrl;
    String returnUrl;
    String tmnCode;
    String secretKey;
    String version;
    String command;
    String orderType;
}
