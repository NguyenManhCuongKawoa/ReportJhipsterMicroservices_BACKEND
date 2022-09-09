package com.babyboy.social.web.rest;

import com.babyboy.social.domain.User;
import com.babyboy.social.dto.request.RoleKeycloakRequest;
import com.babyboy.social.dto.request.UserKeycloakRequest;
import com.babyboy.social.dto.request.UserPasswKeycloakRequest;
import com.babyboy.social.service.KeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class KeycloakAPIResource {

    @Autowired
    private KeycloakService keycloakService;

    private final Logger log = LoggerFactory.getLogger(KeycloakAPIResource.class);

    @GetMapping("keycloak/users/realm/{realm}")
    public ResponseEntity<?> getAllUser(@PathVariable String realm, Pageable pageable) {
        log.debug("Request get all user user keycloak api");

        return keycloakService.getUsers(realm, pageable);
    }

    @PostMapping("keycloak/users/realm")
    public ResponseEntity<?> createUser(
        @RequestParam String realm,
        @RequestBody UserKeycloakRequest userKeycloakRequest) {

        log.debug("Request create user using keycloak api");

        ResponseEntity<?> responseEntity = keycloakService.createUser(realm, userKeycloakRequest);
        return responseEntity;
    }

    @PutMapping("keycloak/users/realm/reset-password")
    public ResponseEntity<?> changeUserPassword(
        @RequestBody UserPasswKeycloakRequest userPassKeycloakRequest
    ) {
        log.debug("Request change user password using keycloak api");

        ResponseEntity<?> responseEntity = keycloakService.changeUserPassword(userPassKeycloakRequest);
        return responseEntity;
    }

    @GetMapping("keycloak/roles/realm")
    public ResponseEntity<?> getAllRoleOfRealm(@RequestParam String realm) {
        log.debug("Request get all role of realm using keycloak api");

        ResponseEntity<?> responseEntity = keycloakService.getAllRoleOfRealm(realm);
        return responseEntity;
    }

    @PostMapping("keycloak/roles/realm/{realm}")
    public ResponseEntity<?> createRole(@PathVariable String realm, @RequestBody RoleKeycloakRequest roleKeycloakRequest) {
        log.debug("Request create Role using keycloak api");

        ResponseEntity<?> responseEntity = keycloakService.createRole(realm, roleKeycloakRequest);
        return responseEntity;
    }
}
