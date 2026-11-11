package org.example.notificationservice.exception;

import org.example.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Notification-service error codes (range 8000-8999).
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum NotificationErrorCode implements ErrorCode {
    EMAIL_SENDING_FAILED(8000, "Failed to send email", HttpStatus.BAD_GATEWAY),
    ;

    final int code;
    final String message;
    final HttpStatus statusCode;
}
