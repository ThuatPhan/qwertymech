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
    INVALID_PASSWORD(2001, "Invalid password", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(2002, "Access denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(2003, "Unauthenticated", HttpStatus.UNAUTHORIZED),

    ROLE_ALREADY_EXISTS(3000, "Role with same name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(3001, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(3002, "Permission with same name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND(3003, "Permission not found", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_ERROR(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
