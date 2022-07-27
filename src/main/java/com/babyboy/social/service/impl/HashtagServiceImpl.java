package com.babyboy.social.service.impl;

import com.babyboy.social.domain.Hashtag;
import com.babyboy.social.repository.HashtagRepository;
import com.babyboy.social.service.HashtagService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hashtag}.
 */
@Service
@Transactional
public class HashtagServiceImpl implements HashtagService {

    private final Logger log = LoggerFactory.getLogger(HashtagServiceImpl.class);

    private final HashtagRepository hashtagRepository;

    public HashtagServiceImpl(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    @Override
    public Hashtag save(Hashtag hashtag) {
        log.debug("Request to save Hashtag : {}", hashtag);
        return hashtagRepository.save(hashtag);
    }

    @Override
    public Optional<Hashtag> partialUpdate(Hashtag hashtag) {
        log.debug("Request to partially update Hashtag : {}", hashtag);

        return hashtagRepository
            .findById(hashtag.getId())
            .map(existingHashtag -> {
                if (hashtag.getName() != null) {
                    existingHashtag.setName(hashtag.getName());
                }
                if (hashtag.getIcon() != null) {
                    existingHashtag.setIcon(hashtag.getIcon());
                }
                if (hashtag.getCreatedAt() != null) {
                    existingHashtag.setCreatedAt(hashtag.getCreatedAt());
                }

                return existingHashtag;
            })
            .map(hashtagRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hashtag> findAll() {
        log.debug("Request to get all Hashtags");
        return hashtagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hashtag> findOne(Long id) {
        log.debug("Request to get Hashtag : {}", id);
        return hashtagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hashtag : {}", id);
        hashtagRepository.deleteById(id);
    }
}
