package org.example.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_ALREADY_EXISTS(2000, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2001, "User not found", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(3001, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD(3002, "Invalid password", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(3003, "Access denied", HttpStatus.FORBIDDEN),
    REVOKED_TOKEN(3004, "Token is revoked", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(3005, "Invalid token", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(3006, "Token has expired", HttpStatus.UNAUTHORIZED),

    ROLE_ALREADY_EXISTS(4000, "Role with same name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(4001, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(4002, "Permission with same name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND(4003, "Permission not found", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
