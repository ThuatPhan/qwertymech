package org.example.productservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Product-service error codes (range 4000-4999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ProductErrorCode implements ErrorCode {
    CATEGORY_ALREADY_EXISTS(4000, "Category with same name already exists", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(4001, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(4002, "Product with same name already exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND(4003, "Product not found", HttpStatus.NOT_FOUND),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
