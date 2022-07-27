package com.babyboy.social.service.impl;

import com.babyboy.social.domain.PostImage;
import com.babyboy.social.repository.PostImageRepository;
import com.babyboy.social.service.PostImageService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PostImage}.
 */
@Service
@Transactional
public class PostImageServiceImpl implements PostImageService {

    private final Logger log = LoggerFactory.getLogger(PostImageServiceImpl.class);

    private final PostImageRepository postImageRepository;

    public PostImageServiceImpl(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Override
    public PostImage save(PostImage postImage) {
        log.debug("Request to save PostImage : {}", postImage);
        return postImageRepository.save(postImage);
    }

    @Override
    public Optional<PostImage> partialUpdate(PostImage postImage) {
        log.debug("Request to partially update PostImage : {}", postImage);

        return postImageRepository
            .findById(postImage.getId())
            .map(existingPostImage -> {
                if (postImage.getPostId() != null) {
                    existingPostImage.setPostId(postImage.getPostId());
                }
                if (postImage.getImage() != null) {
                    existingPostImage.setImage(postImage.getImage());
                }

                return existingPostImage;
            })
            .map(postImageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostImage> findAll() {
        log.debug("Request to get all PostImages");
        return postImageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostImage> findOne(Long id) {
        log.debug("Request to get PostImage : {}", id);
        return postImageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostImage : {}", id);
        postImageRepository.deleteById(id);
    }
}
