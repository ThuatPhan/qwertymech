package org.example.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS(2000, "User already existed", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(2001, "Invalid password", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(2002, "Access denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(2003, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    ROLE_ALREADY_EXISTS(3000, "Role with same name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(3001, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(3002, "Permission with same name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND(3003, "Permission not found", HttpStatus.NOT_FOUND),

    CATEGORY_ALREADY_EXISTS(4000, "Category with same name already exists", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(4001, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(4002, "Product with same name already exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_FOUND(4003, "Product not found", HttpStatus.NOT_FOUND),

    CART_ITEM_NOT_FOUND(5000, "Cart item not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(6000, "Order not found", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
