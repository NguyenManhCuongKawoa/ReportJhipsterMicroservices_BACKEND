package com.babyboy.social.service;

import com.babyboy.social.domain.UserHashtag;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserHashtag}.
 */
public interface UserHashtagService {
    /**
     * Save a userHashtag.
     *
     * @param userHashtag the entity to save.
     * @return the persisted entity.
     */
    UserHashtag save(UserHashtag userHashtag);

    /**
     * Partially updates a userHashtag.
     *
     * @param userHashtag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserHashtag> partialUpdate(UserHashtag userHashtag);

    /**
     * Get all the userHashtags.
     *
     * @return the list of entities.
     */
    List<UserHashtag> findAll();

    /**
     * Get the "id" userHashtag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserHashtag> findOne(Long id);

    /**
     * Delete the "id" userHashtag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
