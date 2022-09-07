package com.babyboy.social.service;

import com.babyboy.social.domain.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {
    /**
     * Save a jUser.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    User save(User user);

    /**
     * Partially updates a jUser.
     *
     * @param user the entity to update partially.
     * @return the persisted entity.
     */
    Optional<User> partialUpdate(User user);

    /**
     * Get all the jUsers.
     *
     * @return the list of entities.
     */
    List<User> findAll();

    /**
     * Get the "id" jUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<User> findOne(String id);

    /**
     * Delete the "id" jUser.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

}
