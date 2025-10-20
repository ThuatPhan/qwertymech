package org.example.identityservice.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.event.dto.NotificationEvent;
import org.example.identityservice.constant.PredefinedRole;
import org.example.identityservice.dto.request.UserCreationRequest;
import org.example.identityservice.dto.request.UserUpdateRequest;
import org.example.identityservice.dto.response.UserResponse;
import org.example.identityservice.entity.Role;
import org.example.identityservice.entity.User;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.mapper.UserMapper;
import org.example.identityservice.repository.RoleRepository;
import org.example.identityservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository
                .findByName(PredefinedRole.USER_ROLE)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(PredefinedRole.USER_ROLE).build()));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(savedUser.getEmail())
                .param(Map.of("firstName", savedUser.getFirstName(), "lastName", savedUser.getLastName()))
                .build();

        // Public message to Kafka topic
        kafkaTemplate.send("notification-delivery", event);

        return userMapper.toUserResponse(savedUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(authority -> log.info(authority.toString()));

        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserResponse updateUser(UserUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        User user = userOpt.get();
        if (userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        if (request.getPassword() != null && !request.getEmail().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userMapper.updateUser(user, request);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }
}
