package org.example.notificationservice.controller;

import com.resend.core.exception.ResendException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.event.dto.NotificationEvent;
import org.example.notificationservice.dto.SendEmailRequest;
import org.example.notificationservice.dto.SendEmailResponse;
import org.example.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;
    TemplateEngine templateEngine;

    @KafkaListener(topics = "notification-delivery")
    public void listenUserCreatedEvent(NotificationEvent event) {
        String email = event.getRecipient();
        String firstName = event.getParam().get("firstName").toString();
        String lastName = event.getParam().get("lastName").toString();

        Context context = new Context();
        context.setVariable("email", event.getRecipient());
        context.setVariable("name", lastName + " " + firstName);

        String html = templateEngine.process("welcome", context);

        try {
            SendEmailResponse response = emailService.sendEmail(SendEmailRequest.builder()
                    .recipient(email)
                    .subject("Welcome to QwertyMech")
                    .htmlContent(html)
                    .build());

            log.info("Email sent successfully to: {} with id: {}", email, response.getEmailId());
        } catch (ResendException exception) {
            log.error("Failed to send email to: {}", email, exception);
        }
    }

}
