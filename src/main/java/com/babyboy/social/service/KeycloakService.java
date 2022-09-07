package com.babyboy.social.service;

import com.babyboy.social.domain.User;
import com.babyboy.social.dto.request.RoleKeycloakRequest;
import com.babyboy.social.dto.request.UserKeycloakRequest;
import com.babyboy.social.dto.request.UserPasswKeycloakRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class KeycloakService {

    @Value("${keycloak.baseurl}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    KeycloakService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Page<User> getUsers(String realm, Pageable pageable) {
        String surfixUrl = "/admin/realms/" + realm + "/users";
        String token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Object> request = new HttpEntity<>(headers);

        List<User> response = (List<User>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.GET, request, Object.class);
        return new PageImpl<>(response, pageable, response.size());
    }

    public ResponseEntity<?> createUser(String realm, UserKeycloakRequest userKeycloakRequest) {
        String surfixUrl = "/admin/realms/" + realm + "/users";
        String token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        JSONObject object = new JSONObject();
        object.put("username", userKeycloakRequest.getUsername());
        object.put("enabled", userKeycloakRequest.getEnabled());
        object.put("emailVerified", userKeycloakRequest.getEmailVerified());
        object.put("firstName", userKeycloakRequest.getFirstName());
        object.put("lastName", userKeycloakRequest.getLastName());
        object.put("email", userKeycloakRequest.getEmail());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.POST, request, Object.class);
        return response;
    }

    public ResponseEntity<?> changeUserPassword(UserPasswKeycloakRequest userPassKeycloakRequest) {
        String surfixUrl = "/admin/realms/" + userPassKeycloakRequest.getRealm() + "/users/" +
            userPassKeycloakRequest.getUserId() + "/reset-password";
        String token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        JSONObject object = new JSONObject();
        object.put("type", "password");
        object.put("value", userPassKeycloakRequest.getPassword());
        object.put("temporary", true);
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.PUT, request, Object.class);
        return response;
    }

    public ResponseEntity<?> getAllRoleOfRealm(String realm) {
        String surfixUrl = "/admin/realms/" + realm + "/roles";
        String token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.GET, request, Object.class);
        return response;
    }
    public ResponseEntity<?> createRole(RoleKeycloakRequest roleKeycloakRequest) {
        String surfixUrl = "/admin/realms/" + roleKeycloakRequest.getRealm() + "/roles";
        String token = "";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        JSONObject object = new JSONObject();
        object.put("name", roleKeycloakRequest.getName());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = ( ResponseEntity<?>) restTemplate.exchange(
            baseUrl + surfixUrl, HttpMethod.POST, request, Object.class);
        return response;
    }


}
