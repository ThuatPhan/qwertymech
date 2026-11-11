package org.example.common.handler;

import java.util.List;

import org.example.common.dto.ApiResponse;
import org.example.common.exception.AppException;
import org.example.common.exception.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAppException(AppException exception) {
        log.warn("Application error [{}]: {}", exception.getErrorCode().getCode(), exception.getMessage());
        return build(exception.getErrorCode(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<ApiResponse.FieldViolation> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        new ApiResponse.FieldViolation(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        log.warn("Validation failed: {}", errors);

        return build(CommonErrorCode.INVALID_REQUEST, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAccessDeniedException(AccessDeniedException exception) {
        log.warn("Access denied: {}", exception.getMessage());
        return build(CommonErrorCode.ACCESS_DENIED, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handlingAuthenticationException(AuthenticationException exception) {
        log.warn("Authentication failed: {}", exception.getMessage());
        return build(CommonErrorCode.UNAUTHENTICATED, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handlingException(Exception exception) {
        log.error("Unhandled error: {}", exception.getMessage(), exception);
        return build(CommonErrorCode.UNCATEGORIZED, null);
    }

    private ResponseEntity<ApiResponse<Object>> build(
            org.example.common.exception.ErrorCode errorCode, List<ApiResponse.FieldViolation> errors) {
        return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.error(errorCode, errors));
    }
}
