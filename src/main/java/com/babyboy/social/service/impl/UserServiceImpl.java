package com.babyboy.social.service.impl;

import com.babyboy.social.domain.User;
import com.babyboy.social.repository.UserRepository;
import com.babyboy.social.security.SecurityUtils;
import com.babyboy.social.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        log.debug("Request to save JUser : {}", user);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> partialUpdate(User user) {
        log.debug("Request to partially update JUser : {}", user);

        return userRepository
            .findById(user.getId())
            .map(existingJUser -> {
                if (user.getLogin() != null) {
                    existingJUser.setLogin(user.getLogin());
                }
                if (user.getFirstName() != null) {
                    existingJUser.setFirstName(user.getFirstName());
                }
                if (user.getLastName() != null) {
                    existingJUser.setLastName(user.getLastName());
                }
                if (user.getEmail() != null) {
                    existingJUser.setEmail(user.getEmail());
                }
                if (user.getImageUrl() != null) {
                    existingJUser.setImageUrl(user.getImageUrl());
                }
                if (user.getLangKey() != null) {
                    existingJUser.setLangKey(user.getLangKey());
                }
                if (user.getCreatedBy() != null) {
                    existingJUser.setCreatedBy(user.getCreatedBy());
                }
                if (user.getCreatedDate() != null) {
                    existingJUser.setCreatedDate(user.getCreatedDate());
                }

                if (user.getLastModifiedBy() != null) {
                    existingJUser.setLastModifiedBy(user.getLastModifiedBy());
                }
                if (user.getLastModifiedDate() != null) {
                    existingJUser.setLastModifiedDate(user.getLastModifiedDate());
                }

                return existingJUser;
            })
            .map(userRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.debug("Request to get all JUsers");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findOne(String id) {
        log.debug("Request to get JUser : {}", id);
        return userRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete JUser : {}", id);
        userRepository.deleteById(id);
    }



}
