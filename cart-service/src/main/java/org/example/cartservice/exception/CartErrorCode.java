package org.example.cartservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Cart-service error codes (range 5000-5999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CartErrorCode implements ErrorCode {
    CART_ITEM_NOT_FOUND(5000, "Cart item not found", HttpStatus.NOT_FOUND),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
