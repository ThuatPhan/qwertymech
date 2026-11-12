package org.example.userservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableConfigurationProperties(KeycloakConfig.KeycloakProperties.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeycloakConfig {

    @Data
    @ConfigurationProperties(prefix = "keycloak")
    public static class KeycloakProperties {
        String serverUrl;
        String realm;
        String adminUsername;
        String adminPassword;
        String clientId;
    }

    @Bean
    public Keycloak keycloak(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getServerUrl())
                .realm("master")
                .clientId("admin-cli")
                .username(properties.getAdminUsername())
                .password(properties.getAdminPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
