package com.babyboy.social.service;

import com.babyboy.social.domain.PostHashtag;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PostHashtag}.
 */
public interface PostHashtagService {
    /**
     * Save a postHashtag.
     *
     * @param postHashtag the entity to save.
     * @return the persisted entity.
     */
    PostHashtag save(PostHashtag postHashtag);

    /**
     * Partially updates a postHashtag.
     *
     * @param postHashtag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostHashtag> partialUpdate(PostHashtag postHashtag);

    /**
     * Get all the postHashtags.
     *
     * @return the list of entities.
     */
    List<PostHashtag> findAll();

    /**
     * Get the "id" postHashtag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostHashtag> findOne(Long id);

    /**
     * Delete the "id" postHashtag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
