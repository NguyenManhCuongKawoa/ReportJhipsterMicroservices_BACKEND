package com.babyboy.social.service.impl;

import com.babyboy.social.domain.PostEmotion;
import com.babyboy.social.repository.PostEmotionRepository;
import com.babyboy.social.service.PostEmotionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PostEmotion}.
 */
@Service
@Transactional
public class PostEmotionServiceImpl implements PostEmotionService {

    private final Logger log = LoggerFactory.getLogger(PostEmotionServiceImpl.class);

    private final PostEmotionRepository postEmotionRepository;

    public PostEmotionServiceImpl(PostEmotionRepository postEmotionRepository) {
        this.postEmotionRepository = postEmotionRepository;
    }

    @Override
    public PostEmotion save(PostEmotion postEmotion) {
        log.debug("Request to save PostEmotion : {}", postEmotion);
        return postEmotionRepository.save(postEmotion);
    }

    @Override
    public Optional<PostEmotion> partialUpdate(PostEmotion postEmotion) {
        log.debug("Request to partially update PostEmotion : {}", postEmotion);

        return postEmotionRepository
            .findById(postEmotion.getId())
            .map(existingPostEmotion -> {
                if (postEmotion.getUserId() != null) {
                    existingPostEmotion.setUserId(postEmotion.getUserId());
                }
                if (postEmotion.getPostId() != null) {
                    existingPostEmotion.setPostId(postEmotion.getPostId());
                }
                if (postEmotion.getEmotionId() != null) {
                    existingPostEmotion.setEmotionId(postEmotion.getEmotionId());
                }

                return existingPostEmotion;
            })
            .map(postEmotionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostEmotion> findAll() {
        log.debug("Request to get all PostEmotions");
        return postEmotionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostEmotion> findOne(Long id) {
        log.debug("Request to get PostEmotion : {}", id);
        return postEmotionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostEmotion : {}", id);
        postEmotionRepository.deleteById(id);
    }
}
