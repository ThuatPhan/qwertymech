package org.example.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Shared error codes (range 1000-1999). Each service defines its own codes in its own range.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CommonErrorCode implements ErrorCode {
    UNAUTHENTICATED(1000, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1001, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_REQUEST(1002, "Invalid request", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(1003, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
