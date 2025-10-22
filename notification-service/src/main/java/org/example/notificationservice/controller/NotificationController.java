package org.example.notificationservice.controller;

import org.example.event.dto.NotificationEvent;
import org.example.notificationservice.dto.request.SendEmailRequest;
import org.example.notificationservice.dto.response.SendEmailResponse;
import org.example.notificationservice.helper.EmailHelper;
import org.example.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resend.core.exception.ResendException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;
    EmailHelper emailHelper;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationEvent(NotificationEvent event) throws JsonProcessingException {
        String subject = event.getTemplateCode().equals("welcome") ? "Welcome to QwertyMech" : "Order Confirmation";
        String html = event.getTemplateCode().equals("welcome")
                ? emailHelper.buildWelcomeEmail(event)
                : emailHelper.buildConfirmationEmail(event);

        try {
            SendEmailResponse response = emailService.sendEmail(SendEmailRequest.builder()
                    .recipient(event.getRecipient())
                    .subject(subject)
                    .htmlContent(html)
                    .build());

            log.info("Email sent successfully to: {} with id: {}", event.getRecipient(), response.getEmailId());
        } catch (ResendException exception) {
            log.error("Failed to send email to: {}", event.getRecipient(), exception);
        }
    }
}
