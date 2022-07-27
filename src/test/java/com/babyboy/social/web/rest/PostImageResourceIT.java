package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.PostImage;
import com.babyboy.social.repository.PostImageRepository;
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
 * Integration tests for the {@link PostImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostImageResourceIT {

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/post-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostImageMockMvc;

    private PostImage postImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostImage createEntity(EntityManager em) {
        PostImage postImage = new PostImage().postId(DEFAULT_POST_ID).image(DEFAULT_IMAGE);
        return postImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostImage createUpdatedEntity(EntityManager em) {
        PostImage postImage = new PostImage().postId(UPDATED_POST_ID).image(UPDATED_IMAGE);
        return postImage;
    }

    @BeforeEach
    public void initTest() {
        postImage = createEntity(em);
    }

    @Test
    @Transactional
    void createPostImage() throws Exception {
        int databaseSizeBeforeCreate = postImageRepository.findAll().size();
        // Create the PostImage
        restPostImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postImage)))
            .andExpect(status().isCreated());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeCreate + 1);
        PostImage testPostImage = postImageList.get(postImageList.size() - 1);
        assertThat(testPostImage.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPostImage.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void createPostImageWithExistingId() throws Exception {
        // Create the PostImage with an existing ID
        postImage.setId(1L);

        int databaseSizeBeforeCreate = postImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postImage)))
            .andExpect(status().isBadRequest());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPostIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postImageRepository.findAll().size();
        // set the field null
        postImage.setPostId(null);

        // Create the PostImage, which fails.

        restPostImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postImage)))
            .andExpect(status().isBadRequest());

        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = postImageRepository.findAll().size();
        // set the field null
        postImage.setImage(null);

        // Create the PostImage, which fails.

        restPostImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postImage)))
            .andExpect(status().isBadRequest());

        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostImages() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        // Get all the postImageList
        restPostImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getPostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        // Get the postImage
        restPostImageMockMvc
            .perform(get(ENTITY_API_URL_ID, postImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postImage.getId().intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingPostImage() throws Exception {
        // Get the postImage
        restPostImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();

        // Update the postImage
        PostImage updatedPostImage = postImageRepository.findById(postImage.getId()).get();
        // Disconnect from session so that the updates on updatedPostImage are not directly saved in db
        em.detach(updatedPostImage);
        updatedPostImage.postId(UPDATED_POST_ID).image(UPDATED_IMAGE);

        restPostImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPostImage))
            )
            .andExpect(status().isOk());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
        PostImage testPostImage = postImageList.get(postImageList.size() - 1);
        assertThat(testPostImage.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostImage.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postImage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postImage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostImageWithPatch() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();

        // Update the postImage using partial update
        PostImage partialUpdatedPostImage = new PostImage();
        partialUpdatedPostImage.setId(postImage.getId());

        partialUpdatedPostImage.image(UPDATED_IMAGE);

        restPostImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostImage))
            )
            .andExpect(status().isOk());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
        PostImage testPostImage = postImageList.get(postImageList.size() - 1);
        assertThat(testPostImage.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testPostImage.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdatePostImageWithPatch() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();

        // Update the postImage using partial update
        PostImage partialUpdatedPostImage = new PostImage();
        partialUpdatedPostImage.setId(postImage.getId());

        partialUpdatedPostImage.postId(UPDATED_POST_ID).image(UPDATED_IMAGE);

        restPostImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostImage))
            )
            .andExpect(status().isOk());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
        PostImage testPostImage = postImageList.get(postImageList.size() - 1);
        assertThat(testPostImage.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testPostImage.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postImage))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostImage() throws Exception {
        int databaseSizeBeforeUpdate = postImageRepository.findAll().size();
        postImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postImage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostImage in the database
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostImage() throws Exception {
        // Initialize the database
        postImageRepository.saveAndFlush(postImage);

        int databaseSizeBeforeDelete = postImageRepository.findAll().size();

        // Delete the postImage
        restPostImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, postImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostImage> postImageList = postImageRepository.findAll();
        assertThat(postImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
