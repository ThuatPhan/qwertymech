package org.example.identityservice.config;

import java.util.HashSet;
import java.util.List;

import org.example.identityservice.constant.PredefinedRole;
import org.example.identityservice.entity.Role;
import org.example.identityservice.entity.User;
import org.example.identityservice.repository.RoleRepository;
import org.example.identityservice.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInitConfig {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    String EMAIL = "admin@qwertymech.com";
    String PASSWORD = "Bb123;;;";

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            if (!userRepository.existsByEmail(EMAIL)) {
                Role adminRole = Role.builder().name(PredefinedRole.ADMIN_ROLE).build();
                roleRepository.save(adminRole);

                Role.builder().name(PredefinedRole.USER_ROLE).build();

                User admin = User.builder()
                        .email(EMAIL)
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Qwerty")
                        .lastName("Mech")
                        .roles(new HashSet<>(List.of(adminRole)))
                        .build();

                userRepository.save(admin);

                log.warn("Admin account has been created with email: {} and password: {}", EMAIL, PASSWORD);
            } else {
                log.info("Application initialized successfully");
            }
        };
    }
}
