package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.UserCustomSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserCustomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserCustomResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final Role DEFAULT_ROLE = Role.STUDENT;
    private static final Role UPDATED_ROLE = Role.INSTRUCTOR;

    private static final AccountStatus DEFAULT_ACCOUNT_STATUS = AccountStatus.ACTIVATED;
    private static final AccountStatus UPDATED_ACCOUNT_STATUS = AccountStatus.DEACTIVATED;

    private static final String DEFAULT_PHONE_NUMBER_1 = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER_1 = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMVER_2 = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMVER_2 = "BBBBBBBBBB";

    private static final Sex DEFAULT_SEX = Sex.MALE;
    private static final Sex UPDATED_SEX = Sex.FEMALE;

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_TELEGRAM_USER_CUSTOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_USER_CUSTOM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TELEGRAM_USER_CUSTOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TELEGRAM_USER_CUSTOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIOGRAPHY = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-customs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-customs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCustomRepository userCustomRepository;

    @Mock
    private UserCustomRepository userCustomRepositoryMock;

    @Autowired
    private UserCustomSearchRepository userCustomSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCustomMockMvc;

    private UserCustom userCustom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCustom createEntity(EntityManager em) {
        UserCustom userCustom = new UserCustom()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .code(DEFAULT_CODE)
            .role(DEFAULT_ROLE)
            .accountStatus(DEFAULT_ACCOUNT_STATUS)
            .phoneNumber1(DEFAULT_PHONE_NUMBER_1)
            .phoneNumver2(DEFAULT_PHONE_NUMVER_2)
            .sex(DEFAULT_SEX)
            .birthdate(DEFAULT_BIRTHDATE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .address(DEFAULT_ADDRESS)
            .facebook(DEFAULT_FACEBOOK)
            .telegramUserCustomId(DEFAULT_TELEGRAM_USER_CUSTOM_ID)
            .telegramUserCustomName(DEFAULT_TELEGRAM_USER_CUSTOM_NAME)
            .biography(DEFAULT_BIOGRAPHY)
            .bankAccountDetails(DEFAULT_BANK_ACCOUNT_DETAILS);
        return userCustom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCustom createUpdatedEntity(EntityManager em) {
        UserCustom userCustom = new UserCustom()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .code(UPDATED_CODE)
            .role(UPDATED_ROLE)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumver2(UPDATED_PHONE_NUMVER_2)
            .sex(UPDATED_SEX)
            .birthdate(UPDATED_BIRTHDATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .facebook(UPDATED_FACEBOOK)
            .telegramUserCustomId(UPDATED_TELEGRAM_USER_CUSTOM_ID)
            .telegramUserCustomName(UPDATED_TELEGRAM_USER_CUSTOM_NAME)
            .biography(UPDATED_BIOGRAPHY)
            .bankAccountDetails(UPDATED_BANK_ACCOUNT_DETAILS);
        return userCustom;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        userCustomSearchRepository.deleteAll();
        assertThat(userCustomSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        userCustom = createEntity(em);
    }

    @Test
    @Transactional
    void createUserCustom() throws Exception {
        int databaseSizeBeforeCreate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // Create the UserCustom
        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isCreated());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserCustom.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserCustom.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUserCustom.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testUserCustom.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
        assertThat(testUserCustom.getPhoneNumber1()).isEqualTo(DEFAULT_PHONE_NUMBER_1);
        assertThat(testUserCustom.getPhoneNumver2()).isEqualTo(DEFAULT_PHONE_NUMVER_2);
        assertThat(testUserCustom.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testUserCustom.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testUserCustom.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testUserCustom.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testUserCustom.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserCustom.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testUserCustom.getTelegramUserCustomId()).isEqualTo(DEFAULT_TELEGRAM_USER_CUSTOM_ID);
        assertThat(testUserCustom.getTelegramUserCustomName()).isEqualTo(DEFAULT_TELEGRAM_USER_CUSTOM_NAME);
        assertThat(testUserCustom.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
        assertThat(testUserCustom.getBankAccountDetails()).isEqualTo(DEFAULT_BANK_ACCOUNT_DETAILS);
    }

    @Test
    @Transactional
    void createUserCustomWithExistingId() throws Exception {
        // Create the UserCustom with an existing ID
        userCustom.setId(1L);

        int databaseSizeBeforeCreate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setFirstName(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setLastName(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setRole(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAccountStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setAccountStatus(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPhoneNumber1IsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setPhoneNumber1(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setSex(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        // set the field null
        userCustom.setBirthdate(null);

        // Create the UserCustom, which fails.

        restUserCustomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isBadRequest());

        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserCustoms() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        // Get all the userCustomList
        restUserCustomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCustom.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber1").value(hasItem(DEFAULT_PHONE_NUMBER_1)))
            .andExpect(jsonPath("$.[*].phoneNumver2").value(hasItem(DEFAULT_PHONE_NUMVER_2)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].telegramUserCustomId").value(hasItem(DEFAULT_TELEGRAM_USER_CUSTOM_ID)))
            .andExpect(jsonPath("$.[*].telegramUserCustomName").value(hasItem(DEFAULT_TELEGRAM_USER_CUSTOM_NAME)))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
            .andExpect(jsonPath("$.[*].bankAccountDetails").value(hasItem(DEFAULT_BANK_ACCOUNT_DETAILS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCustomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userCustomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCustomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userCustomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserCustomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userCustomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserCustomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userCustomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        // Get the userCustom
        restUserCustomMockMvc
            .perform(get(ENTITY_API_URL_ID, userCustom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCustom.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS.toString()))
            .andExpect(jsonPath("$.phoneNumber1").value(DEFAULT_PHONE_NUMBER_1))
            .andExpect(jsonPath("$.phoneNumver2").value(DEFAULT_PHONE_NUMVER_2))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.telegramUserCustomId").value(DEFAULT_TELEGRAM_USER_CUSTOM_ID))
            .andExpect(jsonPath("$.telegramUserCustomName").value(DEFAULT_TELEGRAM_USER_CUSTOM_NAME))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()))
            .andExpect(jsonPath("$.bankAccountDetails").value(DEFAULT_BANK_ACCOUNT_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserCustom() throws Exception {
        // Get the userCustom
        restUserCustomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        userCustomSearchRepository.save(userCustom);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());

        // Update the userCustom
        UserCustom updatedUserCustom = userCustomRepository.findById(userCustom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserCustom are not directly saved in db
        em.detach(updatedUserCustom);
        updatedUserCustom
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .code(UPDATED_CODE)
            .role(UPDATED_ROLE)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumver2(UPDATED_PHONE_NUMVER_2)
            .sex(UPDATED_SEX)
            .birthdate(UPDATED_BIRTHDATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .facebook(UPDATED_FACEBOOK)
            .telegramUserCustomId(UPDATED_TELEGRAM_USER_CUSTOM_ID)
            .telegramUserCustomName(UPDATED_TELEGRAM_USER_CUSTOM_NAME)
            .biography(UPDATED_BIOGRAPHY)
            .bankAccountDetails(UPDATED_BANK_ACCOUNT_DETAILS);

        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserCustom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserCustom))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserCustom.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCustom.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserCustom.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testUserCustom.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testUserCustom.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
        assertThat(testUserCustom.getPhoneNumver2()).isEqualTo(UPDATED_PHONE_NUMVER_2);
        assertThat(testUserCustom.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserCustom.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testUserCustom.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testUserCustom.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testUserCustom.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserCustom.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testUserCustom.getTelegramUserCustomId()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_ID);
        assertThat(testUserCustom.getTelegramUserCustomName()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_NAME);
        assertThat(testUserCustom.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserCustom.getBankAccountDetails()).isEqualTo(UPDATED_BANK_ACCOUNT_DETAILS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserCustom> userCustomSearchList = IterableUtils.toList(userCustomSearchRepository.findAll());
                UserCustom testUserCustomSearch = userCustomSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserCustomSearch.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
                assertThat(testUserCustomSearch.getLastName()).isEqualTo(UPDATED_LAST_NAME);
                assertThat(testUserCustomSearch.getCode()).isEqualTo(UPDATED_CODE);
                assertThat(testUserCustomSearch.getRole()).isEqualTo(UPDATED_ROLE);
                assertThat(testUserCustomSearch.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
                assertThat(testUserCustomSearch.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
                assertThat(testUserCustomSearch.getPhoneNumver2()).isEqualTo(UPDATED_PHONE_NUMVER_2);
                assertThat(testUserCustomSearch.getSex()).isEqualTo(UPDATED_SEX);
                assertThat(testUserCustomSearch.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
                assertThat(testUserCustomSearch.getPhoto()).isEqualTo(UPDATED_PHOTO);
                assertThat(testUserCustomSearch.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
                assertThat(testUserCustomSearch.getAddress()).isEqualTo(UPDATED_ADDRESS);
                assertThat(testUserCustomSearch.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
                assertThat(testUserCustomSearch.getTelegramUserCustomId()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_ID);
                assertThat(testUserCustomSearch.getTelegramUserCustomName()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_NAME);
                assertThat(testUserCustomSearch.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
                assertThat(testUserCustomSearch.getBankAccountDetails()).isEqualTo(UPDATED_BANK_ACCOUNT_DETAILS);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCustom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCustom))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCustom))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCustom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserCustomWithPatch() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();

        // Update the userCustom using partial update
        UserCustom partialUpdatedUserCustom = new UserCustom();
        partialUpdatedUserCustom.setId(userCustom.getId());

        partialUpdatedUserCustom
            .lastName(UPDATED_LAST_NAME)
            .code(UPDATED_CODE)
            .role(UPDATED_ROLE)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .sex(UPDATED_SEX)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .facebook(UPDATED_FACEBOOK)
            .telegramUserCustomName(UPDATED_TELEGRAM_USER_CUSTOM_NAME)
            .biography(UPDATED_BIOGRAPHY)
            .bankAccountDetails(UPDATED_BANK_ACCOUNT_DETAILS);

        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCustom))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserCustom.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCustom.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserCustom.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testUserCustom.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
        assertThat(testUserCustom.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
        assertThat(testUserCustom.getPhoneNumver2()).isEqualTo(DEFAULT_PHONE_NUMVER_2);
        assertThat(testUserCustom.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserCustom.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testUserCustom.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testUserCustom.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testUserCustom.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserCustom.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testUserCustom.getTelegramUserCustomId()).isEqualTo(DEFAULT_TELEGRAM_USER_CUSTOM_ID);
        assertThat(testUserCustom.getTelegramUserCustomName()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_NAME);
        assertThat(testUserCustom.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserCustom.getBankAccountDetails()).isEqualTo(UPDATED_BANK_ACCOUNT_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateUserCustomWithPatch() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);

        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();

        // Update the userCustom using partial update
        UserCustom partialUpdatedUserCustom = new UserCustom();
        partialUpdatedUserCustom.setId(userCustom.getId());

        partialUpdatedUserCustom
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .code(UPDATED_CODE)
            .role(UPDATED_ROLE)
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumver2(UPDATED_PHONE_NUMVER_2)
            .sex(UPDATED_SEX)
            .birthdate(UPDATED_BIRTHDATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .address(UPDATED_ADDRESS)
            .facebook(UPDATED_FACEBOOK)
            .telegramUserCustomId(UPDATED_TELEGRAM_USER_CUSTOM_ID)
            .telegramUserCustomName(UPDATED_TELEGRAM_USER_CUSTOM_NAME)
            .biography(UPDATED_BIOGRAPHY)
            .bankAccountDetails(UPDATED_BANK_ACCOUNT_DETAILS);

        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCustom))
            )
            .andExpect(status().isOk());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        UserCustom testUserCustom = userCustomList.get(userCustomList.size() - 1);
        assertThat(testUserCustom.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserCustom.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserCustom.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUserCustom.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testUserCustom.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testUserCustom.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
        assertThat(testUserCustom.getPhoneNumver2()).isEqualTo(UPDATED_PHONE_NUMVER_2);
        assertThat(testUserCustom.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testUserCustom.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testUserCustom.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testUserCustom.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testUserCustom.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserCustom.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testUserCustom.getTelegramUserCustomId()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_ID);
        assertThat(testUserCustom.getTelegramUserCustomName()).isEqualTo(UPDATED_TELEGRAM_USER_CUSTOM_NAME);
        assertThat(testUserCustom.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserCustom.getBankAccountDetails()).isEqualTo(UPDATED_BANK_ACCOUNT_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userCustom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCustom))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCustom))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserCustom() throws Exception {
        int databaseSizeBeforeUpdate = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        userCustom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCustomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userCustom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCustom in the database
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserCustom() throws Exception {
        // Initialize the database
        userCustomRepository.saveAndFlush(userCustom);
        userCustomRepository.save(userCustom);
        userCustomSearchRepository.save(userCustom);

        int databaseSizeBeforeDelete = userCustomRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userCustom
        restUserCustomMockMvc
            .perform(delete(ENTITY_API_URL_ID, userCustom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserCustom> userCustomList = userCustomRepository.findAll();
        assertThat(userCustomList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userCustomSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserCustom() throws Exception {
        // Initialize the database
        userCustom = userCustomRepository.saveAndFlush(userCustom);
        userCustomSearchRepository.save(userCustom);

        // Search the userCustom
        restUserCustomMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userCustom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCustom.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber1").value(hasItem(DEFAULT_PHONE_NUMBER_1)))
            .andExpect(jsonPath("$.[*].phoneNumver2").value(hasItem(DEFAULT_PHONE_NUMVER_2)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].telegramUserCustomId").value(hasItem(DEFAULT_TELEGRAM_USER_CUSTOM_ID)))
            .andExpect(jsonPath("$.[*].telegramUserCustomName").value(hasItem(DEFAULT_TELEGRAM_USER_CUSTOM_NAME)))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
            .andExpect(jsonPath("$.[*].bankAccountDetails").value(hasItem(DEFAULT_BANK_ACCOUNT_DETAILS.toString())));
    }
}
