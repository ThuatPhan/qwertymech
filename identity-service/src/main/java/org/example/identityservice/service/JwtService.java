package org.example.identityservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.example.identityservice.entity.Role;
import org.example.identityservice.entity.User;
import org.example.identityservice.exception.AppException;
import org.example.identityservice.exception.ErrorCode;
import org.example.identityservice.repository.InvalidateTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {
    final InvalidateTokenRepository invalidateTokenRepository;

    @Value("${jwt.issuer}")
    String ISSUER;

    @Value("${jwt.secret}")
    String SECRET;

    @Value("${jwt.expiry-seconds}")
    Long EXPIRY_SECONDS;

    public String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .issuer(ISSUER)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(EXPIRY_SECONDS, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        jwsObject.sign(new MACSigner(SECRET.getBytes()));

        return jwsObject.serialize();
    }

    public SignedJWT verifyToken(String accessToken, boolean allowExpiredToken) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SECRET.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(accessToken);

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.REVOKED_TOKEN);
        }

        boolean isVerified = signedJWT.verify(verifier);
        if (!isVerified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (!allowExpiredToken) {
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expiryTime.before(new Date())) {
                throw new AppException(ErrorCode.EXPIRED_TOKEN);
            }
        }

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner scopes = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            for (Role role : user.getRoles()) {
                scopes.add(role.getName());
            }
        }

        return scopes.toString();
    }
}
