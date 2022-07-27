package com.babyboy.social.service.impl;

import com.babyboy.social.domain.User;
import com.babyboy.social.repository.UserRepository;
import com.babyboy.social.service.UserService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                if (user.getPassword() != null) {
                    existingJUser.setPassword(user.getPassword());
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
                if (user.getActivated() != null) {
                    existingJUser.setActivated(user.getActivated());
                }
                if (user.getLangKey() != null) {
                    existingJUser.setLangKey(user.getLangKey());
                }
                if (user.getActivationKey() != null) {
                    existingJUser.setActivationKey(user.getActivationKey());
                }
                if (user.getResetKey() != null) {
                    existingJUser.setResetKey(user.getResetKey());
                }
                if (user.getCreatedBy() != null) {
                    existingJUser.setCreatedBy(user.getCreatedBy());
                }
                if (user.getCreatedDate() != null) {
                    existingJUser.setCreatedDate(user.getCreatedDate());
                }
                if (user.getResetDate() != null) {
                    existingJUser.setResetDate(user.getResetDate());
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
    public Optional<User> findOne(Long id) {
        log.debug("Request to get JUser : {}", id);
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JUser : {}", id);
        userRepository.deleteById(id);
    }
}
