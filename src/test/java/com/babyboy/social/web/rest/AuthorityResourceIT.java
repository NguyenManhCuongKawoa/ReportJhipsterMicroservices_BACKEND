//package com.babyboy.social.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.babyboy.social.IntegrationTest;
//import com.babyboy.social.domain.Authority;
//import com.babyboy.social.repository.AuthorityRepository;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link AuthorityResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class AuthorityResourceIT {
//
//    private static final String DEFAULT_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_NAME = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/j-authorities";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restJAuthorityMockMvc;
//
//    private Authority jAuthority;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Authority createEntity(EntityManager em) {
//        Authority jAuthority = new Authority().name(DEFAULT_NAME);
//        return jAuthority;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static Authority createUpdatedEntity(EntityManager em) {
//        Authority jAuthority = new Authority().name(UPDATED_NAME);
//        return jAuthority;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        jAuthority = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createJAuthority() throws Exception {
//        int databaseSizeBeforeCreate = authorityRepository.findAll().size();
//        // Create the JAuthority
//        restJAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jAuthority)))
//            .andExpect(status().isCreated());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeCreate + 1);
//        Authority testJAuthority = jAuthorityList.get(jAuthorityList.size() - 1);
//        assertThat(testJAuthority.getName()).isEqualTo(DEFAULT_NAME);
//    }
//
//    @Test
//    @Transactional
//    void createJAuthorityWithExistingId() throws Exception {
//        // Create the JAuthority with an existing ID
//        jAuthority.setId(1L);
//
//        int databaseSizeBeforeCreate = authorityRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restJAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jAuthority)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = authorityRepository.findAll().size();
//        // set the field null
//        jAuthority.setName(null);
//
//        // Create the JAuthority, which fails.
//
//        restJAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jAuthority)))
//            .andExpect(status().isBadRequest());
//
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllJAuthorities() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        // Get all the jAuthorityList
//        restJAuthorityMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(jAuthority.getId().intValue())))
//            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
//    }
//
//    @Test
//    @Transactional
//    void getJAuthority() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        // Get the jAuthority
//        restJAuthorityMockMvc
//            .perform(get(ENTITY_API_URL_ID, jAuthority.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(jAuthority.getId().intValue()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingJAuthority() throws Exception {
//        // Get the jAuthority
//        restJAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewJAuthority() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//
//        // Update the jAuthority
//        Authority updatedJAuthority = authorityRepository.findById(jAuthority.getId()).get();
//        // Disconnect from session so that the updates on updatedJAuthority are not directly saved in db
//        em.detach(updatedJAuthority);
//        updatedJAuthority.name(UPDATED_NAME);
//
//        restJAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedJAuthority.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedJAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        Authority testJAuthority = jAuthorityList.get(jAuthorityList.size() - 1);
//        assertThat(testJAuthority.getName()).isEqualTo(UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, jAuthority.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(jAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(jAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jAuthority)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateJAuthorityWithPatch() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//
//        // Update the jAuthority using partial update
//        Authority partialUpdatedJAuthority = new Authority();
//        partialUpdatedJAuthority.setId(jAuthority.getId());
//
//        partialUpdatedJAuthority.name(UPDATED_NAME);
//
//        restJAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedJAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        Authority testJAuthority = jAuthorityList.get(jAuthorityList.size() - 1);
//        assertThat(testJAuthority.getName()).isEqualTo(UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateJAuthorityWithPatch() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//
//        // Update the jAuthority using partial update
//        Authority partialUpdatedJAuthority = new Authority();
//        partialUpdatedJAuthority.setId(jAuthority.getId());
//
//        partialUpdatedJAuthority.name(UPDATED_NAME);
//
//        restJAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedJAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        Authority testJAuthority = jAuthorityList.get(jAuthorityList.size() - 1);
//        assertThat(testJAuthority.getName()).isEqualTo(UPDATED_NAME);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, jAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(jAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(jAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamJAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();
//        jAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restJAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jAuthority))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the JAuthority in the database
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteJAuthority() throws Exception {
//        // Initialize the database
//        authorityRepository.saveAndFlush(jAuthority);
//
//        int databaseSizeBeforeDelete = authorityRepository.findAll().size();
//
//        // Delete the jAuthority
//        restJAuthorityMockMvc
//            .perform(delete(ENTITY_API_URL_ID, jAuthority.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<Authority> jAuthorityList = authorityRepository.findAll();
//        assertThat(jAuthorityList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
