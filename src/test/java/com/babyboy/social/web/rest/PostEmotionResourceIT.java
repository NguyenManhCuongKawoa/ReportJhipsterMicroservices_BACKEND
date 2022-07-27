package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.PostEmotion;
import com.babyboy.social.repository.PostEmotionRepository;
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
 * Integration tests for the {@link PostEmotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostEmotionResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_EMOTION_ID = 1L;
    private static final Long UPDATED_EMOTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/post-emotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostEmotionRepository postEmotionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostEmotionMockMvc;

    private PostEmotion postEmotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostEmotion createEntity(EntityManager em) {
        PostEmotion postEmotion = new PostEmotion().userId(DEFAULT_USER_ID).postId(DEFAULT_POST_ID).emotionId(DEFAULT_EMOTION_ID);
        return postEmotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostEmotion createUpdatedEntity(EntityManager em) {
        PostEmotion postEmotion = new PostEmotion().userId(UPDATED_USER_ID).postId(UPDATED_POST_ID).emotionId(UPDATED_EMOTION_ID);
        return postEmotion;
    }

    @BeforeEach
    public void initTest() {
        postEmotion = createEntity(em);
    }

    @Test
    @Transactional
    void createPostEmotion() throws Exception {
        int databaseSizeBeforeCreate = postEmotionRepository.findAll().size();
        // Create the PostEmotion
        restPostEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isCreated());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeCreate + 1);
        PostEmotion testPostEmotion = postEmotionList.get(postEmotionList.size() - 1);
        assertThat(testPostEmotion.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPostEmotion.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPostEmotion.getEmotionId()).isEqualTo(DEFAULT_EMOTION_ID);
    }

    @Test
    @Transactional
    void createPostEmotionWithExistingId() throws Exception {
        // Create the PostEmotion with an existing ID
        postEmotion.setId(1L);

        int databaseSizeBeforeCreate = postEmotionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isBadRequest());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postEmotionRepository.findAll().size();
        // set the field null
        postEmotion.setUserId(null);

        // Create the PostEmotion, which fails.

        restPostEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isBadRequest());

        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postEmotionRepository.findAll().size();
        // set the field null
        postEmotion.setPostId(null);

        // Create the PostEmotion, which fails.

        restPostEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isBadRequest());

        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmotionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postEmotionRepository.findAll().size();
        // set the field null
        postEmotion.setEmotionId(null);

        // Create the PostEmotion, which fails.

        restPostEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isBadRequest());

        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostEmotions() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        // Get all the postEmotionList
        restPostEmotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postEmotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].emotionId").value(hasItem(DEFAULT_EMOTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getPostEmotion() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        // Get the postEmotion
        restPostEmotionMockMvc
            .perform(get(ENTITY_API_URL_ID, postEmotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postEmotion.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.emotionId").value(DEFAULT_EMOTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostEmotion() throws Exception {
        // Get the postEmotion
        restPostEmotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPostEmotion() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();

        // Update the postEmotion
        PostEmotion updatedPostEmotion = postEmotionRepository.findById(postEmotion.getId()).get();
        // Disconnect from session so that the updates on updatedPostEmotion are not directly saved in db
        em.detach(updatedPostEmotion);
        updatedPostEmotion.userId(UPDATED_USER_ID).postId(UPDATED_POST_ID).emotionId(UPDATED_EMOTION_ID);

        restPostEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostEmotion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPostEmotion))
            )
            .andExpect(status().isOk());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
        PostEmotion testPostEmotion = postEmotionList.get(postEmotionList.size() - 1);
        assertThat(testPostEmotion.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPostEmotion.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostEmotion.getEmotionId()).isEqualTo(UPDATED_EMOTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postEmotion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postEmotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postEmotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postEmotion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostEmotionWithPatch() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();

        // Update the postEmotion using partial update
        PostEmotion partialUpdatedPostEmotion = new PostEmotion();
        partialUpdatedPostEmotion.setId(postEmotion.getId());

        partialUpdatedPostEmotion.userId(UPDATED_USER_ID).postId(UPDATED_POST_ID);

        restPostEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostEmotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostEmotion))
            )
            .andExpect(status().isOk());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
        PostEmotion testPostEmotion = postEmotionList.get(postEmotionList.size() - 1);
        assertThat(testPostEmotion.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPostEmotion.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostEmotion.getEmotionId()).isEqualTo(DEFAULT_EMOTION_ID);
    }

    @Test
    @Transactional
    void fullUpdatePostEmotionWithPatch() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();

        // Update the postEmotion using partial update
        PostEmotion partialUpdatedPostEmotion = new PostEmotion();
        partialUpdatedPostEmotion.setId(postEmotion.getId());

        partialUpdatedPostEmotion.userId(UPDATED_USER_ID).postId(UPDATED_POST_ID).emotionId(UPDATED_EMOTION_ID);

        restPostEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostEmotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostEmotion))
            )
            .andExpect(status().isOk());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
        PostEmotion testPostEmotion = postEmotionList.get(postEmotionList.size() - 1);
        assertThat(testPostEmotion.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPostEmotion.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostEmotion.getEmotionId()).isEqualTo(UPDATED_EMOTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postEmotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postEmotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postEmotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostEmotion() throws Exception {
        int databaseSizeBeforeUpdate = postEmotionRepository.findAll().size();
        postEmotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postEmotion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostEmotion in the database
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostEmotion() throws Exception {
        // Initialize the database
        postEmotionRepository.saveAndFlush(postEmotion);

        int databaseSizeBeforeDelete = postEmotionRepository.findAll().size();

        // Delete the postEmotion
        restPostEmotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, postEmotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostEmotion> postEmotionList = postEmotionRepository.findAll();
        assertThat(postEmotionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
