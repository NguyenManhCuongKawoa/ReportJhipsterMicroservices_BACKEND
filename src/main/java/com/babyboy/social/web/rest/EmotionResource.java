package com.babyboy.social.web.rest;

import com.babyboy.social.domain.Emotion;
import com.babyboy.social.repository.EmotionRepository;
import com.babyboy.social.service.EmotionService;
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
 * REST controller for managing {@link com.babyboy.social.domain.Emotion}.
 */
@RestController
@RequestMapping("/api")
public class EmotionResource {

    private final Logger log = LoggerFactory.getLogger(EmotionResource.class);

    private static final String ENTITY_NAME = "backendsocialEmotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmotionService emotionService;

    private final EmotionRepository emotionRepository;

    public EmotionResource(EmotionService emotionService, EmotionRepository emotionRepository) {
        this.emotionService = emotionService;
        this.emotionRepository = emotionRepository;
    }

    /**
     * {@code POST  /emotions} : Create a new emotion.
     *
     * @param emotion the emotion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emotion, or with status {@code 400 (Bad Request)} if the emotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emotions")
    public ResponseEntity<Emotion> createEmotion(@Valid @RequestBody Emotion emotion) throws URISyntaxException {
        log.debug("REST request to save Emotion : {}", emotion);
        if (emotion.getId() != null) {
            throw new BadRequestAlertException("A new emotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Emotion result = emotionService.save(emotion);
        return ResponseEntity
            .created(new URI("/api/emotions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emotions/:id} : Updates an existing emotion.
     *
     * @param id the id of the emotion to save.
     * @param emotion the emotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emotion,
     * or with status {@code 400 (Bad Request)} if the emotion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emotions/{id}")
    public ResponseEntity<Emotion> updateEmotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Emotion emotion
    ) throws URISyntaxException {
        log.debug("REST request to update Emotion : {}, {}", id, emotion);
        if (emotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Emotion result = emotionService.save(emotion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emotion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emotions/:id} : Partial updates given fields of an existing emotion, field will ignore if it is null
     *
     * @param id the id of the emotion to save.
     * @param emotion the emotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emotion,
     * or with status {@code 400 (Bad Request)} if the emotion is not valid,
     * or with status {@code 404 (Not Found)} if the emotion is not found,
     * or with status {@code 500 (Internal Server Error)} if the emotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/emotions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Emotion> partialUpdateEmotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Emotion emotion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Emotion partially : {}, {}", id, emotion);
        if (emotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Emotion> result = emotionService.partialUpdate(emotion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emotion.getId().toString())
        );
    }

    /**
     * {@code GET  /emotions} : get all the emotions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emotions in body.
     */
    @GetMapping("/emotions")
    public List<Emotion> getAllEmotions() {
        log.debug("REST request to get all Emotions");
        return emotionService.findAll();
    }

    /**
     * {@code GET  /emotions/:id} : get the "id" emotion.
     *
     * @param id the id of the emotion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emotion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emotions/{id}")
    public ResponseEntity<Emotion> getEmotion(@PathVariable Long id) {
        log.debug("REST request to get Emotion : {}", id);
        Optional<Emotion> emotion = emotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emotion);
    }

    /**
     * {@code DELETE  /emotions/:id} : delete the "id" emotion.
     *
     * @param id the id of the emotion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emotions/{id}")
    public ResponseEntity<Void> deleteEmotion(@PathVariable Long id) {
        log.debug("REST request to delete Emotion : {}", id);
        emotionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
