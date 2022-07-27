package com.babyboy.social.service;

import com.babyboy.social.domain.PostImage;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PostImage}.
 */
public interface PostImageService {
    /**
     * Save a postImage.
     *
     * @param postImage the entity to save.
     * @return the persisted entity.
     */
    PostImage save(PostImage postImage);

    /**
     * Partially updates a postImage.
     *
     * @param postImage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostImage> partialUpdate(PostImage postImage);

    /**
     * Get all the postImages.
     *
     * @return the list of entities.
     */
    List<PostImage> findAll();

    /**
     * Get the "id" postImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostImage> findOne(Long id);

    /**
     * Delete the "id" postImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
