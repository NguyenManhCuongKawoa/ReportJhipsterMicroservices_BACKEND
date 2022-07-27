package com.babyboy.social.web.rest;

import com.babyboy.social.domain.Authority;
import com.babyboy.social.repository.AuthorityRepository;
import com.babyboy.social.service.AuthorityService;
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
 * REST controller for managing {@link Authority}.
 */
@RestController
@RequestMapping("/api")
public class AuthorityResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "cmssocialJAuthority";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityService authorityService;

    private final AuthorityRepository authorityRepository;

    public AuthorityResource(AuthorityService authorityService, AuthorityRepository authorityRepository) {
        this.authorityService = authorityService;
        this.authorityRepository = authorityRepository;
    }

    /**
     * {@code POST  /j-authorities} : Create a new jAuthority.
     *
     * @param authority the jAuthority to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jAuthority, or with status {@code 400 (Bad Request)} if the jAuthority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/j-authorities")
    public ResponseEntity<Authority> createJAuthority(@Valid @RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST request to save JAuthority : {}", authority);
        if (authority.getName() != null) {
            throw new BadRequestAlertException("A new jAuthority cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Authority result = authorityService.save(authority);
        return ResponseEntity
            .created(new URI("/api/j-authorities/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /j-authorities/:id} : Updates an existing jAuthority.
     *
     * @param name the id of the jAuthority to save.
     * @param authority the jAuthority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jAuthority,
     * or with status {@code 400 (Bad Request)} if the jAuthority is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jAuthority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/j-authorities/{name}")
    public ResponseEntity<Authority> updateJAuthority(
        @PathVariable(value = "name", required = false) final String name,
        @Valid @RequestBody Authority authority
    ) throws URISyntaxException {
        log.debug("REST request to update JAuthority : {}, {}", name, authority);
        if (authority.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, authority.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorityRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Authority result = authorityService.save(authority);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, authority.getName()))
            .body(result);
    }

    /**
     * {@code PATCH  /j-authorities/:id} : Partial updates given fields of an existing jAuthority, field will ignore if it is null
     *
     * @param name the id of the jAuthority to save.
     * @param authority the jAuthority to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jAuthority,
     * or with status {@code 400 (Bad Request)} if the jAuthority is not valid,
     * or with status {@code 404 (Not Found)} if the jAuthority is not found,
     * or with status {@code 500 (Internal Server Error)} if the jAuthority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/j-authorities/{name}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Authority> partialUpdateJAuthority(
        @PathVariable(value = "name", required = false) final String name,
        @NotNull @RequestBody Authority authority
    ) throws URISyntaxException {
        log.debug("REST request to partial update JAuthority partially : {}, {}", name, authority);
        if (authority.getName() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, authority.getName())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authorityRepository.existsById(name)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Authority> result = authorityService.partialUpdate(authority);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, authority.getName())
        );
    }

    /**
     * {@code GET  /j-authorities} : get all the jAuthorities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jAuthorities in body.
     */
    @GetMapping("/j-authorities")
    public List<Authority> getAllJAuthorities() {
        log.debug("REST request to get all JAuthorities");
        return authorityService.findAll();
    }

    /**
     * {@code GET  /j-authorities/:id} : get the "id" jAuthority.
     *
     * @param name the id of the jAuthority to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jAuthority, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/j-authorities/{name}")
    public ResponseEntity<Authority> getJAuthority(@PathVariable String name) {
        log.debug("REST request to get JAuthority : {}", name);
        Optional<Authority> jAuthority = authorityService.findOne(name);
        return ResponseUtil.wrapOrNotFound(jAuthority);
    }

    /**
     * {@code DELETE  /j-authorities/:id} : delete the "id" jAuthority.
     *
     * @param name the id of the jAuthority to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/j-authorities/{name}")
    public ResponseEntity<Void> deleteJAuthority(@PathVariable String name) {
        log.debug("REST request to delete JAuthority : {}", name);
        authorityService.delete(name);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, name)).build();
    }
}
