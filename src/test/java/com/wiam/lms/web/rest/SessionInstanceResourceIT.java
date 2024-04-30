package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.search.SessionInstanceSearchRepository;
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

/**
 * Integration tests for the {@link SessionInstanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SessionInstanceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SESSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SESSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final Attendance DEFAULT_ATTENDANCE = Attendance.PRESENT;
    private static final Attendance UPDATED_ATTENDANCE = Attendance.ABSENT;

    private static final String DEFAULT_JUSTIF_REF = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIF_REF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/session-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/session-instances/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionInstanceRepository sessionInstanceRepository;

    @Mock
    private SessionInstanceRepository sessionInstanceRepositoryMock;

    @Autowired
    private SessionInstanceSearchRepository sessionInstanceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionInstanceMockMvc;

    private SessionInstance sessionInstance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionInstance createEntity(EntityManager em) {
        SessionInstance sessionInstance = new SessionInstance()
            .title(DEFAULT_TITLE)
            .sessionDate(DEFAULT_SESSION_DATE)
            .info(DEFAULT_INFO)
            .attendance(DEFAULT_ATTENDANCE)
            .justifRef(DEFAULT_JUSTIF_REF)
            .isActive(DEFAULT_IS_ACTIVE);
        return sessionInstance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionInstance createUpdatedEntity(EntityManager em) {
        SessionInstance sessionInstance = new SessionInstance()
            .title(UPDATED_TITLE)
            .sessionDate(UPDATED_SESSION_DATE)
            .info(UPDATED_INFO)
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .isActive(UPDATED_IS_ACTIVE);
        return sessionInstance;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        sessionInstanceSearchRepository.deleteAll();
        assertThat(sessionInstanceSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        sessionInstance = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionInstance() throws Exception {
        int databaseSizeBeforeCreate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // Create the SessionInstance
        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isCreated());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SessionInstance testSessionInstance = sessionInstanceList.get(sessionInstanceList.size() - 1);
        assertThat(testSessionInstance.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionInstance.getSessionDate()).isEqualTo(DEFAULT_SESSION_DATE);

        assertThat(testSessionInstance.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testSessionInstance.getAttendance()).isEqualTo(DEFAULT_ATTENDANCE);
        assertThat(testSessionInstance.getJustifRef()).isEqualTo(DEFAULT_JUSTIF_REF);
        assertThat(testSessionInstance.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createSessionInstanceWithExistingId() throws Exception {
        // Create the SessionInstance with an existing ID
        sessionInstance.setId(1L);

        int databaseSizeBeforeCreate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null
        sessionInstance.setTitle(null);

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSessionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null
        sessionInstance.setSessionDate(null);

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAttendanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null
        sessionInstance.setAttendance(null);

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        // set the field null
        sessionInstance.setIsActive(null);

        // Create the SessionInstance, which fails.

        restSessionInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSessionInstances() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);

        // Get all the sessionInstanceList
        restSessionInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].sessionDate").value(hasItem(DEFAULT_SESSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].attendance").value(hasItem(DEFAULT_ATTENDANCE.toString())))
            .andExpect(jsonPath("$.[*].justifRef").value(hasItem(DEFAULT_JUSTIF_REF)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionInstancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(sessionInstanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionInstanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sessionInstanceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSessionInstancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sessionInstanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSessionInstanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sessionInstanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSessionInstance() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);

        // Get the sessionInstance
        restSessionInstanceMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionInstance.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.sessionDate").value(DEFAULT_SESSION_DATE.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.attendance").value(DEFAULT_ATTENDANCE.toString()))
            .andExpect(jsonPath("$.justifRef").value(DEFAULT_JUSTIF_REF))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSessionInstance() throws Exception {
        // Get the sessionInstance
        restSessionInstanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionInstance() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);

        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        sessionInstanceSearchRepository.save(sessionInstance);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());

        // Update the sessionInstance
        SessionInstance updatedSessionInstance = sessionInstanceRepository.findById(sessionInstance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSessionInstance are not directly saved in db
        em.detach(updatedSessionInstance);
        updatedSessionInstance
            .title(UPDATED_TITLE)
            .sessionDate(UPDATED_SESSION_DATE)
            .info(UPDATED_INFO)
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSessionInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSessionInstance))
            )
            .andExpect(status().isOk());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        SessionInstance testSessionInstance = sessionInstanceList.get(sessionInstanceList.size() - 1);
        assertThat(testSessionInstance.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionInstance.getSessionDate()).isEqualTo(UPDATED_SESSION_DATE);

        assertThat(testSessionInstance.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testSessionInstance.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testSessionInstance.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testSessionInstance.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SessionInstance> sessionInstanceSearchList = IterableUtils.toList(sessionInstanceSearchRepository.findAll());
                SessionInstance testSessionInstanceSearch = sessionInstanceSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSessionInstanceSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testSessionInstanceSearch.getSessionDate()).isEqualTo(UPDATED_SESSION_DATE);

                assertThat(testSessionInstanceSearch.getInfo()).isEqualTo(UPDATED_INFO);
                assertThat(testSessionInstanceSearch.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
                assertThat(testSessionInstanceSearch.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
                assertThat(testSessionInstanceSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSessionInstanceWithPatch() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);

        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();

        // Update the sessionInstance using partial update
        SessionInstance partialUpdatedSessionInstance = new SessionInstance();
        partialUpdatedSessionInstance.setId(sessionInstance.getId());

        partialUpdatedSessionInstance
            .sessionDate(UPDATED_SESSION_DATE)
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionInstance))
            )
            .andExpect(status().isOk());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        SessionInstance testSessionInstance = sessionInstanceList.get(sessionInstanceList.size() - 1);
        assertThat(testSessionInstance.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionInstance.getSessionDate()).isEqualTo(UPDATED_SESSION_DATE);

        assertThat(testSessionInstance.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testSessionInstance.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testSessionInstance.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testSessionInstance.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSessionInstanceWithPatch() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);

        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();

        // Update the sessionInstance using partial update
        SessionInstance partialUpdatedSessionInstance = new SessionInstance();
        partialUpdatedSessionInstance.setId(sessionInstance.getId());

        partialUpdatedSessionInstance
            .title(UPDATED_TITLE)
            .sessionDate(UPDATED_SESSION_DATE)
            .info(UPDATED_INFO)
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .isActive(UPDATED_IS_ACTIVE);

        restSessionInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionInstance))
            )
            .andExpect(status().isOk());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        SessionInstance testSessionInstance = sessionInstanceList.get(sessionInstanceList.size() - 1);
        assertThat(testSessionInstance.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionInstance.getSessionDate()).isEqualTo(UPDATED_SESSION_DATE);

        assertThat(testSessionInstance.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testSessionInstance.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testSessionInstance.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testSessionInstance.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionInstance() throws Exception {
        int databaseSizeBeforeUpdate = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        sessionInstance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionInstance in the database
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSessionInstance() throws Exception {
        // Initialize the database
        sessionInstanceRepository.saveAndFlush(sessionInstance);
        sessionInstanceRepository.save(sessionInstance);
        sessionInstanceSearchRepository.save(sessionInstance);

        int databaseSizeBeforeDelete = sessionInstanceRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the sessionInstance
        restSessionInstanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionInstance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionInstance> sessionInstanceList = sessionInstanceRepository.findAll();
        assertThat(sessionInstanceList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionInstanceSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSessionInstance() throws Exception {
        // Initialize the database
        sessionInstance = sessionInstanceRepository.saveAndFlush(sessionInstance);
        sessionInstanceSearchRepository.save(sessionInstance);

        // Search the sessionInstance
        restSessionInstanceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sessionInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].sessionDate").value(hasItem(DEFAULT_SESSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
            .andExpect(jsonPath("$.[*].attendance").value(hasItem(DEFAULT_ATTENDANCE.toString())))
            .andExpect(jsonPath("$.[*].justifRef").value(hasItem(DEFAULT_JUSTIF_REF)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
