package org.example.userservice.service;

import java.util.List;

import jakarta.ws.rs.core.Response;

import org.example.common.exception.AppException;
import org.example.common.exception.CommonErrorCode;
import org.example.userservice.config.KeycloakConfig.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeycloakService {
    Keycloak keycloak;
    KeycloakProperties properties;

    public String createUser(String email, String password, String firstName, String lastName, String realmRole) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(true);

        RealmResource realm = keycloak.realm(properties.getRealm());

        try (Response response = realm.users().create(user)) {
            if (response.getStatus() == 409) {
                throw new AppException(CommonErrorCode.INVALID_REQUEST, "User already exists in Keycloak");
            }
            if (response.getStatus() >= 400) {
                throw new AppException(CommonErrorCode.UNCATEGORIZED, "Failed to create user in Keycloak");
            }

            String createdId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            setUserPassword(createdId, password);
            assignRealmRole(createdId, realmRole);

            return createdId;
        }
    }

    public void resetPassword(String keycloakId, String password) {
        setUserPassword(keycloakId, password);
    }

    public void updateEmail(String keycloakId, String email) {
        UserResource userResource =
                keycloak.realm(properties.getRealm()).users().get(keycloakId);
        UserRepresentation representation = userResource.toRepresentation();
        representation.setEmail(email);
        representation.setUsername(email);
        userResource.update(representation);
    }

    private void setUserPassword(String keycloakId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(properties.getRealm()).users().get(keycloakId).resetPassword(credential);
    }

    private void assignRealmRole(String keycloakId, String roleName) {
        RealmResource realm = keycloak.realm(properties.getRealm());
        RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
        realm.users().get(keycloakId).roles().realmLevel().add(List.of(role));
    }
}
