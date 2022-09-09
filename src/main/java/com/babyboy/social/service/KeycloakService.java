package com.babyboy.social.service;

import com.babyboy.social.dto.UserChangeaPassKeycloak;
import com.babyboy.social.dto.request.RoleKeycloakRequest;
import com.babyboy.social.dto.request.UserKeycloakRequest;
import com.babyboy.social.dto.request.UserPasswKeycloakRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class KeycloakService {

    @Value("${keycloak.baseurl}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();


    KeycloakService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public  ResponseEntity<?> getUsers(String realm, Pageable pageable) {

        String token = bearerTokenResolver.resolve(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        );

        String surfixUrl = "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        return response;
    }

    public ResponseEntity<?> createUser(String realm, UserKeycloakRequest userKeycloakRequest) {
        String token = bearerTokenResolver.resolve(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        );

        String surfixUrl = "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> request = new HttpEntity<>(userKeycloakRequest, headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.postForObject(
            baseUrl + surfixUrl, request, Object.class);
        return response;
    }

    public ResponseEntity<?> changeUserPassword(UserPasswKeycloakRequest userPassKeycloakRequest) {
        String token = bearerTokenResolver.resolve(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        );

        String surfixUrl = "/admin/realms/" + userPassKeycloakRequest.getRealm() + "/users/" +
            userPassKeycloakRequest.getUserId() + "/reset-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);


        HttpEntity<Object> request = new HttpEntity<>(new UserChangeaPassKeycloak(userPassKeycloakRequest.getPassword()), headers);

        ResponseEntity<?> response = ( ResponseEntity<?> ) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.PUT, request, Object.class);
        return response;
    }

    public ResponseEntity<?> getAllRoleOfRealm(String realm) {
        String token = bearerTokenResolver.resolve(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        );

        String surfixUrl = "/admin/realms/" + realm + "/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.GET, request, Object.class);
        return response;
    }
    public ResponseEntity<?> createRole(String realm, RoleKeycloakRequest roleKeycloakRequest) {
        String token = bearerTokenResolver.resolve(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        );

        String surfixUrl = "/admin/realms/" + realm + "/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> request = new HttpEntity<>(roleKeycloakRequest, headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.postForObject(
            baseUrl + surfixUrl, request, Object.class);
        return response;
    }


}
