package org.example.notificationservice.config;

import com.resend.Resend;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResendConfig {
    @Value("${resend.api-key}")
    String RESEND_API_KEY;

    @Bean
    public Resend resend() {
        return new Resend(RESEND_API_KEY);
    }
}
