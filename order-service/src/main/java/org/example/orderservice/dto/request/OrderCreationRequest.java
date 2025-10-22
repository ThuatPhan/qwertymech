package org.example.orderservice.dto.request;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.example.orderservice.enums.PaymentMethod;
import org.example.orderservice.validator.EnumValidator;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {
    @NotBlank(message = "Shipping address can't be blank")
    String shippingAddress;

    @NotNull(message = "Payment method can't be null")
    @EnumValidator(
            enumClass = PaymentMethod.class,
            message = "Payment method must be one of: CASH, MOMO, VN_PAY, ZALO_PAY, BANK_TRANSFER")
    String paymentMethod;

    @NotEmpty(message = "Order items can't be empty")
    Set<@Valid OrderItemCreationRequest> items;
}
