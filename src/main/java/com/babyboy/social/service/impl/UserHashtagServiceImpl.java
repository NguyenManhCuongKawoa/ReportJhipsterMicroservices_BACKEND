package com.babyboy.social.service.impl;

import com.babyboy.social.domain.UserHashtag;
import com.babyboy.social.repository.UserHashtagRepository;
import com.babyboy.social.service.UserHashtagService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserHashtag}.
 */
@Service
@Transactional
public class UserHashtagServiceImpl implements UserHashtagService {

    private final Logger log = LoggerFactory.getLogger(UserHashtagServiceImpl.class);

    private final UserHashtagRepository userHashtagRepository;

    public UserHashtagServiceImpl(UserHashtagRepository userHashtagRepository) {
        this.userHashtagRepository = userHashtagRepository;
    }

    @Override
    public UserHashtag save(UserHashtag userHashtag) {
        log.debug("Request to save UserHashtag : {}", userHashtag);
        return userHashtagRepository.save(userHashtag);
    }

    @Override
    public Optional<UserHashtag> partialUpdate(UserHashtag userHashtag) {
        log.debug("Request to partially update UserHashtag : {}", userHashtag);

        return userHashtagRepository
            .findById(userHashtag.getId())
            .map(existingUserHashtag -> {
                if (userHashtag.getUserId() != null) {
                    existingUserHashtag.setUserId(userHashtag.getUserId());
                }
                if (userHashtag.getHashtagId() != null) {
                    existingUserHashtag.setHashtagId(userHashtag.getHashtagId());
                }

                return existingUserHashtag;
            })
            .map(userHashtagRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserHashtag> findAll() {
        log.debug("Request to get all UserHashtags");
        return userHashtagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserHashtag> findOne(Long id) {
        log.debug("Request to get UserHashtag : {}", id);
        return userHashtagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserHashtag : {}", id);
        userHashtagRepository.deleteById(id);
    }
}
