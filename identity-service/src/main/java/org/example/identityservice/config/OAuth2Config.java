package org.example.identityservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "outbound.identity")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OAuth2Config {
    String clientId;
    String clientSecret;
    String redirectUri;
    String grantType;
}
