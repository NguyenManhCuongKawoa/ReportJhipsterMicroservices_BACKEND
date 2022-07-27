package com.babyboy.social.service;

import com.babyboy.social.domain.Hashtag;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Hashtag}.
 */
public interface HashtagService {
    /**
     * Save a hashtag.
     *
     * @param hashtag the entity to save.
     * @return the persisted entity.
     */
    Hashtag save(Hashtag hashtag);

    /**
     * Partially updates a hashtag.
     *
     * @param hashtag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Hashtag> partialUpdate(Hashtag hashtag);

    /**
     * Get all the hashtags.
     *
     * @return the list of entities.
     */
    List<Hashtag> findAll();

    /**
     * Get the "id" hashtag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Hashtag> findOne(Long id);

    /**
     * Delete the "id" hashtag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
