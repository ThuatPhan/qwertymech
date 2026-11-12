package org.example.userservice.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.common.exception.AppException;
import org.example.common.exception.CommonErrorCode;
import org.example.event.dto.NotificationEvent;
import org.example.userservice.constant.PredefinedRole;
import org.example.userservice.dto.request.UserCreationRequest;
import org.example.userservice.dto.request.UserUpdateRequest;
import org.example.userservice.dto.response.UserResponse;
import org.example.userservice.entity.User;
import org.example.userservice.exception.UserErrorCode;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    KeycloakService keycloakService;
    UserMapper userMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        String keycloakId = keycloakService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                PredefinedRole.USER_ROLE);

        User user = userMapper.toUser(request);
        user.setKeycloakId(keycloakId);

        User savedUser = userRepository.save(user);

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .templateCode("welcome")
                .recipient(savedUser.getEmail())
                .param(Map.of("firstName", savedUser.getFirstName(), "lastName", savedUser.getLastName()))
                .build();

        kafkaTemplate.send("notification-delivery", event);

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // With Keycloak tokens the principal name is the "sub" claim (the Keycloak user id)
        User user = userRepository
                .findByKeycloakId(authentication.getName())
                .orElseThrow(() -> new AppException(CommonErrorCode.UNAUTHENTICATED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse getUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(CommonErrorCode.UNAUTHENTICATED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public UserResponse updateUser(UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository
                .findByKeycloakId(authentication.getName())
                .orElseThrow(() -> new AppException(CommonErrorCode.UNAUTHENTICATED));

        if (request.getEmail() != null && userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
            throw new AppException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            keycloakService.resetPassword(user.getKeycloakId(), request.getPassword());
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && !request.getEmail().equals(user.getEmail())) {
            keycloakService.updateEmail(user.getKeycloakId(), request.getEmail());
        }

        userMapper.updateUser(user, request);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }
}
