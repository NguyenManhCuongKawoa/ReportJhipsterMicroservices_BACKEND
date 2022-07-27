package com.babyboy.social.web.rest;

import com.babyboy.social.domain.Post;
import com.babyboy.social.dto.PostDto;
import com.babyboy.social.dto.response.PostResponse;
import com.babyboy.social.repository.PostRepository;
import com.babyboy.social.service.PostService;
import com.babyboy.social.service.kafka.ProducerKafkaService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.babyboy.social.domain.Post}.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "backendsocialPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostService postService;

    private final PostRepository postRepository;

    @Autowired
    private ProducerKafkaService producerKafkaServie;

    public PostResource(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    //    /**
    //     * {@code POST  /posts} : Create a new post.
    //     *
    //     * @param post the post to create.
    //     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new post, or with status {@code 400 (Bad Request)} if the post has already an ID.
    //     * @throws URISyntaxException if the Location URI syntax is incorrect.
    //     */
    //    @PostMapping("/posts")
    //    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) throws URISyntaxException {
    //        log.debug("REST request to save Post : {}", post);
    //        if (post.getId() != null) {
    //            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //        Post result = postService.save(post);
    //        return ResponseEntity
    //            .created(new URI("/api/posts/" + result.getId()))
    //            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
    //            .body(result);
    //    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostDto postDto) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDto);
        if (postDto.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }

        PostResponse result = postService.savePost(postDto);
        postDto.setCreatedAt(result.getCreatedAt());
        postDto.setUpdatedAt(result.getUpdatedAt());
        postDto.setId(result.getId());
        postService.savePostSearch(postDto);

        // TODO: Send notify for people who have the same hashtag
        //        String currentUsername = SecurityUtils.getCurrentUserLogin().get();
        //        for(Hashtag h : result.getHashtags()) {
        //            String message = currentUsername + " just post a new post having a hashtag " + h.getName() ;
        //            producerKafkaServie.publish(h.getCode(), message);
        //        }

        return ResponseEntity
            .created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing post.
     *
     * @param id the id of the post to save.
     * @param post the post to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated post,
     * or with status {@code 400 (Bad Request)} if the post is not valid,
     * or with status {@code 500 (Internal Server Error)} if the post couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Post post)
        throws URISyntaxException {
        log.debug("REST request to update Post : {}, {}", id, post);
        if (post.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, post.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Post result = postService.save(post);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, post.getId().toString()))
            .body(result);
    }

    @PutMapping("/posts/change-mode/{id}/{mode}")
    public ResponseEntity<?> updateModePost(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "mode") final Integer mode
    ) throws URISyntaxException {
        log.debug("REST request to update Mode Post {} with mode: {}", id, mode);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Post result = postService.ChangeModePost(id, mode);
        if (result == null) {
            return ResponseEntity
                .badRequest()
                .headers(
                    HeaderUtil.createFailureAlert(
                        applicationName,
                        false,
                        ENTITY_NAME,
                        "Mode",
                        "Some thing was wrong when update mode of post!!"
                    )
                )
                .body("Some thing was wrong!!");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing post, field will ignore if it is null
     *
     * @param id the id of the post to save.
     * @param post the post to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated post,
     * or with status {@code 400 (Bad Request)} if the post is not valid,
     * or with status {@code 404 (Not Found)} if the post is not found,
     * or with status {@code 500 (Internal Server Error)} if the post couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/posts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Post> partialUpdatePost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Post post
    ) throws URISyntaxException {
        log.debug("REST request to partial update Post partially : {}, {}", id, post);
        if (post.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, post.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Post> result = postService.partialUpdate(post);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, post.getId().toString())
        );
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<Post> page = postService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the post to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the post, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<Post> post = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(post);
    }

    @GetMapping("/_search/posts")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Posts for query {}", query);
        Page<PostDto> page = postService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the post to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);
        postService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
