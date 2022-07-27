package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.PostHashtag;
import com.babyboy.social.repository.PostHashtagRepository;
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
 * Integration tests for the {@link PostHashtagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostHashtagResourceIT {

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_HASHTAG_ID = 1L;
    private static final Long UPDATED_HASHTAG_ID = 2L;

    private static final String ENTITY_API_URL = "/api/post-hashtags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostHashtagMockMvc;

    private PostHashtag postHashtag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostHashtag createEntity(EntityManager em) {
        PostHashtag postHashtag = new PostHashtag().postId(DEFAULT_POST_ID).hashtagId(DEFAULT_HASHTAG_ID);
        return postHashtag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostHashtag createUpdatedEntity(EntityManager em) {
        PostHashtag postHashtag = new PostHashtag().postId(UPDATED_POST_ID).hashtagId(UPDATED_HASHTAG_ID);
        return postHashtag;
    }

    @BeforeEach
    public void initTest() {
        postHashtag = createEntity(em);
    }

    @Test
    @Transactional
    void createPostHashtag() throws Exception {
        int databaseSizeBeforeCreate = postHashtagRepository.findAll().size();
        // Create the PostHashtag
        restPostHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postHashtag)))
            .andExpect(status().isCreated());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeCreate + 1);
        PostHashtag testPostHashtag = postHashtagList.get(postHashtagList.size() - 1);
        assertThat(testPostHashtag.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPostHashtag.getHashtagId()).isEqualTo(DEFAULT_HASHTAG_ID);
    }

    @Test
    @Transactional
    void createPostHashtagWithExistingId() throws Exception {
        // Create the PostHashtag with an existing ID
        postHashtag.setId(1L);

        int databaseSizeBeforeCreate = postHashtagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postHashtag)))
            .andExpect(status().isBadRequest());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPostIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postHashtagRepository.findAll().size();
        // set the field null
        postHashtag.setPostId(null);

        // Create the PostHashtag, which fails.

        restPostHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postHashtag)))
            .andExpect(status().isBadRequest());

        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashtagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postHashtagRepository.findAll().size();
        // set the field null
        postHashtag.setHashtagId(null);

        // Create the PostHashtag, which fails.

        restPostHashtagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postHashtag)))
            .andExpect(status().isBadRequest());

        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostHashtags() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        // Get all the postHashtagList
        restPostHashtagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postHashtag.getId().intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].hashtagId").value(hasItem(DEFAULT_HASHTAG_ID.intValue())));
    }

    @Test
    @Transactional
    void getPostHashtag() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        // Get the postHashtag
        restPostHashtagMockMvc
            .perform(get(ENTITY_API_URL_ID, postHashtag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postHashtag.getId().intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.hashtagId").value(DEFAULT_HASHTAG_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostHashtag() throws Exception {
        // Get the postHashtag
        restPostHashtagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPostHashtag() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();

        // Update the postHashtag
        PostHashtag updatedPostHashtag = postHashtagRepository.findById(postHashtag.getId()).get();
        // Disconnect from session so that the updates on updatedPostHashtag are not directly saved in db
        em.detach(updatedPostHashtag);
        updatedPostHashtag.postId(UPDATED_POST_ID).hashtagId(UPDATED_HASHTAG_ID);

        restPostHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostHashtag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPostHashtag))
            )
            .andExpect(status().isOk());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
        PostHashtag testPostHashtag = postHashtagList.get(postHashtagList.size() - 1);
        assertThat(testPostHashtag.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostHashtag.getHashtagId()).isEqualTo(UPDATED_HASHTAG_ID);
    }

    @Test
    @Transactional
    void putNonExistingPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postHashtag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postHashtag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostHashtagWithPatch() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();

        // Update the postHashtag using partial update
        PostHashtag partialUpdatedPostHashtag = new PostHashtag();
        partialUpdatedPostHashtag.setId(postHashtag.getId());

        partialUpdatedPostHashtag.hashtagId(UPDATED_HASHTAG_ID);

        restPostHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostHashtag))
            )
            .andExpect(status().isOk());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
        PostHashtag testPostHashtag = postHashtagList.get(postHashtagList.size() - 1);
        assertThat(testPostHashtag.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPostHashtag.getHashtagId()).isEqualTo(UPDATED_HASHTAG_ID);
    }

    @Test
    @Transactional
    void fullUpdatePostHashtagWithPatch() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();

        // Update the postHashtag using partial update
        PostHashtag partialUpdatedPostHashtag = new PostHashtag();
        partialUpdatedPostHashtag.setId(postHashtag.getId());

        partialUpdatedPostHashtag.postId(UPDATED_POST_ID).hashtagId(UPDATED_HASHTAG_ID);

        restPostHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostHashtag))
            )
            .andExpect(status().isOk());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
        PostHashtag testPostHashtag = postHashtagList.get(postHashtagList.size() - 1);
        assertThat(testPostHashtag.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostHashtag.getHashtagId()).isEqualTo(UPDATED_HASHTAG_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postHashtag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postHashtag))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostHashtag() throws Exception {
        int databaseSizeBeforeUpdate = postHashtagRepository.findAll().size();
        postHashtag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostHashtagMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postHashtag))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostHashtag in the database
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostHashtag() throws Exception {
        // Initialize the database
        postHashtagRepository.saveAndFlush(postHashtag);

        int databaseSizeBeforeDelete = postHashtagRepository.findAll().size();

        // Delete the postHashtag
        restPostHashtagMockMvc
            .perform(delete(ENTITY_API_URL_ID, postHashtag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostHashtag> postHashtagList = postHashtagRepository.findAll();
        assertThat(postHashtagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
