package com.babyboy.social.service;

import com.babyboy.social.domain.Emotion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Emotion}.
 */
public interface EmotionService {
    /**
     * Save a emotion.
     *
     * @param emotion the entity to save.
     * @return the persisted entity.
     */
    Emotion save(Emotion emotion);

    /**
     * Partially updates a emotion.
     *
     * @param emotion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Emotion> partialUpdate(Emotion emotion);

    /**
     * Get all the emotions.
     *
     * @return the list of entities.
     */
    List<Emotion> findAll();

    /**
     * Get the "id" emotion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Emotion> findOne(Long id);

    /**
     * Delete the "id" emotion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
