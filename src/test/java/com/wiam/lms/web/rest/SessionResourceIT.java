package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.enumeration.SessionJoinMode;
import com.wiam.lms.domain.enumeration.SessionMode;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.repository.SessionRepository;
import com.wiam.lms.repository.search.SessionSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SessionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SessionResourceIT {

    private static final SessionMode DEFAULT_SESSION_MODE = SessionMode.ONLINE;
    private static final SessionMode UPDATED_SESSION_MODE = SessionMode.ONSITE;

    private static final SessionType DEFAULT_SESSION_TYPE = SessionType.HALAQA;
    private static final SessionType UPDATED_SESSION_TYPE = SessionType.LECTURE;

    private static final SessionJoinMode DEFAULT_SESSION_JOIN_MODE = SessionJoinMode.DIRECT;
    private static final SessionJoinMode UPDATED_SESSION_JOIN_MODE = SessionJoinMode.AUTHORIZED;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIODE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODE_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_SESSION_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SESSION_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SESSION_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SESSION_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_SESSION_SIZE = 0;
    private static final Integer UPDATED_SESSION_SIZE = 1;

    private static final TargetedGender DEFAULT_TARGETED_GENDER = TargetedGender.MEN;
    private static final TargetedGender UPDATED_TARGETED_GENDER = TargetedGender.WOMEN;

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_MONDAY = false;
    private static final Boolean UPDATED_MONDAY = true;

    private static final Boolean DEFAULT_TUESDAY = false;
    private static final Boolean UPDATED_TUESDAY = true;

    private static final Boolean DEFAULT_WEDNESDAY = false;
    private static final Boolean UPDATED_WEDNESDAY = true;

    private static final Boolean DEFAULT_THURSDAY = false;
    private static final Boolean UPDATED_THURSDAY = true;

    private static final Boolean DEFAULT_FRIDAY = false;
    private static final Boolean UPDATED_FRIDAY = true;

    private static final Boolean DEFAULT_SATURDAY = false;
    private static final Boolean UPDATED_SATURDAY = true;

    private static final Boolean DEFAULT_SUNDAY = false;
    private static final Boolean UPDATED_SUNDAY = true;

    private static final Boolean DEFAULT_IS_PERIODIC = false;
    private static final Boolean UPDATED_IS_PERIODIC = true;

    private static final Boolean DEFAULT_IS_MINOR_ALLOWED = false;
    private static final Boolean UPDATED_IS_MINOR_ALLOWED = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/sessions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionRepository sessionRepository;

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Autowired
    private SessionSearchRepository sessionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionMockMvc;

    private Session session;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createEntity(EntityManager em) {
        Session session = new Session()
            .sessionMode(DEFAULT_SESSION_MODE)
            .sessionType(DEFAULT_SESSION_TYPE)
            .sessionJoinMode(DEFAULT_SESSION_JOIN_MODE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .periodStartDate(DEFAULT_PERIOD_START_DATE)
            .periodeEndDate(DEFAULT_PERIODE_END_DATE)
            .sessionSize(DEFAULT_SESSION_SIZE)
            .targetedGender(DEFAULT_TARGETED_GENDER)
            .price(DEFAULT_PRICE)
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .monday(DEFAULT_MONDAY)
            .tuesday(DEFAULT_TUESDAY)
            .wednesday(DEFAULT_WEDNESDAY)
            .thursday(DEFAULT_THURSDAY)
            .friday(DEFAULT_FRIDAY)
            .saturday(DEFAULT_SATURDAY)
            .sunday(DEFAULT_SUNDAY)
            .isPeriodic(DEFAULT_IS_PERIODIC)
            .isMinorAllowed(DEFAULT_IS_MINOR_ALLOWED)
            .isActive(DEFAULT_IS_ACTIVE);
        return session;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Session createUpdatedEntity(EntityManager em) {
        Session session = new Session()
            .sessionMode(UPDATED_SESSION_MODE)
            .sessionType(UPDATED_SESSION_TYPE)
            .sessionJoinMode(UPDATED_SESSION_JOIN_MODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodeEndDate(UPDATED_PERIODE_END_DATE)
            .sessionSize(UPDATED_SESSION_SIZE)
            .targetedGender(UPDATED_TARGETED_GENDER)
            .price(UPDATED_PRICE)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .monday(UPDATED_MONDAY)
            .tuesday(UPDATED_TUESDAY)
            .wednesday(UPDATED_WEDNESDAY)
            .thursday(UPDATED_THURSDAY)
            .friday(UPDATED_FRIDAY)
            .saturday(UPDATED_SATURDAY)
            .sunday(UPDATED_SUNDAY)
            .isPeriodic(UPDATED_IS_PERIODIC)
            .isMinorAllowed(UPDATED_IS_MINOR_ALLOWED)
            .isActive(UPDATED_IS_ACTIVE);
        return session;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        sessionSearchRepository.deleteAll();
        assertThat(sessionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        session = createEntity(em);
    }

    @Test
    @Transactional
    void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // Create the Session
        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getSessionMode()).isEqualTo(DEFAULT_SESSION_MODE);
        assertThat(testSession.getSessionType()).isEqualTo(DEFAULT_SESSION_TYPE);
        assertThat(testSession.getSessionJoinMode()).isEqualTo(DEFAULT_SESSION_JOIN_MODE);
        assertThat(testSession.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSession.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testSession.getPeriodeEndDate()).isEqualTo(DEFAULT_PERIODE_END_DATE);
        assertThat(testSession.getSessionSize()).isEqualTo(DEFAULT_SESSION_SIZE);
        assertThat(testSession.getTargetedGender()).isEqualTo(DEFAULT_TARGETED_GENDER);
        assertThat(testSession.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSession.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSession.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSession.getMonday()).isEqualTo(DEFAULT_MONDAY);
        assertThat(testSession.getTuesday()).isEqualTo(DEFAULT_TUESDAY);
        assertThat(testSession.getWednesday()).isEqualTo(DEFAULT_WEDNESDAY);
        assertThat(testSession.getThursday()).isEqualTo(DEFAULT_THURSDAY);
        assertThat(testSession.getFriday()).isEqualTo(DEFAULT_FRIDAY);
        assertThat(testSession.getSaturday()).isEqualTo(DEFAULT_SATURDAY);
        assertThat(testSession.getSunday()).isEqualTo(DEFAULT_SUNDAY);
        assertThat(testSession.getIsPeriodic()).isEqualTo(DEFAULT_IS_PERIODIC);
        assertThat(testSession.getIsMinorAllowed()).isEqualTo(DEFAULT_IS_MINOR_ALLOWED);
        assertThat(testSession.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createSessionWithExistingId() throws Exception {
        // Create the Session with an existing ID
        session.setId(1L);

        int databaseSizeBeforeCreate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setSessionMode(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setSessionType(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionJoinModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setSessionJoinMode(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setTitle(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setSessionSize(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTargetedGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setTargetedGender(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsPeriodicIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setIsPeriodic(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsMinorAllowedIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setIsMinorAllowed(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        // set the field null
        session.setIsActive(null);

        // Create the Session, which fails.

        restSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isBadRequest());

        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessionList
        restSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionMode").value(hasItem(DEFAULT_SESSION_MODE.toString())))
            .andExpect(jsonPath("$.[*].sessionType").value(hasItem(DEFAULT_SESSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sessionJoinMode").value(hasItem(DEFAULT_SESSION_JOIN_MODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodeEndDate").value(hasItem(DEFAULT_PERIODE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].sessionSize").value(hasItem(DEFAULT_SESSION_SIZE)))
            .andExpect(jsonPath("$.[*].targetedGender").value(hasItem(DEFAULT_TARGETED_GENDER.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].monday").value(hasItem(DEFAULT_MONDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].tuesday").value(hasItem(DEFAULT_TUESDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].wednesday").value(hasItem(DEFAULT_WEDNESDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].thursday").value(hasItem(DEFAULT_THURSDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].friday").value(hasItem(DEFAULT_FRIDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].saturday").value(hasItem(DEFAULT_SATURDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].sunday").value(hasItem(DEFAULT_SUNDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].isPeriodic").value(hasItem(DEFAULT_IS_PERIODIC.booleanValue())))
            .andExpect(jsonPath("$.[*].isMinorAllowed").value(hasItem(DEFAULT_IS_MINOR_ALLOWED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sessionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sessionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sessionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sessionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.sessionMode").value(DEFAULT_SESSION_MODE.toString()))
            .andExpect(jsonPath("$.sessionType").value(DEFAULT_SESSION_TYPE.toString()))
            .andExpect(jsonPath("$.sessionJoinMode").value(DEFAULT_SESSION_JOIN_MODE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.periodStartDate").value(DEFAULT_PERIOD_START_DATE.toString()))
            .andExpect(jsonPath("$.periodeEndDate").value(DEFAULT_PERIODE_END_DATE.toString()))
            .andExpect(jsonPath("$.sessionSize").value(DEFAULT_SESSION_SIZE))
            .andExpect(jsonPath("$.targetedGender").value(DEFAULT_TARGETED_GENDER.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.monday").value(DEFAULT_MONDAY.booleanValue()))
            .andExpect(jsonPath("$.tuesday").value(DEFAULT_TUESDAY.booleanValue()))
            .andExpect(jsonPath("$.wednesday").value(DEFAULT_WEDNESDAY.booleanValue()))
            .andExpect(jsonPath("$.thursday").value(DEFAULT_THURSDAY.booleanValue()))
            .andExpect(jsonPath("$.friday").value(DEFAULT_FRIDAY.booleanValue()))
            .andExpect(jsonPath("$.saturday").value(DEFAULT_SATURDAY.booleanValue()))
            .andExpect(jsonPath("$.sunday").value(DEFAULT_SUNDAY.booleanValue()))
            .andExpect(jsonPath("$.isPeriodic").value(DEFAULT_IS_PERIODIC.booleanValue()))
            .andExpect(jsonPath("$.isMinorAllowed").value(DEFAULT_IS_MINOR_ALLOWED.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        sessionSearchRepository.save(session);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());

        // Update the session
        Session updatedSession = sessionRepository.findById(session.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSession are not directly saved in db
        em.detach(updatedSession);
        updatedSession
            .sessionMode(UPDATED_SESSION_MODE)
            .sessionType(UPDATED_SESSION_TYPE)
            .sessionJoinMode(UPDATED_SESSION_JOIN_MODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodeEndDate(UPDATED_PERIODE_END_DATE)
            .sessionSize(UPDATED_SESSION_SIZE)
            .targetedGender(UPDATED_TARGETED_GENDER)
            .price(UPDATED_PRICE)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .monday(UPDATED_MONDAY)
            .tuesday(UPDATED_TUESDAY)
            .wednesday(UPDATED_WEDNESDAY)
            .thursday(UPDATED_THURSDAY)
            .friday(UPDATED_FRIDAY)
            .saturday(UPDATED_SATURDAY)
            .sunday(UPDATED_SUNDAY)
            .isPeriodic(UPDATED_IS_PERIODIC)
            .isMinorAllowed(UPDATED_IS_MINOR_ALLOWED)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSession))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getSessionMode()).isEqualTo(UPDATED_SESSION_MODE);
        assertThat(testSession.getSessionType()).isEqualTo(UPDATED_SESSION_TYPE);
        assertThat(testSession.getSessionJoinMode()).isEqualTo(UPDATED_SESSION_JOIN_MODE);
        assertThat(testSession.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSession.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testSession.getPeriodeEndDate()).isEqualTo(UPDATED_PERIODE_END_DATE);

        assertThat(testSession.getSessionSize()).isEqualTo(UPDATED_SESSION_SIZE);
        assertThat(testSession.getTargetedGender()).isEqualTo(UPDATED_TARGETED_GENDER);
        assertThat(testSession.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSession.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSession.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSession.getMonday()).isEqualTo(UPDATED_MONDAY);
        assertThat(testSession.getTuesday()).isEqualTo(UPDATED_TUESDAY);
        assertThat(testSession.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
        assertThat(testSession.getThursday()).isEqualTo(UPDATED_THURSDAY);
        assertThat(testSession.getFriday()).isEqualTo(UPDATED_FRIDAY);
        assertThat(testSession.getSaturday()).isEqualTo(UPDATED_SATURDAY);
        assertThat(testSession.getSunday()).isEqualTo(UPDATED_SUNDAY);
        assertThat(testSession.getIsPeriodic()).isEqualTo(UPDATED_IS_PERIODIC);
        assertThat(testSession.getIsMinorAllowed()).isEqualTo(UPDATED_IS_MINOR_ALLOWED);
        assertThat(testSession.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Session> sessionSearchList = IterableUtils.toList(sessionSearchRepository.findAll());
                Session testSessionSearch = sessionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSessionSearch.getSessionMode()).isEqualTo(UPDATED_SESSION_MODE);
                assertThat(testSessionSearch.getSessionType()).isEqualTo(UPDATED_SESSION_TYPE);
                assertThat(testSessionSearch.getSessionJoinMode()).isEqualTo(UPDATED_SESSION_JOIN_MODE);
                assertThat(testSessionSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testSessionSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSessionSearch.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
                assertThat(testSessionSearch.getPeriodeEndDate()).isEqualTo(UPDATED_PERIODE_END_DATE);
                assertThat(testSessionSearch.getSessionSize()).isEqualTo(UPDATED_SESSION_SIZE);
                assertThat(testSessionSearch.getTargetedGender()).isEqualTo(UPDATED_TARGETED_GENDER);
                assertThat(testSessionSearch.getPrice()).isEqualTo(UPDATED_PRICE);
                assertThat(testSessionSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSessionSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSessionSearch.getMonday()).isEqualTo(UPDATED_MONDAY);
                assertThat(testSessionSearch.getTuesday()).isEqualTo(UPDATED_TUESDAY);
                assertThat(testSessionSearch.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
                assertThat(testSessionSearch.getThursday()).isEqualTo(UPDATED_THURSDAY);
                assertThat(testSessionSearch.getFriday()).isEqualTo(UPDATED_FRIDAY);
                assertThat(testSessionSearch.getSaturday()).isEqualTo(UPDATED_SATURDAY);
                assertThat(testSessionSearch.getSunday()).isEqualTo(UPDATED_SUNDAY);
                assertThat(testSessionSearch.getIsPeriodic()).isEqualTo(UPDATED_IS_PERIODIC);
                assertThat(testSessionSearch.getIsMinorAllowed()).isEqualTo(UPDATED_IS_MINOR_ALLOWED);
                assertThat(testSessionSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, session.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(session))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(session))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession
            .sessionType(UPDATED_SESSION_TYPE)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodeEndDate(UPDATED_PERIODE_END_DATE)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .tuesday(UPDATED_TUESDAY)
            .wednesday(UPDATED_WEDNESDAY)
            .friday(UPDATED_FRIDAY)
            .isMinorAllowed(UPDATED_IS_MINOR_ALLOWED)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSession))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getSessionMode()).isEqualTo(DEFAULT_SESSION_MODE);
        assertThat(testSession.getSessionType()).isEqualTo(UPDATED_SESSION_TYPE);
        assertThat(testSession.getSessionJoinMode()).isEqualTo(DEFAULT_SESSION_JOIN_MODE);
        assertThat(testSession.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSession.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testSession.getPeriodeEndDate()).isEqualTo(UPDATED_PERIODE_END_DATE);
        assertThat(testSession.getSessionSize()).isEqualTo(DEFAULT_SESSION_SIZE);
        assertThat(testSession.getTargetedGender()).isEqualTo(DEFAULT_TARGETED_GENDER);
        assertThat(testSession.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testSession.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSession.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSession.getMonday()).isEqualTo(DEFAULT_MONDAY);
        assertThat(testSession.getTuesday()).isEqualTo(UPDATED_TUESDAY);
        assertThat(testSession.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
        assertThat(testSession.getThursday()).isEqualTo(DEFAULT_THURSDAY);
        assertThat(testSession.getFriday()).isEqualTo(UPDATED_FRIDAY);
        assertThat(testSession.getSaturday()).isEqualTo(DEFAULT_SATURDAY);
        assertThat(testSession.getSunday()).isEqualTo(DEFAULT_SUNDAY);
        assertThat(testSession.getIsPeriodic()).isEqualTo(DEFAULT_IS_PERIODIC);
        assertThat(testSession.getIsMinorAllowed()).isEqualTo(UPDATED_IS_MINOR_ALLOWED);
        assertThat(testSession.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSessionWithPatch() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session using partial update
        Session partialUpdatedSession = new Session();
        partialUpdatedSession.setId(session.getId());

        partialUpdatedSession
            .sessionMode(UPDATED_SESSION_MODE)
            .sessionType(UPDATED_SESSION_TYPE)
            .sessionJoinMode(UPDATED_SESSION_JOIN_MODE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodeEndDate(UPDATED_PERIODE_END_DATE)
            .sessionSize(UPDATED_SESSION_SIZE)
            .targetedGender(UPDATED_TARGETED_GENDER)
            .price(UPDATED_PRICE)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .monday(UPDATED_MONDAY)
            .tuesday(UPDATED_TUESDAY)
            .wednesday(UPDATED_WEDNESDAY)
            .thursday(UPDATED_THURSDAY)
            .friday(UPDATED_FRIDAY)
            .saturday(UPDATED_SATURDAY)
            .sunday(UPDATED_SUNDAY)
            .isPeriodic(UPDATED_IS_PERIODIC)
            .isMinorAllowed(UPDATED_IS_MINOR_ALLOWED)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSession))
            )
            .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessionList.get(sessionList.size() - 1);
        assertThat(testSession.getSessionMode()).isEqualTo(UPDATED_SESSION_MODE);
        assertThat(testSession.getSessionType()).isEqualTo(UPDATED_SESSION_TYPE);
        assertThat(testSession.getSessionJoinMode()).isEqualTo(UPDATED_SESSION_JOIN_MODE);
        assertThat(testSession.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSession.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSession.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testSession.getPeriodeEndDate()).isEqualTo(UPDATED_PERIODE_END_DATE);
        assertThat(testSession.getSessionSize()).isEqualTo(UPDATED_SESSION_SIZE);
        assertThat(testSession.getTargetedGender()).isEqualTo(UPDATED_TARGETED_GENDER);
        assertThat(testSession.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testSession.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSession.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSession.getMonday()).isEqualTo(UPDATED_MONDAY);
        assertThat(testSession.getTuesday()).isEqualTo(UPDATED_TUESDAY);
        assertThat(testSession.getWednesday()).isEqualTo(UPDATED_WEDNESDAY);
        assertThat(testSession.getThursday()).isEqualTo(UPDATED_THURSDAY);
        assertThat(testSession.getFriday()).isEqualTo(UPDATED_FRIDAY);
        assertThat(testSession.getSaturday()).isEqualTo(UPDATED_SATURDAY);
        assertThat(testSession.getSunday()).isEqualTo(UPDATED_SUNDAY);
        assertThat(testSession.getIsPeriodic()).isEqualTo(UPDATED_IS_PERIODIC);
        assertThat(testSession.getIsMinorAllowed()).isEqualTo(UPDATED_IS_MINOR_ALLOWED);
        assertThat(testSession.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, session.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(session))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(session))
            )
            .andExpect(status().isBadRequest());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSession() throws Exception {
        int databaseSizeBeforeUpdate = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        session.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(session)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Session in the database
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);
        sessionRepository.save(session);
        sessionSearchRepository.save(session);

        int databaseSizeBeforeDelete = sessionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the session
        restSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, session.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Session> sessionList = sessionRepository.findAll();
        assertThat(sessionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSession() throws Exception {
        // Initialize the database
        session = sessionRepository.saveAndFlush(session);
        sessionSearchRepository.save(session);

        // Search the session
        restSessionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionMode").value(hasItem(DEFAULT_SESSION_MODE.toString())))
            .andExpect(jsonPath("$.[*].sessionType").value(hasItem(DEFAULT_SESSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sessionJoinMode").value(hasItem(DEFAULT_SESSION_JOIN_MODE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].periodStartDate").value(hasItem(DEFAULT_PERIOD_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].periodeEndDate").value(hasItem(DEFAULT_PERIODE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].sessionSize").value(hasItem(DEFAULT_SESSION_SIZE)))
            .andExpect(jsonPath("$.[*].targetedGender").value(hasItem(DEFAULT_TARGETED_GENDER.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].monday").value(hasItem(DEFAULT_MONDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].tuesday").value(hasItem(DEFAULT_TUESDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].wednesday").value(hasItem(DEFAULT_WEDNESDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].thursday").value(hasItem(DEFAULT_THURSDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].friday").value(hasItem(DEFAULT_FRIDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].saturday").value(hasItem(DEFAULT_SATURDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].sunday").value(hasItem(DEFAULT_SUNDAY.booleanValue())))
            .andExpect(jsonPath("$.[*].isPeriodic").value(hasItem(DEFAULT_IS_PERIODIC.booleanValue())))
            .andExpect(jsonPath("$.[*].isMinorAllowed").value(hasItem(DEFAULT_IS_MINOR_ALLOWED.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
