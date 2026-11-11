package org.example.orderservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Order-service error codes (range 6000-6999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(6000, "Order not found", HttpStatus.NOT_FOUND),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
