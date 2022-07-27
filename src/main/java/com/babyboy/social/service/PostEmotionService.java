package com.babyboy.social.service;

import com.babyboy.social.domain.PostEmotion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PostEmotion}.
 */
public interface PostEmotionService {
    /**
     * Save a postEmotion.
     *
     * @param postEmotion the entity to save.
     * @return the persisted entity.
     */
    PostEmotion save(PostEmotion postEmotion);

    /**
     * Partially updates a postEmotion.
     *
     * @param postEmotion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostEmotion> partialUpdate(PostEmotion postEmotion);

    /**
     * Get all the postEmotions.
     *
     * @return the list of entities.
     */
    List<PostEmotion> findAll();

    /**
     * Get the "id" postEmotion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostEmotion> findOne(Long id);

    /**
     * Delete the "id" postEmotion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
