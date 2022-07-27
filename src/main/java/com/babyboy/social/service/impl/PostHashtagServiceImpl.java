package com.babyboy.social.service.impl;

import com.babyboy.social.domain.PostHashtag;
import com.babyboy.social.repository.PostHashtagRepository;
import com.babyboy.social.service.PostHashtagService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PostHashtag}.
 */
@Service
@Transactional
public class PostHashtagServiceImpl implements PostHashtagService {

    private final Logger log = LoggerFactory.getLogger(PostHashtagServiceImpl.class);

    private final PostHashtagRepository postHashtagRepository;

    public PostHashtagServiceImpl(PostHashtagRepository postHashtagRepository) {
        this.postHashtagRepository = postHashtagRepository;
    }

    @Override
    public PostHashtag save(PostHashtag postHashtag) {
        log.debug("Request to save PostHashtag : {}", postHashtag);
        return postHashtagRepository.save(postHashtag);
    }

    @Override
    public Optional<PostHashtag> partialUpdate(PostHashtag postHashtag) {
        log.debug("Request to partially update PostHashtag : {}", postHashtag);

        return postHashtagRepository
            .findById(postHashtag.getId())
            .map(existingPostHashtag -> {
                if (postHashtag.getPostId() != null) {
                    existingPostHashtag.setPostId(postHashtag.getPostId());
                }
                if (postHashtag.getHashtagId() != null) {
                    existingPostHashtag.setHashtagId(postHashtag.getHashtagId());
                }

                return existingPostHashtag;
            })
            .map(postHashtagRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostHashtag> findAll() {
        log.debug("Request to get all PostHashtags");
        return postHashtagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostHashtag> findOne(Long id) {
        log.debug("Request to get PostHashtag : {}", id);
        return postHashtagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostHashtag : {}", id);
        postHashtagRepository.deleteById(id);
    }
}
