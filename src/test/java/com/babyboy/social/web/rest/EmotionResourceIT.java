package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.Emotion;
import com.babyboy.social.repository.EmotionRepository;
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
 * Integration tests for the {@link EmotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmotionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmotionRepository emotionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmotionMockMvc;

    private Emotion emotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emotion createEntity(EntityManager em) {
        Emotion emotion = new Emotion().name(DEFAULT_NAME).icon(DEFAULT_ICON);
        return emotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emotion createUpdatedEntity(EntityManager em) {
        Emotion emotion = new Emotion().name(UPDATED_NAME).icon(UPDATED_ICON);
        return emotion;
    }

    @BeforeEach
    public void initTest() {
        emotion = createEntity(em);
    }

    @Test
    @Transactional
    void createEmotion() throws Exception {
        int databaseSizeBeforeCreate = emotionRepository.findAll().size();
        // Create the Emotion
        restEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isCreated());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeCreate + 1);
        Emotion testEmotion = emotionList.get(emotionList.size() - 1);
        assertThat(testEmotion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmotion.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    void createEmotionWithExistingId() throws Exception {
        // Create the Emotion with an existing ID
        emotion.setId(1L);

        int databaseSizeBeforeCreate = emotionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isBadRequest());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = emotionRepository.findAll().size();
        // set the field null
        emotion.setName(null);

        // Create the Emotion, which fails.

        restEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isBadRequest());

        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = emotionRepository.findAll().size();
        // set the field null
        emotion.setIcon(null);

        // Create the Emotion, which fails.

        restEmotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isBadRequest());

        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmotions() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        // Get all the emotionList
        restEmotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }

    @Test
    @Transactional
    void getEmotion() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        // Get the emotion
        restEmotionMockMvc
            .perform(get(ENTITY_API_URL_ID, emotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emotion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }

    @Test
    @Transactional
    void getNonExistingEmotion() throws Exception {
        // Get the emotion
        restEmotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmotion() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();

        // Update the emotion
        Emotion updatedEmotion = emotionRepository.findById(emotion.getId()).get();
        // Disconnect from session so that the updates on updatedEmotion are not directly saved in db
        em.detach(updatedEmotion);
        updatedEmotion.name(UPDATED_NAME).icon(UPDATED_ICON);

        restEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmotion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmotion))
            )
            .andExpect(status().isOk());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
        Emotion testEmotion = emotionList.get(emotionList.size() - 1);
        assertThat(testEmotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmotion.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void putNonExistingEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emotion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmotionWithPatch() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();

        // Update the emotion using partial update
        Emotion partialUpdatedEmotion = new Emotion();
        partialUpdatedEmotion.setId(emotion.getId());

        partialUpdatedEmotion.name(UPDATED_NAME);

        restEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmotion))
            )
            .andExpect(status().isOk());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
        Emotion testEmotion = emotionList.get(emotionList.size() - 1);
        assertThat(testEmotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmotion.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    void fullUpdateEmotionWithPatch() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();

        // Update the emotion using partial update
        Emotion partialUpdatedEmotion = new Emotion();
        partialUpdatedEmotion.setId(emotion.getId());

        partialUpdatedEmotion.name(UPDATED_NAME).icon(UPDATED_ICON);

        restEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmotion))
            )
            .andExpect(status().isOk());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
        Emotion testEmotion = emotionList.get(emotionList.size() - 1);
        assertThat(testEmotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmotion.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void patchNonExistingEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emotion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmotion() throws Exception {
        int databaseSizeBeforeUpdate = emotionRepository.findAll().size();
        emotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmotionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emotion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emotion in the database
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmotion() throws Exception {
        // Initialize the database
        emotionRepository.saveAndFlush(emotion);

        int databaseSizeBeforeDelete = emotionRepository.findAll().size();

        // Delete the emotion
        restEmotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, emotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emotion> emotionList = emotionRepository.findAll();
        assertThat(emotionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
