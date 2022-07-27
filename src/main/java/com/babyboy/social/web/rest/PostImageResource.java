package com.babyboy.social.web.rest;

import com.babyboy.social.domain.PostImage;
import com.babyboy.social.repository.PostImageRepository;
import com.babyboy.social.service.PostImageService;
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
 * REST controller for managing {@link com.babyboy.social.domain.PostImage}.
 */
@RestController
@RequestMapping("/api")
public class PostImageResource {

    private final Logger log = LoggerFactory.getLogger(PostImageResource.class);

    private static final String ENTITY_NAME = "backendsocialPostImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostImageService postImageService;

    private final PostImageRepository postImageRepository;

    public PostImageResource(PostImageService postImageService, PostImageRepository postImageRepository) {
        this.postImageService = postImageService;
        this.postImageRepository = postImageRepository;
    }

    /**
     * {@code POST  /post-images} : Create a new postImage.
     *
     * @param postImage the postImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postImage, or with status {@code 400 (Bad Request)} if the postImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-images")
    public ResponseEntity<PostImage> createPostImage(@Valid @RequestBody PostImage postImage) throws URISyntaxException {
        log.debug("REST request to save PostImage : {}", postImage);
        if (postImage.getId() != null) {
            throw new BadRequestAlertException("A new postImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostImage result = postImageService.save(postImage);
        return ResponseEntity
            .created(new URI("/api/post-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-images/:id} : Updates an existing postImage.
     *
     * @param id the id of the postImage to save.
     * @param postImage the postImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postImage,
     * or with status {@code 400 (Bad Request)} if the postImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-images/{id}")
    public ResponseEntity<PostImage> updatePostImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostImage postImage
    ) throws URISyntaxException {
        log.debug("REST request to update PostImage : {}, {}", id, postImage);
        if (postImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostImage result = postImageService.save(postImage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postImage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-images/:id} : Partial updates given fields of an existing postImage, field will ignore if it is null
     *
     * @param id the id of the postImage to save.
     * @param postImage the postImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postImage,
     * or with status {@code 400 (Bad Request)} if the postImage is not valid,
     * or with status {@code 404 (Not Found)} if the postImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the postImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/post-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostImage> partialUpdatePostImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostImage postImage
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostImage partially : {}, {}", id, postImage);
        if (postImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostImage> result = postImageService.partialUpdate(postImage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postImage.getId().toString())
        );
    }

    /**
     * {@code GET  /post-images} : get all the postImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postImages in body.
     */
    @GetMapping("/post-images")
    public List<PostImage> getAllPostImages() {
        log.debug("REST request to get all PostImages");
        return postImageService.findAll();
    }

    /**
     * {@code GET  /post-images/:id} : get the "id" postImage.
     *
     * @param id the id of the postImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-images/{id}")
    public ResponseEntity<PostImage> getPostImage(@PathVariable Long id) {
        log.debug("REST request to get PostImage : {}", id);
        Optional<PostImage> postImage = postImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postImage);
    }

    /**
     * {@code DELETE  /post-images/:id} : delete the "id" postImage.
     *
     * @param id the id of the postImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-images/{id}")
    public ResponseEntity<Void> deletePostImage(@PathVariable Long id) {
        log.debug("REST request to delete PostImage : {}", id);
        postImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
