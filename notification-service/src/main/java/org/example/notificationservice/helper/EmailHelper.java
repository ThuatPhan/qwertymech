package org.example.notificationservice.helper;

import java.util.Map;

import org.example.event.dto.NotificationEvent;
import org.example.notificationservice.dto.response.OrderResponse;
import org.example.notificationservice.dto.response.UserResponse;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailHelper {
    TemplateEngine templateEngine;
    ObjectMapper objectMapper;

    public String buildWelcomeEmail(NotificationEvent event) {
        Map<String, Object> param = event.getParam();
        String firstName = param.get("firstName").toString();
        String lastName = param.get("lastName").toString();

        Context context = new Context();
        context.setVariable("email", event.getRecipient());
        context.setVariable("name", lastName + " " + firstName);

        return templateEngine.process("welcome", context);
    }

    public String buildConfirmationEmail(NotificationEvent event) throws JsonProcessingException {
        Map<String, Object> param = event.getParam();

        OrderResponse order = objectMapper.readValue(param.get("order").toString(), OrderResponse.class);
        UserResponse user = objectMapper.readValue(param.get("user").toString(), UserResponse.class);

        Context context = new Context();
        context.setVariable("name", user.getLastName() + " " + user.getFirstName());
        context.setVariable("order", order);

        return templateEngine.process("order-confirmation", context);
    }
}
