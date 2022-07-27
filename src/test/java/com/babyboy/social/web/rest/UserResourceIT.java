package com.babyboy.social.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.babyboy.social.IntegrationTest;
import com.babyboy.social.domain.User;
import com.babyboy.social.repository.UserRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link UserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_RESET_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESET_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RESET_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESET_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/j-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJUserMockMvc;

    private User user;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User()
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .activated(DEFAULT_ACTIVATED)
            .langKey(DEFAULT_LANG_KEY)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .resetKey(DEFAULT_RESET_KEY)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .resetDate(DEFAULT_RESET_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return user;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity(EntityManager em) {
        User user = new User()
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .resetDate(UPDATED_RESET_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return user;
    }

    @BeforeEach
    public void initTest() {
        user = createEntity(em);
    }

    @Test
    @Transactional
    void createJUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();
        // Create the JUser
        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isCreated());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUser.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANG_KEY);
        assertThat(testUser.getActivationKey()).isEqualTo(DEFAULT_ACTIVATION_KEY);
        assertThat(testUser.getResetKey()).isEqualTo(DEFAULT_RESET_KEY);
        assertThat(testUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUser.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testUser.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUser.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createJUserWithExistingId() throws Exception {
        // Create the JUser with an existing ID
        user.setId(1L);

        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setLogin(null);

        // Create the JUser, which fails.

        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setPassword(null);

        // Create the JUser, which fails.

        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setEmail(null);

        // Create the JUser, which fails.

        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setActivated(null);

        // Create the JUser, which fails.

        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setCreatedBy(null);

        // Create the JUser, which fails.

        restJUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the jUserList
        restJUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANG_KEY)))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].resetKey").value(hasItem(DEFAULT_RESET_KEY)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(DEFAULT_RESET_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getJUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get the jUser
        restJUserMockMvc
            .perform(get(ENTITY_API_URL_ID, user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANG_KEY))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY))
            .andExpect(jsonPath("$.resetKey").value(DEFAULT_RESET_KEY))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.resetDate").value(DEFAULT_RESET_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJUser() throws Exception {
        // Get the jUser
        restJUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the jUser
        User updatedUser = userRepository.findById(user.getId()).get();
        // Disconnect from session so that the updates on updatedJUser are not directly saved in db
        em.detach(updatedUser);
        updatedUser
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .resetDate(UPDATED_RESET_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restJUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUser))
            )
            .andExpect(status().isOk());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testUser.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUser.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJUserWithPatch() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the jUser using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .login(UPDATED_LOGIN)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restJUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testUser.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUser.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);
        assertThat(testUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateJUserWithPatch() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the jUser using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .activated(UPDATED_ACTIVATED)
            .langKey(UPDATED_LANG_KEY)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .resetKey(UPDATED_RESET_KEY)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .resetDate(UPDATED_RESET_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restJUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUser))
            )
            .andExpect(status().isOk());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUser.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANG_KEY);
        assertThat(testUser.getActivationKey()).isEqualTo(UPDATED_ACTIVATION_KEY);
        assertThat(testUser.getResetKey()).isEqualTo(UPDATED_RESET_KEY);
        assertThat(testUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUser.getResetDate()).isEqualTo(UPDATED_RESET_DATE);
        assertThat(testUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, user.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(user))
            )
            .andExpect(status().isBadRequest());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJUser() throws Exception {
        int databaseSizeBeforeUpdate = userRepository.findAll().size();
        user.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JUser in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the jUser
        restJUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, user.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
