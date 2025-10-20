package org.example.notificationservice.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.SendEmailRequest;
import org.example.notificationservice.dto.SendEmailResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    Resend resend;

    public SendEmailResponse sendEmail(SendEmailRequest request) throws ResendException {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("QwertyMech <noreply@myec2.shop>")
                .to(request.getRecipient())
                .subject(request.getSubject())
                .html(request.getHtmlContent())
                .build();

        CreateEmailResponse response = resend.emails().send(params);

        return SendEmailResponse.builder().emailId(response.getId()).build();
    }
}
