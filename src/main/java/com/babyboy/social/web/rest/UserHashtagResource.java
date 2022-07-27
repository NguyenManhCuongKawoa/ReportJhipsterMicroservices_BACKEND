package com.babyboy.social.web.rest;

import com.babyboy.social.domain.UserHashtag;
import com.babyboy.social.repository.UserHashtagRepository;
import com.babyboy.social.service.UserHashtagService;
import com.babyboy.social.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.babyboy.social.domain.UserHashtag}.
 */
@RestController
@RequestMapping("/api")
public class UserHashtagResource {

    private final Logger log = LoggerFactory.getLogger(UserHashtagResource.class);

    private static final String ENTITY_NAME = "backendsocialUserHashtag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserHashtagService userHashtagService;

    private final UserHashtagRepository userHashtagRepository;

    public UserHashtagResource(UserHashtagService userHashtagService, UserHashtagRepository userHashtagRepository) {
        this.userHashtagService = userHashtagService;
        this.userHashtagRepository = userHashtagRepository;
    }

    /**
     * {@code POST  /user-hashtags} : Create a new userHashtag.
     *
     * @param userHashtag the userHashtag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userHashtag, or with status {@code 400 (Bad Request)} if the userHashtag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-hashtags")
    public ResponseEntity<UserHashtag> createUserHashtag(@Valid @RequestBody UserHashtag userHashtag) throws URISyntaxException {
        log.debug("REST request to save UserHashtag : {}", userHashtag);
        if (userHashtag.getId() != null) {
            throw new BadRequestAlertException("A new userHashtag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserHashtag result = userHashtagService.save(userHashtag);
        return ResponseEntity
            .created(new URI("/api/user-hashtags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-hashtags/:id} : Updates an existing userHashtag.
     *
     * @param id the id of the userHashtag to save.
     * @param userHashtag the userHashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHashtag,
     * or with status {@code 400 (Bad Request)} if the userHashtag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userHashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-hashtags/{id}")
    public ResponseEntity<UserHashtag> updateUserHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserHashtag userHashtag
    ) throws URISyntaxException {
        log.debug("REST request to update UserHashtag : {}, {}", id, userHashtag);
        if (userHashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserHashtag result = userHashtagService.save(userHashtag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userHashtag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-hashtags/:id} : Partial updates given fields of an existing userHashtag, field will ignore if it is null
     *
     * @param id the id of the userHashtag to save.
     * @param userHashtag the userHashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userHashtag,
     * or with status {@code 400 (Bad Request)} if the userHashtag is not valid,
     * or with status {@code 404 (Not Found)} if the userHashtag is not found,
     * or with status {@code 500 (Internal Server Error)} if the userHashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-hashtags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserHashtag> partialUpdateUserHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserHashtag userHashtag
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserHashtag partially : {}, {}", id, userHashtag);
        if (userHashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userHashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userHashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserHashtag> result = userHashtagService.partialUpdate(userHashtag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userHashtag.getId().toString())
        );
    }

    /**
     * {@code GET  /user-hashtags} : get all the userHashtags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userHashtags in body.
     */
    @GetMapping("/user-hashtags")
    public List<UserHashtag> getAllUserHashtags() {
        log.debug("REST request to get all UserHashtags");
        return userHashtagService.findAll();
    }

    /**
     * {@code GET  /user-hashtags/:id} : get the "id" userHashtag.
     *
     * @param id the id of the userHashtag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userHashtag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-hashtags/{id}")
    public ResponseEntity<UserHashtag> getUserHashtag(@PathVariable Long id) {
        log.debug("REST request to get UserHashtag : {}", id);
        Optional<UserHashtag> userHashtag = userHashtagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userHashtag);
    }

    /**
     * {@code DELETE  /user-hashtags/:id} : delete the "id" userHashtag.
     *
     * @param id the id of the userHashtag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-hashtags/{id}")
    public ResponseEntity<Void> deleteUserHashtag(@PathVariable Long id) {
        log.debug("REST request to delete UserHashtag : {}", id);
        userHashtagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
