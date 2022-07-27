package com.babyboy.social.web.rest;

import com.babyboy.social.domain.Hashtag;
import com.babyboy.social.repository.HashtagRepository;
import com.babyboy.social.service.HashtagService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.babyboy.social.domain.Hashtag}.
 */
@RestController
@RequestMapping("/api")
public class HashtagResource {

    private final Logger log = LoggerFactory.getLogger(HashtagResource.class);

    private static final String ENTITY_NAME = "backendsocialHashtag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashtagService hashtagService;

    private final HashtagRepository hashtagRepository;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public HashtagResource(HashtagService hashtagService, HashtagRepository hashtagRepository) {
        this.hashtagService = hashtagService;
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * {@code POST  /hashtags} : Create a new hashtag.
     *
     * @param hashtag the hashtag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashtag, or with status {@code 400 (Bad Request)} if the hashtag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hashtags")
    public ResponseEntity<Hashtag> createHashtag(@Valid @RequestBody Hashtag hashtag) throws URISyntaxException {
        log.debug("REST request to save Hashtag : {}", hashtag);
        if (hashtag.getId() != null) {
            throw new BadRequestAlertException("A new hashtag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hashtag result = hashtagService.save(hashtag);

        // TODO: Handle create Kafka Topic
        //        kafkaAdmin.createOrModifyTopics(
        //            TopicBuilder.name(hashtag.getCode())
        //            .partitions(1)
        //            .replicas(1)
        //            .build()
        //        );

        return ResponseEntity
            .created(new URI("/api/hashtags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hashtags/:id} : Updates an existing hashtag.
     *
     * @param id the id of the hashtag to save.
     * @param hashtag the hashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashtag,
     * or with status {@code 400 (Bad Request)} if the hashtag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hashtags/{id}")
    public ResponseEntity<Hashtag> updateHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Hashtag hashtag
    ) throws URISyntaxException {
        log.debug("REST request to update Hashtag : {}, {}", id, hashtag);
        if (hashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hashtag result = hashtagService.save(hashtag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hashtag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hashtags/:id} : Partial updates given fields of an existing hashtag, field will ignore if it is null
     *
     * @param id the id of the hashtag to save.
     * @param hashtag the hashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashtag,
     * or with status {@code 400 (Bad Request)} if the hashtag is not valid,
     * or with status {@code 404 (Not Found)} if the hashtag is not found,
     * or with status {@code 500 (Internal Server Error)} if the hashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hashtags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hashtag> partialUpdateHashtag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Hashtag hashtag
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hashtag partially : {}, {}", id, hashtag);
        if (hashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashtag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashtagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hashtag> result = hashtagService.partialUpdate(hashtag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hashtag.getId().toString())
        );
    }

    /**
     * {@code GET  /hashtags} : get all the hashtags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashtags in body.
     */
    @GetMapping("/hashtags")
    public List<Hashtag> getAllHashtags() {
        log.debug("REST request to get all Hashtags");
        return hashtagService.findAll();
    }

    /**
     * {@code GET  /hashtags/:id} : get the "id" hashtag.
     *
     * @param id the id of the hashtag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashtag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hashtags/{id}")
    public ResponseEntity<Hashtag> getHashtag(@PathVariable Long id) {
        log.debug("REST request to get Hashtag : {}", id);
        Optional<Hashtag> hashtag = hashtagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashtag);
    }

    /**
     * {@code DELETE  /hashtags/:id} : delete the "id" hashtag.
     *
     * @param id the id of the hashtag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hashtags/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable Long id) {
        log.debug("REST request to delete Hashtag : {}", id);
        hashtagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
