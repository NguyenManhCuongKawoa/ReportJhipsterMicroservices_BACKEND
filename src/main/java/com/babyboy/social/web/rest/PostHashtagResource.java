package com.babyboy.social.web.rest;

import com.babyboy.social.domain.PostHashtag;
import com.babyboy.social.repository.PostHashtagRepository;
import com.babyboy.social.service.PostHashtagService;
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
 * REST controller for managing {@link com.babyboy.social.domain.PostHashtag}.
 */
@RestController
@RequestMapping("/api")
public class PostHashtagResource {

    private final Logger log = LoggerFactory.getLogger(PostHashtagResource.class);

    private static final String ENTITY_NAME = "backendsocialPostHashtag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostHashtagService postHashtagService;

    private final PostHashtagRepository postHashtagRepository;

    public PostHashtagResource(PostHashtagService postHashtagService, PostHashtagRepository postHashtagRepository) {
        this.postHashtagService = postHashtagService;
        this.postHashtagRepository = postHashtagRepository;
    }

    /**
     * {@code POST  /post-hashtags} : Create a new postHashtag.
     *
     * @param postHashtag the postHashtag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postHashtag, or with status {@code 400 (Bad Request)} if the postHashtag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-hashtags")
    public ResponseEntity<PostHashtag> createPostHashtag(@Valid @RequestBody PostHashtag postHashtag) throws URISyntaxException {
        log.debug("REST request to save PostHashtag : {}", postHashtag);
        if (postHashtag.getId() != null) {
            throw new BadRequestAlertException("A new postHashtag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostHashtag result = postHashtagService.save(postHashtag);
        return ResponseEntity
            .created(new URI("/api/post-hashtags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-hashtags/:id} : Updates an existing postHashtag.
     *
     * @param id the id of the postHashtag to save.
     * @param postHashtag the postHashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postHashtag,
     * or with status {@code 400 (Bad Request)} if the postHashtag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postHashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-hashtags/{id}")
    public ResponseEntity<PostHashtag> updatePostHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostHashtag postHashtag
    ) throws URISyntaxException {
        log.debug("REST request to update PostHashtag : {}, {}", id, postHashtag);
        if (postHashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postHashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postHashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostHashtag result = postHashtagService.save(postHashtag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postHashtag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-hashtags/:id} : Partial updates given fields of an existing postHashtag, field will ignore if it is null
     *
     * @param id the id of the postHashtag to save.
     * @param postHashtag the postHashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postHashtag,
     * or with status {@code 400 (Bad Request)} if the postHashtag is not valid,
     * or with status {@code 404 (Not Found)} if the postHashtag is not found,
     * or with status {@code 500 (Internal Server Error)} if the postHashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/post-hashtags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostHashtag> partialUpdatePostHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostHashtag postHashtag
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostHashtag partially : {}, {}", id, postHashtag);
        if (postHashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postHashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postHashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostHashtag> result = postHashtagService.partialUpdate(postHashtag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postHashtag.getId().toString())
        );
    }

    /**
     * {@code GET  /post-hashtags} : get all the postHashtags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postHashtags in body.
     */
    @GetMapping("/post-hashtags")
    public List<PostHashtag> getAllPostHashtags() {
        log.debug("REST request to get all PostHashtags");
        return postHashtagService.findAll();
    }

    /**
     * {@code GET  /post-hashtags/:id} : get the "id" postHashtag.
     *
     * @param id the id of the postHashtag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postHashtag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-hashtags/{id}")
    public ResponseEntity<PostHashtag> getPostHashtag(@PathVariable Long id) {
        log.debug("REST request to get PostHashtag : {}", id);
        Optional<PostHashtag> postHashtag = postHashtagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postHashtag);
    }

    /**
     * {@code DELETE  /post-hashtags/:id} : delete the "id" postHashtag.
     *
     * @param id the id of the postHashtag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-hashtags/{id}")
    public ResponseEntity<Void> deletePostHashtag(@PathVariable Long id) {
        log.debug("REST request to delete PostHashtag : {}", id);
        postHashtagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
