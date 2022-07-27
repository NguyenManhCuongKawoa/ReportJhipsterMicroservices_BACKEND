package com.babyboy.social.service;

import com.babyboy.social.domain.Authority;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Authority}.
 */
public interface AuthorityService {
    /**
     * Save a jAuthority.
     *
     * @param authority the entity to save.
     * @return the persisted entity.
     */
    Authority save(Authority authority);

    /**
     * Partially updates a jAuthority.
     *
     * @param authority the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Authority> partialUpdate(Authority authority);

    /**
     * Get all the jAuthorities.
     *
     * @return the list of entities.
     */
    List<Authority> findAll();

    /**
     * Get the "id" jAuthority.
     *
     * @param name the id of the entity.
     * @return the entity.
     */
    Optional<Authority> findOne(String name);

    /**
     * Delete the "id" jAuthority.
     *
     * @param name the id of the entity.
     */
    void delete(String name);
}
