//package com.babyboy.social.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.babyboy.social.IntegrationTest;
//
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
// * Integration tests for the {@link UserAuthorityResource} REST controller.
// */
//@IntegrationTest
//@AutoConfigureMockMvc
//@WithMockUser
//class UserAuthorityResourceIT {
//
//    private static final Long DEFAULT_USER_ID = 1L;
//    private static final Long UPDATED_USER_ID = 2L;
//
//    private static final String DEFAULT_AUTORITY_NAME = "AAAAAAAAAA";
//    private static final String UPDATED_AUTORITY_NAME = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/user-authorities";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private UserAuthorityRepository userAuthorityRepository;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restUserAuthorityMockMvc;
//
//    private UserAuthority userAuthority;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserAuthority createEntity(EntityManager em) {
//        UserAuthority userAuthority = new UserAuthority().userId(DEFAULT_USER_ID).autorityName(DEFAULT_AUTORITY_NAME);
//        return userAuthority;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static UserAuthority createUpdatedEntity(EntityManager em) {
//        UserAuthority userAuthority = new UserAuthority().userId(UPDATED_USER_ID).autorityName(UPDATED_AUTORITY_NAME);
//        return userAuthority;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        userAuthority = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createUserAuthority() throws Exception {
//        int databaseSizeBeforeCreate = userAuthorityRepository.findAll().size();
//        // Create the UserAuthority
//        restUserAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuthority)))
//            .andExpect(status().isCreated());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeCreate + 1);
//        UserAuthority testUserAuthority = userAuthorityList.get(userAuthorityList.size() - 1);
//        assertThat(testUserAuthority.getUserId()).isEqualTo(DEFAULT_USER_ID);
//        assertThat(testUserAuthority.getAutorityName()).isEqualTo(DEFAULT_AUTORITY_NAME);
//    }
//
//    @Test
//    @Transactional
//    void createUserAuthorityWithExistingId() throws Exception {
//        // Create the UserAuthority with an existing ID
//        userAuthority.setId(1L);
//
//        int databaseSizeBeforeCreate = userAuthorityRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restUserAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuthority)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void checkUserIdIsRequired() throws Exception {
//        int databaseSizeBeforeTest = userAuthorityRepository.findAll().size();
//        // set the field null
//        userAuthority.setUserId(null);
//
//        // Create the UserAuthority, which fails.
//
//        restUserAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuthority)))
//            .andExpect(status().isBadRequest());
//
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void checkAutorityNameIsRequired() throws Exception {
//        int databaseSizeBeforeTest = userAuthorityRepository.findAll().size();
//        // set the field null
//        userAuthority.setAutorityName(null);
//
//        // Create the UserAuthority, which fails.
//
//        restUserAuthorityMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuthority)))
//            .andExpect(status().isBadRequest());
//
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeTest);
//    }
//
//    @Test
//    @Transactional
//    void getAllUserAuthorities() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        // Get all the userAuthorityList
//        restUserAuthorityMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(userAuthority.getId().intValue())))
//            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
//            .andExpect(jsonPath("$.[*].autorityName").value(hasItem(DEFAULT_AUTORITY_NAME)));
//    }
//
//    @Test
//    @Transactional
//    void getUserAuthority() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        // Get the userAuthority
//        restUserAuthorityMockMvc
//            .perform(get(ENTITY_API_URL_ID, userAuthority.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(userAuthority.getId().intValue()))
//            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
//            .andExpect(jsonPath("$.autorityName").value(DEFAULT_AUTORITY_NAME));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingUserAuthority() throws Exception {
//        // Get the userAuthority
//        restUserAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewUserAuthority() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//
//        // Update the userAuthority
//        UserAuthority updatedUserAuthority = userAuthorityRepository.findById(userAuthority.getId()).get();
//        // Disconnect from session so that the updates on updatedUserAuthority are not directly saved in db
//        em.detach(updatedUserAuthority);
//        updatedUserAuthority.userId(UPDATED_USER_ID).autorityName(UPDATED_AUTORITY_NAME);
//
//        restUserAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedUserAuthority.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedUserAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        UserAuthority testUserAuthority = userAuthorityList.get(userAuthorityList.size() - 1);
//        assertThat(testUserAuthority.getUserId()).isEqualTo(UPDATED_USER_ID);
//        assertThat(testUserAuthority.getAutorityName()).isEqualTo(UPDATED_AUTORITY_NAME);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, userAuthority.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(userAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(userAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userAuthority)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateUserAuthorityWithPatch() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//
//        // Update the userAuthority using partial update
//        UserAuthority partialUpdatedUserAuthority = new UserAuthority();
//        partialUpdatedUserAuthority.setId(userAuthority.getId());
//
//        partialUpdatedUserAuthority.userId(UPDATED_USER_ID).autorityName(UPDATED_AUTORITY_NAME);
//
//        restUserAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        UserAuthority testUserAuthority = userAuthorityList.get(userAuthorityList.size() - 1);
//        assertThat(testUserAuthority.getUserId()).isEqualTo(UPDATED_USER_ID);
//        assertThat(testUserAuthority.getAutorityName()).isEqualTo(UPDATED_AUTORITY_NAME);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateUserAuthorityWithPatch() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//
//        // Update the userAuthority using partial update
//        UserAuthority partialUpdatedUserAuthority = new UserAuthority();
//        partialUpdatedUserAuthority.setId(userAuthority.getId());
//
//        partialUpdatedUserAuthority.userId(UPDATED_USER_ID).autorityName(UPDATED_AUTORITY_NAME);
//
//        restUserAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthority))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//        UserAuthority testUserAuthority = userAuthorityList.get(userAuthorityList.size() - 1);
//        assertThat(testUserAuthority.getUserId()).isEqualTo(UPDATED_USER_ID);
//        assertThat(testUserAuthority.getAutorityName()).isEqualTo(UPDATED_AUTORITY_NAME);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, userAuthority.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(userAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(userAuthority))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamUserAuthority() throws Exception {
//        int databaseSizeBeforeUpdate = userAuthorityRepository.findAll().size();
//        userAuthority.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restUserAuthorityMockMvc
//            .perform(
//                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userAuthority))
//            )
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the UserAuthority in the database
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteUserAuthority() throws Exception {
//        // Initialize the database
//        userAuthorityRepository.saveAndFlush(userAuthority);
//
//        int databaseSizeBeforeDelete = userAuthorityRepository.findAll().size();
//
//        // Delete the userAuthority
//        restUserAuthorityMockMvc
//            .perform(delete(ENTITY_API_URL_ID, userAuthority.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<UserAuthority> userAuthorityList = userAuthorityRepository.findAll();
//        assertThat(userAuthorityList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
