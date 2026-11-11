package org.example.identityservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Identity-service error codes (range 2000-2999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum IdentityErrorCode implements ErrorCode {
    USER_ALREADY_EXISTS(2000, "User already existed", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(2001, "Invalid password", HttpStatus.UNAUTHORIZED),
    ROLE_ALREADY_EXISTS(2002, "Role with same name already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(2003, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_ALREADY_EXISTS(2004, "Permission with same name already exists", HttpStatus.CONFLICT),
    PERMISSION_NOT_FOUND(2005, "Permission not found", HttpStatus.NOT_FOUND),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
