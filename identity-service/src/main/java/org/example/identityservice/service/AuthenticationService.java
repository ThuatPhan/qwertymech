package org.example.identityservice.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.example.event.dto.NotificationEvent;
import org.example.identityservice.config.OAuth2Config;
import org.example.identityservice.constant.PredefinedRole;
import org.example.identityservice.dto.request.*;
import org.example.identityservice.dto.response.AuthenticationResponse;
import org.example.identityservice.dto.response.ExchangeTokenResponse;
import org.example.identityservice.dto.response.IntrospectResponse;
import org.example.identityservice.dto.response.UserInfoResponse;
import org.example.identityservice.entity.InvalidateToken;
import org.example.identityservice.entity.Role;
import org.example.identityservice.entity.User;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.repository.InvalidateTokenRepository;
import org.example.identityservice.repository.RoleRepository;
import org.example.identityservice.repository.UserRepository;
import org.example.identityservice.repository.httpClient.OutboundIdentityClient;
import org.example.identityservice.repository.httpClient.OutboundUserClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    JwtService jwtService;
    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;
    OAuth2Config oauth2Config;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    RoleRepository roleRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public IntrospectResponse introspectToken(IntrospectRequest request) throws ParseException, JOSEException {
        boolean isValid = true;

        try {
            jwtService.verifyToken(request.getToken(), false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().isValid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isPasswordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isPasswordMatches) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = jwtService.verifyToken(request.getToken(), false);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidatedToken =
                InvalidateToken.builder().jwtId(jwtId).expiryTime(expiryTime).build();

        invalidateTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = jwtService.verifyToken(request.getToken(), true);

        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidatedToken =
                InvalidateToken.builder().jwtId(jwtId).expiryTime(expiryTime).build();
        invalidateTokenRepository.save(invalidatedToken);

        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String newAccessToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(newAccessToken).build();
    }

    public AuthenticationResponse outboundAuthenticate(String code) throws JOSEException {
        ExchangeTokenRequest request = ExchangeTokenRequest.builder()
                .clientId(oauth2Config.getClientId())
                .clientSecret(oauth2Config.getClientSecret())
                .code(code)
                .redirectUri(oauth2Config.getRedirectUri())
                .grantType(oauth2Config.getGrantType())
                .build();

        ExchangeTokenResponse response = outboundIdentityClient.exchangeToken(request);

        UserInfoResponse userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        User newUser = onboardUser(userInfo);

        String accessToken = jwtService.generateToken(newUser);

        return AuthenticationResponse.builder().token(accessToken).build();
    }

    private User onboardUser(UserInfoResponse userInfo) {
        Set<Role> roles = new HashSet<>();
        roleRepository.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        User savedUser = userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            String firstname = userInfo.getGivenName();
            String lastname = userInfo.getFamilyName();

            return userRepository.save(User.builder()
                    .email(userInfo.getEmail())
                    .firstName(firstname)
                    .lastName(lastname)
                    .email(userInfo.getEmail())
                    .avatar(userInfo.getPicture())
                    .roles(roles)
                    .build());
        });

        NotificationEvent event = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(savedUser.getEmail())
                .param(Map.of("firstName", savedUser.getFirstName(), "lastName", savedUser.getLastName()))
                .build();

        // Public message to Kafka topic
        kafkaTemplate.send("notification-delivery", event);

        return savedUser;
    }
}
