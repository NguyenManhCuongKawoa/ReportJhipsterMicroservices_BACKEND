package com.babyboy.social.web.rest;

import com.babyboy.social.domain.PostEmotion;
import com.babyboy.social.repository.PostEmotionRepository;
import com.babyboy.social.service.PostEmotionService;
import com.babyboy.social.service.PostService;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.babyboy.social.domain.PostEmotion}.
 */
@RestController
@RequestMapping("/api")
public class PostEmotionResource {

    private final Logger log = LoggerFactory.getLogger(PostEmotionResource.class);

    private static final String ENTITY_NAME = "backendsocialPostEmotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostEmotionService postEmotionService;

    private final PostEmotionRepository postEmotionRepository;

    @Autowired
    private PostService postService;

    public PostEmotionResource(PostEmotionService postEmotionService, PostEmotionRepository postEmotionRepository) {
        this.postEmotionService = postEmotionService;
        this.postEmotionRepository = postEmotionRepository;
    }

    /**
     * {@code POST  /post-emotions} : Create a new postEmotion.
     *
     * @param postEmotion the postEmotion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postEmotion, or with status {@code 400 (Bad Request)} if the postEmotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-emotions")
    public ResponseEntity<PostEmotion> createPostEmotion(@Valid @RequestBody PostEmotion postEmotion) throws URISyntaxException {
        log.debug("REST request to save PostEmotion : {}", postEmotion);
        if (postEmotion.getId() != null) {
            throw new BadRequestAlertException("A new postEmotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostEmotion postEmotionOld = postEmotionRepository
            .findByUserIdAndPostId(postEmotion.getUserId(), postEmotion.getPostId())
            .orElse(null);
        if (postEmotionOld == null) {
            postEmotionOld = postEmotionService.save(postEmotion);

            // TODO: INCREMENT TOTAL EMOTION OF POST
            postService.incrementTotalEmotionBy(postEmotion.getPostId(), 1);
        } else {
            postEmotionOld.setEmotionId(postEmotion.getEmotionId());
            postEmotionRepository.save(postEmotionOld);
        }

        return ResponseEntity
            .created(new URI("/api/post-emotions/" + postEmotionOld.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, postEmotionOld.getId().toString()))
            .body(postEmotionOld);
    }

    /**
     * {@code PUT  /post-emotions/:id} : Updates an existing postEmotion.
     *
     * @param id the id of the postEmotion to save.
     * @param postEmotion the postEmotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postEmotion,
     * or with status {@code 400 (Bad Request)} if the postEmotion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postEmotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-emotions/{id}")
    public ResponseEntity<PostEmotion> updatePostEmotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostEmotion postEmotion
    ) throws URISyntaxException {
        log.debug("REST request to update PostEmotion : {}, {}", id, postEmotion);
        if (postEmotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postEmotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postEmotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostEmotion result = postEmotionService.save(postEmotion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postEmotion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-emotions/:id} : Partial updates given fields of an existing postEmotion, field will ignore if it is null
     *
     * @param id the id of the postEmotion to save.
     * @param postEmotion the postEmotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postEmotion,
     * or with status {@code 400 (Bad Request)} if the postEmotion is not valid,
     * or with status {@code 404 (Not Found)} if the postEmotion is not found,
     * or with status {@code 500 (Internal Server Error)} if the postEmotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/post-emotions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostEmotion> partialUpdatePostEmotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostEmotion postEmotion
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostEmotion partially : {}, {}", id, postEmotion);
        if (postEmotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postEmotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postEmotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostEmotion> result = postEmotionService.partialUpdate(postEmotion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postEmotion.getId().toString())
        );
    }

    /**
     * {@code GET  /post-emotions} : get all the postEmotions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postEmotions in body.
     */
    @GetMapping("/post-emotions")
    public List<PostEmotion> getAllPostEmotions() {
        log.debug("REST request to get all PostEmotions");
        return postEmotionService.findAll();
    }

    /**
     * {@code GET  /post-emotions/:id} : get the "id" postEmotion.
     *
     * @param id the id of the postEmotion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postEmotion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-emotions/{id}")
    public ResponseEntity<PostEmotion> getPostEmotion(@PathVariable Long id) {
        log.debug("REST request to get PostEmotion : {}", id);
        Optional<PostEmotion> postEmotion = postEmotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postEmotion);
    }

    /**
     * {@code DELETE  /post-emotions/:id} : delete the "id" postEmotion.
     *
     * @param id the id of the postEmotion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-emotions/{id}")
    public ResponseEntity<Void> deletePostEmotion(@PathVariable Long id) {
        log.debug("REST request to delete PostEmotion : {}", id);
        postEmotionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
