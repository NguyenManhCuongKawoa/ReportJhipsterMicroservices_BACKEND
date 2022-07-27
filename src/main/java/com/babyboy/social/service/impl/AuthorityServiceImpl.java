package com.babyboy.social.service.impl;

import com.babyboy.social.domain.Authority;
import com.babyboy.social.repository.AuthorityRepository;
import com.babyboy.social.service.AuthorityService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Authority}.
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority save(Authority authority) {
        log.debug("Request to save JAuthority : {}", authority);
        return authorityRepository.save(authority);
    }

    @Override
    public Optional<Authority> partialUpdate(Authority authority) {
        log.debug("Request to partially update JAuthority : {}", authority);

        return authorityRepository
            .findById(authority.getName())
            .map(existingJAuthority -> {
                if (authority.getName() != null) {
                    existingJAuthority.setName(authority.getName());
                }

                return existingJAuthority;
            })
            .map(authorityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Authority> findAll() {
        log.debug("Request to get all JAuthorities");
        return authorityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findOne(String name) {
        log.debug("Request to get JAuthority : {}", name);
        return authorityRepository.findById(name);
    }

    @Override
    public void delete(String name) {
        log.debug("Request to delete JAuthority : {}", name);
        authorityRepository.deleteById(name);
    }
}
