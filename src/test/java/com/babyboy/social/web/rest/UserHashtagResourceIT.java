package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.UserHashtag;
import com.babyboy.social.repository.UserHashtagRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserHashtagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserHashtagResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_HASHTAG_ID = 1L;
    private static final Long UPDATED_HASHTAG_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-hashtags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserHashtagRepository userHashtagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserHashtagMockMvc;

    private UserHashtag userHashtag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHashtag createEntity(EntityManager em) {
        UserHashtag userHashtag = new UserHashtag().userId(DEFAULT_USER_ID).hashtagId(DEFAULT_HASHTAG_ID);
        return userHashtag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserHashtag createUpdatedEntity(EntityManager em) {
        UserHashtag userHashtag = new UserHashtag().userId(UPDATED_USER_ID).hashtagId(UPDATED_HASHTAG_ID);
        return userHashtag;
    }

    @BeforeEach
    public void initTest() {
        userHashtag = createEntity(em);
    }

    @Test
    @Transactional
    void createUserHashtag() throws Exception {
        int databaseSizeBeforeCreate = userHashtagRepository.findAll().size();
        // Create the UserHashtag
        restUserHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHashtag)))
            .andExpect(status().isCreated());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeCreate + 1);
        UserHashtag testUserHashtag = userHashtagList.get(userHashtagList.size() - 1);
        assertThat(testUserHashtag.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserHashtag.getHashtagId()).isEqualTo(DEFAULT_HASHTAG_ID);
    }

    @Test
    @Transactional
    void createUserHashtagWithExistingId() throws Exception {
        // Create the UserHashtag with an existing ID
        userHashtag.setId(1L);

        int databaseSizeBeforeCreate = userHashtagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHashtag)))
            .andExpect(status().isBadRequest());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userHashtagRepository.findAll().size();
        // set the field null
        userHashtag.setUserId(null);

        // Create the UserHashtag, which fails.

        restUserHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHashtag)))
            .andExpect(status().isBadRequest());

        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashtagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userHashtagRepository.findAll().size();
        // set the field null
        userHashtag.setHashtagId(null);

        // Create the UserHashtag, which fails.

        restUserHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHashtag)))
            .andExpect(status().isBadRequest());

        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserHashtags() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        // Get all the userHashtagList
        restUserHashtagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userHashtag.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hashtagId").value(hasItem(DEFAULT_HASHTAG_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserHashtag() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        // Get the userHashtag
        restUserHashtagMockMvc
            .perform(get(ENTITY_API_URL_ID, userHashtag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userHashtag.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.hashtagId").value(DEFAULT_HASHTAG_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserHashtag() throws Exception {
        // Get the userHashtag
        restUserHashtagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserHashtag() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();

        // Update the userHashtag
        UserHashtag updatedUserHashtag = userHashtagRepository.findById(userHashtag.getId()).get();
        // Disconnect from session so that the updates on updatedUserHashtag are not directly saved in db
        em.detach(updatedUserHashtag);
        updatedUserHashtag.userId(UPDATED_USER_ID).hashtagId(UPDATED_HASHTAG_ID);

        restUserHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserHashtag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserHashtag))
            )
            .andExpect(status().isOk());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
        UserHashtag testUserHashtag = userHashtagList.get(userHashtagList.size() - 1);
        assertThat(testUserHashtag.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserHashtag.getHashtagId()).isEqualTo(UPDATED_HASHTAG_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userHashtag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userHashtag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserHashtagWithPatch() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();

        // Update the userHashtag using partial update
        UserHashtag partialUpdatedUserHashtag = new UserHashtag();
        partialUpdatedUserHashtag.setId(userHashtag.getId());

        restUserHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserHashtag))
            )
            .andExpect(status().isOk());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
        UserHashtag testUserHashtag = userHashtagList.get(userHashtagList.size() - 1);
        assertThat(testUserHashtag.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserHashtag.getHashtagId()).isEqualTo(DEFAULT_HASHTAG_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserHashtagWithPatch() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();

        // Update the userHashtag using partial update
        UserHashtag partialUpdatedUserHashtag = new UserHashtag();
        partialUpdatedUserHashtag.setId(userHashtag.getId());

        partialUpdatedUserHashtag.userId(UPDATED_USER_ID).hashtagId(UPDATED_HASHTAG_ID);

        restUserHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserHashtag))
            )
            .andExpect(status().isOk());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
        UserHashtag testUserHashtag = userHashtagList.get(userHashtagList.size() - 1);
        assertThat(testUserHashtag.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserHashtag.getHashtagId()).isEqualTo(UPDATED_HASHTAG_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserHashtag() throws Exception {
        int databaseSizeBeforeUpdate = userHashtagRepository.findAll().size();
        userHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userHashtag))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserHashtag in the database
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserHashtag() throws Exception {
        // Initialize the database
        userHashtagRepository.saveAndFlush(userHashtag);

        int databaseSizeBeforeDelete = userHashtagRepository.findAll().size();

        // Delete the userHashtag
        restUserHashtagMockMvc
            .perform(delete(ENTITY_API_URL_ID, userHashtag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserHashtag> userHashtagList = userHashtagRepository.findAll();
        assertThat(userHashtagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
