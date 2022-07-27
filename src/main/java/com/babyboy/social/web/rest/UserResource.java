package com.babyboy.social.web.rest;

import com.babyboy.social.config.Constants;
import com.babyboy.social.domain.User;
import com.babyboy.social.domain.UserReport;
import com.babyboy.social.repository.UserRepository;
import com.babyboy.social.service.UserService;
import com.babyboy.social.service.kafka.ProducerKafkaService;
import com.babyboy.social.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "backendsocialJUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    private ProducerKafkaService producerKafkaService;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /users} : Create a new jUser.
     *
     * @param user the jUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jUser, or with status {@code 400 (Bad Request)} if the jUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users")
    public ResponseEntity<User> createJUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save JUser : {}", user);
        if (user.getId() != null) {
            throw new BadRequestAlertException("A new jUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User result = userService.save(user);
        return ResponseEntity
            .created(new URI("/api/j-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /users/:id} : Updates an existing jUser.
     *
     * @param id the id of the jUser to save.
     * @param user the jUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jUser,
     * or with status {@code 400 (Bad Request)} if the jUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateJUser(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody User user)
        throws URISyntaxException {
        log.debug("REST request to update JUser : {}, {}", id, user);
        if (user.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, user.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        User result = userService.save(user);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, user.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing jUser, field will ignore if it is null
     *
     * @param id the id of the jUser to save.
     * @param user the jUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jUser,
     * or with status {@code 400 (Bad Request)} if the jUser is not valid,
     * or with status {@code 404 (Not Found)} if the jUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the jUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<User> partialUpdateJUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody User user
    ) throws URISyntaxException {
        log.debug("REST request to partial update JUser partially : {}, {}", id, user);
        if (user.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, user.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<User> result = userService.partialUpdate(user);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, user.getId().toString())
        );
    }

    /**
     * {@code GET  /j-users} : get all the jUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jUsers in body.
     */
    @GetMapping("/users")
    public List<User> getAllJUsers() {
        log.debug("REST request to get all JUsers");
        return userService.findAll();
    }

    /**
     * {@code GET  /j-users/:id} : get the "id" jUser.
     *
     * @param id the id of the jUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getJUser(@PathVariable Long id) {
        log.debug("REST request to get JUser : {}", id);
        Optional<User> jUser = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jUser);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" jUser.
     *
     * @param id the id of the jUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteJUser(@PathVariable Long id) {
        log.debug("REST request to delete JUser : {}", id);
        userService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("users/report")
    ResponseEntity<?> userReport(@RequestBody UserReport userReport) {
        log.info("User Report Info : {}", userReport);

        // TODO: Send message to topic "report_user"
        try {
            producerKafkaService.publish(Constants.TOPIC_USER_REPORT, new ObjectMapper().writeValueAsString(userReport));

            return ResponseEntity.ok().body(userReport);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error when report!!!");
        }
    }
}
