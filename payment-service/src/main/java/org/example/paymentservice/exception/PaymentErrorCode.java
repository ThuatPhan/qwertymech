package org.example.paymentservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Payment-service error codes (range 7000-7999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_FAILED(7000, "Payment failed", HttpStatus.BAD_REQUEST),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
