package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.ProgressionMode;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.Sourate;
import com.wiam.lms.domain.enumeration.Sourate;
import com.wiam.lms.domain.enumeration.Tilawa;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.search.ProgressionSearchRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ProgressionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProgressionResourceIT {

    private static final Attendance DEFAULT_ATTENDANCE = Attendance.PRESENT;
    private static final Attendance UPDATED_ATTENDANCE = Attendance.ABSENT;

    private static final String DEFAULT_JUSTIF_REF = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIF_REF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LATE_ARRIVAL = false;
    private static final Boolean UPDATED_LATE_ARRIVAL = true;

    private static final Boolean DEFAULT_EARLY_DEPARTURE = false;
    private static final Boolean UPDATED_EARLY_DEPARTURE = true;

    private static final ProgressionMode DEFAULT_PROGRESSION_MODE = ProgressionMode.SLOW;
    private static final ProgressionMode UPDATED_PROGRESSION_MODE = ProgressionMode.INTERMEDIATE;

    private static final ExamType DEFAULT_EXAM_TYPE = ExamType.NONE;
    private static final ExamType UPDATED_EXAM_TYPE = ExamType.OPTIONAL;

    private static final Riwayats DEFAULT_RIWAYA = Riwayats.WARSHS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_VIA_AL_SHATIBIYYAH;
    private static final Riwayats UPDATED_RIWAYA = Riwayats.QALOUNS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_ON_THE_AUTHORITY_OF_AL_SHATIBIYYAH;

    private static final Sourate DEFAULT_FROM_SOURATE = Sourate.FATIHA;
    private static final Sourate UPDATED_FROM_SOURATE = Sourate.BA9ARA;

    private static final Sourate DEFAULT_TO_SOURATE = Sourate.FATIHA;
    private static final Sourate UPDATED_TO_SOURATE = Sourate.BA9ARA;

    private static final Integer DEFAULT_FROM_AYA_NUM = 1;
    private static final Integer UPDATED_FROM_AYA_NUM = 2;

    private static final Integer DEFAULT_TO_AYA_NUM = 1;
    private static final Integer UPDATED_TO_AYA_NUM = 2;

    private static final String DEFAULT_FROM_AYA_VERSET = "AAAAAAAAAA";
    private static final String UPDATED_FROM_AYA_VERSET = "BBBBBBBBBB";

    private static final String DEFAULT_TO_AYA_VERSET = "AAAAAAAAAA";
    private static final String UPDATED_TO_AYA_VERSET = "BBBBBBBBBB";

    private static final Tilawa DEFAULT_TILAWA_TYPE = Tilawa.HIFD;
    private static final Tilawa UPDATED_TILAWA_TYPE = Tilawa.MORAJA3A;

    private static final Boolean DEFAULT_TASK_DONE = false;
    private static final Boolean UPDATED_TASK_DONE = true;

    private static final Integer DEFAULT_TAJWEED_SCORE = 1;
    private static final Integer UPDATED_TAJWEED_SCORE = 2;

    private static final Integer DEFAULT_HIFD_SCORE = 1;
    private static final Integer UPDATED_HIFD_SCORE = 2;

    private static final Integer DEFAULT_ADAE_SCORE = 1;
    private static final Integer UPDATED_ADAE_SCORE = 2;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/progressions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/progressions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgressionRepository progressionRepository;

    @Mock
    private ProgressionRepository progressionRepositoryMock;

    @Autowired
    private ProgressionSearchRepository progressionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgressionMockMvc;

    private Progression progression;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progression createEntity(EntityManager em) {
        Progression progression = new Progression()
            .attendance(DEFAULT_ATTENDANCE)
            .justifRef(DEFAULT_JUSTIF_REF)
            .lateArrival(DEFAULT_LATE_ARRIVAL)
            .earlyDeparture(DEFAULT_EARLY_DEPARTURE)
            .progressionMode(DEFAULT_PROGRESSION_MODE)
            .examType(DEFAULT_EXAM_TYPE)
            .riwaya(DEFAULT_RIWAYA)
            .fromSourate(DEFAULT_FROM_SOURATE)
            .toSourate(DEFAULT_TO_SOURATE)
            .fromAyaNum(DEFAULT_FROM_AYA_NUM)
            .toAyaNum(DEFAULT_TO_AYA_NUM)
            .fromAyaVerset(DEFAULT_FROM_AYA_VERSET)
            .toAyaVerset(DEFAULT_TO_AYA_VERSET)
            .tilawaType(DEFAULT_TILAWA_TYPE)
            .taskDone(DEFAULT_TASK_DONE)
            .tajweedScore(DEFAULT_TAJWEED_SCORE)
            .hifdScore(DEFAULT_HIFD_SCORE)
            .adaeScore(DEFAULT_ADAE_SCORE)
            .observation(DEFAULT_OBSERVATION);
        return progression;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Progression createUpdatedEntity(EntityManager em) {
        Progression progression = new Progression()
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .lateArrival(UPDATED_LATE_ARRIVAL)
            .earlyDeparture(UPDATED_EARLY_DEPARTURE)
            .progressionMode(UPDATED_PROGRESSION_MODE)
            .examType(UPDATED_EXAM_TYPE)
            .riwaya(UPDATED_RIWAYA)
            .fromSourate(UPDATED_FROM_SOURATE)
            .toSourate(UPDATED_TO_SOURATE)
            .fromAyaNum(UPDATED_FROM_AYA_NUM)
            .toAyaNum(UPDATED_TO_AYA_NUM)
            .fromAyaVerset(UPDATED_FROM_AYA_VERSET)
            .toAyaVerset(UPDATED_TO_AYA_VERSET)
            .tilawaType(UPDATED_TILAWA_TYPE)
            .taskDone(UPDATED_TASK_DONE)
            .tajweedScore(UPDATED_TAJWEED_SCORE)
            .hifdScore(UPDATED_HIFD_SCORE)
            .adaeScore(UPDATED_ADAE_SCORE)
            .observation(UPDATED_OBSERVATION);
        return progression;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        progressionSearchRepository.deleteAll();
        assertThat(progressionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        progression = createEntity(em);
    }

    @Test
    @Transactional
    void createProgression() throws Exception {
        int databaseSizeBeforeCreate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // Create the Progression
        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isCreated());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Progression testProgression = progressionList.get(progressionList.size() - 1);
        assertThat(testProgression.getAttendance()).isEqualTo(DEFAULT_ATTENDANCE);
        assertThat(testProgression.getJustifRef()).isEqualTo(DEFAULT_JUSTIF_REF);
        assertThat(testProgression.getLateArrival()).isEqualTo(DEFAULT_LATE_ARRIVAL);
        assertThat(testProgression.getEarlyDeparture()).isEqualTo(DEFAULT_EARLY_DEPARTURE);
        assertThat(testProgression.getProgressionMode()).isEqualTo(DEFAULT_PROGRESSION_MODE);
        assertThat(testProgression.getExamType()).isEqualTo(DEFAULT_EXAM_TYPE);
        assertThat(testProgression.getRiwaya()).isEqualTo(DEFAULT_RIWAYA);
        assertThat(testProgression.getFromSourate()).isEqualTo(DEFAULT_FROM_SOURATE);
        assertThat(testProgression.getToSourate()).isEqualTo(DEFAULT_TO_SOURATE);
        assertThat(testProgression.getFromAyaNum()).isEqualTo(DEFAULT_FROM_AYA_NUM);
        assertThat(testProgression.getToAyaNum()).isEqualTo(DEFAULT_TO_AYA_NUM);
        assertThat(testProgression.getFromAyaVerset()).isEqualTo(DEFAULT_FROM_AYA_VERSET);
        assertThat(testProgression.getToAyaVerset()).isEqualTo(DEFAULT_TO_AYA_VERSET);
        assertThat(testProgression.getTilawaType()).isEqualTo(DEFAULT_TILAWA_TYPE);
        assertThat(testProgression.getTaskDone()).isEqualTo(DEFAULT_TASK_DONE);
        assertThat(testProgression.getTajweedScore()).isEqualTo(DEFAULT_TAJWEED_SCORE);
        assertThat(testProgression.getHifdScore()).isEqualTo(DEFAULT_HIFD_SCORE);
        assertThat(testProgression.getAdaeScore()).isEqualTo(DEFAULT_ADAE_SCORE);
        assertThat(testProgression.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createProgressionWithExistingId() throws Exception {
        // Create the Progression with an existing ID
        progression.setId(1L);

        int databaseSizeBeforeCreate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAttendanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setAttendance(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLateArrivalIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setLateArrival(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEarlyDepartureIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setEarlyDeparture(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTaskDoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setTaskDone(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTajweedScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setTajweedScore(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkHifdScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setHifdScore(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAdaeScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        // set the field null
        progression.setAdaeScore(null);

        // Create the Progression, which fails.

        restProgressionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isBadRequest());

        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProgressions() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);

        // Get all the progressionList
        restProgressionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(progression.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendance").value(hasItem(DEFAULT_ATTENDANCE.toString())))
            .andExpect(jsonPath("$.[*].justifRef").value(hasItem(DEFAULT_JUSTIF_REF)))
            .andExpect(jsonPath("$.[*].lateArrival").value(hasItem(DEFAULT_LATE_ARRIVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].earlyDeparture").value(hasItem(DEFAULT_EARLY_DEPARTURE.booleanValue())))
            .andExpect(jsonPath("$.[*].progressionMode").value(hasItem(DEFAULT_PROGRESSION_MODE.toString())))
            .andExpect(jsonPath("$.[*].examType").value(hasItem(DEFAULT_EXAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riwaya").value(hasItem(DEFAULT_RIWAYA.toString())))
            .andExpect(jsonPath("$.[*].fromSourate").value(hasItem(DEFAULT_FROM_SOURATE.toString())))
            .andExpect(jsonPath("$.[*].toSourate").value(hasItem(DEFAULT_TO_SOURATE.toString())))
            .andExpect(jsonPath("$.[*].fromAyaNum").value(hasItem(DEFAULT_FROM_AYA_NUM)))
            .andExpect(jsonPath("$.[*].toAyaNum").value(hasItem(DEFAULT_TO_AYA_NUM)))
            .andExpect(jsonPath("$.[*].fromAyaVerset").value(hasItem(DEFAULT_FROM_AYA_VERSET.toString())))
            .andExpect(jsonPath("$.[*].toAyaVerset").value(hasItem(DEFAULT_TO_AYA_VERSET.toString())))
            .andExpect(jsonPath("$.[*].tilawaType").value(hasItem(DEFAULT_TILAWA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].taskDone").value(hasItem(DEFAULT_TASK_DONE.booleanValue())))
            .andExpect(jsonPath("$.[*].tajweedScore").value(hasItem(DEFAULT_TAJWEED_SCORE)))
            .andExpect(jsonPath("$.[*].hifdScore").value(hasItem(DEFAULT_HIFD_SCORE)))
            .andExpect(jsonPath("$.[*].adaeScore").value(hasItem(DEFAULT_ADAE_SCORE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgressionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(progressionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgressionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(progressionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgressionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(progressionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgressionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(progressionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProgression() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);

        // Get the progression
        restProgressionMockMvc
            .perform(get(ENTITY_API_URL_ID, progression.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(progression.getId().intValue()))
            .andExpect(jsonPath("$.attendance").value(DEFAULT_ATTENDANCE.toString()))
            .andExpect(jsonPath("$.justifRef").value(DEFAULT_JUSTIF_REF))
            .andExpect(jsonPath("$.lateArrival").value(DEFAULT_LATE_ARRIVAL.booleanValue()))
            .andExpect(jsonPath("$.earlyDeparture").value(DEFAULT_EARLY_DEPARTURE.booleanValue()))
            .andExpect(jsonPath("$.progressionMode").value(DEFAULT_PROGRESSION_MODE.toString()))
            .andExpect(jsonPath("$.examType").value(DEFAULT_EXAM_TYPE.toString()))
            .andExpect(jsonPath("$.riwaya").value(DEFAULT_RIWAYA.toString()))
            .andExpect(jsonPath("$.fromSourate").value(DEFAULT_FROM_SOURATE.toString()))
            .andExpect(jsonPath("$.toSourate").value(DEFAULT_TO_SOURATE.toString()))
            .andExpect(jsonPath("$.fromAyaNum").value(DEFAULT_FROM_AYA_NUM))
            .andExpect(jsonPath("$.toAyaNum").value(DEFAULT_TO_AYA_NUM))
            .andExpect(jsonPath("$.fromAyaVerset").value(DEFAULT_FROM_AYA_VERSET.toString()))
            .andExpect(jsonPath("$.toAyaVerset").value(DEFAULT_TO_AYA_VERSET.toString()))
            .andExpect(jsonPath("$.tilawaType").value(DEFAULT_TILAWA_TYPE.toString()))
            .andExpect(jsonPath("$.taskDone").value(DEFAULT_TASK_DONE.booleanValue()))
            .andExpect(jsonPath("$.tajweedScore").value(DEFAULT_TAJWEED_SCORE))
            .andExpect(jsonPath("$.hifdScore").value(DEFAULT_HIFD_SCORE))
            .andExpect(jsonPath("$.adaeScore").value(DEFAULT_ADAE_SCORE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProgression() throws Exception {
        // Get the progression
        restProgressionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgression() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);

        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        progressionSearchRepository.save(progression);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());

        // Update the progression
        Progression updatedProgression = progressionRepository.findById(progression.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProgression are not directly saved in db
        em.detach(updatedProgression);
        updatedProgression
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .lateArrival(UPDATED_LATE_ARRIVAL)
            .earlyDeparture(UPDATED_EARLY_DEPARTURE)
            .progressionMode(UPDATED_PROGRESSION_MODE)
            .examType(UPDATED_EXAM_TYPE)
            .riwaya(UPDATED_RIWAYA)
            .fromSourate(UPDATED_FROM_SOURATE)
            .toSourate(UPDATED_TO_SOURATE)
            .fromAyaNum(UPDATED_FROM_AYA_NUM)
            .toAyaNum(UPDATED_TO_AYA_NUM)
            .fromAyaVerset(UPDATED_FROM_AYA_VERSET)
            .toAyaVerset(UPDATED_TO_AYA_VERSET)
            .tilawaType(UPDATED_TILAWA_TYPE)
            .taskDone(UPDATED_TASK_DONE)
            .tajweedScore(UPDATED_TAJWEED_SCORE)
            .hifdScore(UPDATED_HIFD_SCORE)
            .adaeScore(UPDATED_ADAE_SCORE)
            .observation(UPDATED_OBSERVATION);

        restProgressionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgression.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgression))
            )
            .andExpect(status().isOk());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        Progression testProgression = progressionList.get(progressionList.size() - 1);
        assertThat(testProgression.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testProgression.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testProgression.getLateArrival()).isEqualTo(UPDATED_LATE_ARRIVAL);
        assertThat(testProgression.getEarlyDeparture()).isEqualTo(UPDATED_EARLY_DEPARTURE);
        assertThat(testProgression.getProgressionMode()).isEqualTo(UPDATED_PROGRESSION_MODE);
        assertThat(testProgression.getExamType()).isEqualTo(UPDATED_EXAM_TYPE);
        assertThat(testProgression.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
        assertThat(testProgression.getFromSourate()).isEqualTo(UPDATED_FROM_SOURATE);
        assertThat(testProgression.getToSourate()).isEqualTo(UPDATED_TO_SOURATE);
        assertThat(testProgression.getFromAyaNum()).isEqualTo(UPDATED_FROM_AYA_NUM);
        assertThat(testProgression.getToAyaNum()).isEqualTo(UPDATED_TO_AYA_NUM);
        assertThat(testProgression.getFromAyaVerset()).isEqualTo(UPDATED_FROM_AYA_VERSET);
        assertThat(testProgression.getToAyaVerset()).isEqualTo(UPDATED_TO_AYA_VERSET);
        assertThat(testProgression.getTilawaType()).isEqualTo(UPDATED_TILAWA_TYPE);
        assertThat(testProgression.getTaskDone()).isEqualTo(UPDATED_TASK_DONE);
        assertThat(testProgression.getTajweedScore()).isEqualTo(UPDATED_TAJWEED_SCORE);
        assertThat(testProgression.getHifdScore()).isEqualTo(UPDATED_HIFD_SCORE);
        assertThat(testProgression.getAdaeScore()).isEqualTo(UPDATED_ADAE_SCORE);
        assertThat(testProgression.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Progression> progressionSearchList = IterableUtils.toList(progressionSearchRepository.findAll());
                Progression testProgressionSearch = progressionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProgressionSearch.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
                assertThat(testProgressionSearch.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
                assertThat(testProgressionSearch.getLateArrival()).isEqualTo(UPDATED_LATE_ARRIVAL);
                assertThat(testProgressionSearch.getEarlyDeparture()).isEqualTo(UPDATED_EARLY_DEPARTURE);
                assertThat(testProgressionSearch.getProgressionMode()).isEqualTo(UPDATED_PROGRESSION_MODE);
                assertThat(testProgressionSearch.getExamType()).isEqualTo(UPDATED_EXAM_TYPE);
                assertThat(testProgressionSearch.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
                assertThat(testProgressionSearch.getFromSourate()).isEqualTo(UPDATED_FROM_SOURATE);
                assertThat(testProgressionSearch.getToSourate()).isEqualTo(UPDATED_TO_SOURATE);
                assertThat(testProgressionSearch.getFromAyaNum()).isEqualTo(UPDATED_FROM_AYA_NUM);
                assertThat(testProgressionSearch.getToAyaNum()).isEqualTo(UPDATED_TO_AYA_NUM);
                assertThat(testProgressionSearch.getFromAyaVerset()).isEqualTo(UPDATED_FROM_AYA_VERSET);
                assertThat(testProgressionSearch.getToAyaVerset()).isEqualTo(UPDATED_TO_AYA_VERSET);
                assertThat(testProgressionSearch.getTilawaType()).isEqualTo(UPDATED_TILAWA_TYPE);
                assertThat(testProgressionSearch.getTaskDone()).isEqualTo(UPDATED_TASK_DONE);
                assertThat(testProgressionSearch.getTajweedScore()).isEqualTo(UPDATED_TAJWEED_SCORE);
                assertThat(testProgressionSearch.getHifdScore()).isEqualTo(UPDATED_HIFD_SCORE);
                assertThat(testProgressionSearch.getAdaeScore()).isEqualTo(UPDATED_ADAE_SCORE);
                assertThat(testProgressionSearch.getObservation()).isEqualTo(UPDATED_OBSERVATION);
            });
    }

    @Test
    @Transactional
    void putNonExistingProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, progression.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progression))
            )
            .andExpect(status().isBadRequest());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progression))
            )
            .andExpect(status().isBadRequest());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progression)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProgressionWithPatch() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);

        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();

        // Update the progression using partial update
        Progression partialUpdatedProgression = new Progression();
        partialUpdatedProgression.setId(progression.getId());

        partialUpdatedProgression
            .justifRef(UPDATED_JUSTIF_REF)
            .earlyDeparture(UPDATED_EARLY_DEPARTURE)
            .progressionMode(UPDATED_PROGRESSION_MODE)
            .examType(UPDATED_EXAM_TYPE)
            .riwaya(UPDATED_RIWAYA)
            .toSourate(UPDATED_TO_SOURATE)
            .fromAyaNum(UPDATED_FROM_AYA_NUM)
            .toAyaNum(UPDATED_TO_AYA_NUM)
            .taskDone(UPDATED_TASK_DONE)
            .observation(UPDATED_OBSERVATION);

        restProgressionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgression.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgression))
            )
            .andExpect(status().isOk());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        Progression testProgression = progressionList.get(progressionList.size() - 1);
        assertThat(testProgression.getAttendance()).isEqualTo(DEFAULT_ATTENDANCE);
        assertThat(testProgression.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testProgression.getLateArrival()).isEqualTo(DEFAULT_LATE_ARRIVAL);
        assertThat(testProgression.getEarlyDeparture()).isEqualTo(UPDATED_EARLY_DEPARTURE);
        assertThat(testProgression.getProgressionMode()).isEqualTo(UPDATED_PROGRESSION_MODE);
        assertThat(testProgression.getExamType()).isEqualTo(UPDATED_EXAM_TYPE);
        assertThat(testProgression.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
        assertThat(testProgression.getFromSourate()).isEqualTo(DEFAULT_FROM_SOURATE);
        assertThat(testProgression.getToSourate()).isEqualTo(UPDATED_TO_SOURATE);
        assertThat(testProgression.getFromAyaNum()).isEqualTo(UPDATED_FROM_AYA_NUM);
        assertThat(testProgression.getToAyaNum()).isEqualTo(UPDATED_TO_AYA_NUM);
        assertThat(testProgression.getFromAyaVerset()).isEqualTo(DEFAULT_FROM_AYA_VERSET);
        assertThat(testProgression.getToAyaVerset()).isEqualTo(DEFAULT_TO_AYA_VERSET);
        assertThat(testProgression.getTilawaType()).isEqualTo(DEFAULT_TILAWA_TYPE);
        assertThat(testProgression.getTaskDone()).isEqualTo(UPDATED_TASK_DONE);
        assertThat(testProgression.getTajweedScore()).isEqualTo(DEFAULT_TAJWEED_SCORE);
        assertThat(testProgression.getHifdScore()).isEqualTo(DEFAULT_HIFD_SCORE);
        assertThat(testProgression.getAdaeScore()).isEqualTo(DEFAULT_ADAE_SCORE);
        assertThat(testProgression.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateProgressionWithPatch() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);

        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();

        // Update the progression using partial update
        Progression partialUpdatedProgression = new Progression();
        partialUpdatedProgression.setId(progression.getId());

        partialUpdatedProgression
            .attendance(UPDATED_ATTENDANCE)
            .justifRef(UPDATED_JUSTIF_REF)
            .lateArrival(UPDATED_LATE_ARRIVAL)
            .earlyDeparture(UPDATED_EARLY_DEPARTURE)
            .progressionMode(UPDATED_PROGRESSION_MODE)
            .examType(UPDATED_EXAM_TYPE)
            .riwaya(UPDATED_RIWAYA)
            .fromSourate(UPDATED_FROM_SOURATE)
            .toSourate(UPDATED_TO_SOURATE)
            .fromAyaNum(UPDATED_FROM_AYA_NUM)
            .toAyaNum(UPDATED_TO_AYA_NUM)
            .fromAyaVerset(UPDATED_FROM_AYA_VERSET)
            .toAyaVerset(UPDATED_TO_AYA_VERSET)
            .tilawaType(UPDATED_TILAWA_TYPE)
            .taskDone(UPDATED_TASK_DONE)
            .tajweedScore(UPDATED_TAJWEED_SCORE)
            .hifdScore(UPDATED_HIFD_SCORE)
            .adaeScore(UPDATED_ADAE_SCORE)
            .observation(UPDATED_OBSERVATION);

        restProgressionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgression.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgression))
            )
            .andExpect(status().isOk());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        Progression testProgression = progressionList.get(progressionList.size() - 1);
        assertThat(testProgression.getAttendance()).isEqualTo(UPDATED_ATTENDANCE);
        assertThat(testProgression.getJustifRef()).isEqualTo(UPDATED_JUSTIF_REF);
        assertThat(testProgression.getLateArrival()).isEqualTo(UPDATED_LATE_ARRIVAL);
        assertThat(testProgression.getEarlyDeparture()).isEqualTo(UPDATED_EARLY_DEPARTURE);
        assertThat(testProgression.getProgressionMode()).isEqualTo(UPDATED_PROGRESSION_MODE);
        assertThat(testProgression.getExamType()).isEqualTo(UPDATED_EXAM_TYPE);
        assertThat(testProgression.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
        assertThat(testProgression.getFromSourate()).isEqualTo(UPDATED_FROM_SOURATE);
        assertThat(testProgression.getToSourate()).isEqualTo(UPDATED_TO_SOURATE);
        assertThat(testProgression.getFromAyaNum()).isEqualTo(UPDATED_FROM_AYA_NUM);
        assertThat(testProgression.getToAyaNum()).isEqualTo(UPDATED_TO_AYA_NUM);
        assertThat(testProgression.getFromAyaVerset()).isEqualTo(UPDATED_FROM_AYA_VERSET);
        assertThat(testProgression.getToAyaVerset()).isEqualTo(UPDATED_TO_AYA_VERSET);
        assertThat(testProgression.getTilawaType()).isEqualTo(UPDATED_TILAWA_TYPE);
        assertThat(testProgression.getTaskDone()).isEqualTo(UPDATED_TASK_DONE);
        assertThat(testProgression.getTajweedScore()).isEqualTo(UPDATED_TAJWEED_SCORE);
        assertThat(testProgression.getHifdScore()).isEqualTo(UPDATED_HIFD_SCORE);
        assertThat(testProgression.getAdaeScore()).isEqualTo(UPDATED_ADAE_SCORE);
        assertThat(testProgression.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, progression.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progression))
            )
            .andExpect(status().isBadRequest());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progression))
            )
            .andExpect(status().isBadRequest());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgression() throws Exception {
        int databaseSizeBeforeUpdate = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        progression.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(progression))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Progression in the database
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProgression() throws Exception {
        // Initialize the database
        progressionRepository.saveAndFlush(progression);
        progressionRepository.save(progression);
        progressionSearchRepository.save(progression);

        int databaseSizeBeforeDelete = progressionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the progression
        restProgressionMockMvc
            .perform(delete(ENTITY_API_URL_ID, progression.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Progression> progressionList = progressionRepository.findAll();
        assertThat(progressionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(progressionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProgression() throws Exception {
        // Initialize the database
        progression = progressionRepository.saveAndFlush(progression);
        progressionSearchRepository.save(progression);

        // Search the progression
        restProgressionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + progression.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(progression.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendance").value(hasItem(DEFAULT_ATTENDANCE.toString())))
            .andExpect(jsonPath("$.[*].justifRef").value(hasItem(DEFAULT_JUSTIF_REF)))
            .andExpect(jsonPath("$.[*].lateArrival").value(hasItem(DEFAULT_LATE_ARRIVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].earlyDeparture").value(hasItem(DEFAULT_EARLY_DEPARTURE.booleanValue())))
            .andExpect(jsonPath("$.[*].progressionMode").value(hasItem(DEFAULT_PROGRESSION_MODE.toString())))
            .andExpect(jsonPath("$.[*].examType").value(hasItem(DEFAULT_EXAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].riwaya").value(hasItem(DEFAULT_RIWAYA.toString())))
            .andExpect(jsonPath("$.[*].fromSourate").value(hasItem(DEFAULT_FROM_SOURATE.toString())))
            .andExpect(jsonPath("$.[*].toSourate").value(hasItem(DEFAULT_TO_SOURATE.toString())))
            .andExpect(jsonPath("$.[*].fromAyaNum").value(hasItem(DEFAULT_FROM_AYA_NUM)))
            .andExpect(jsonPath("$.[*].toAyaNum").value(hasItem(DEFAULT_TO_AYA_NUM)))
            .andExpect(jsonPath("$.[*].fromAyaVerset").value(hasItem(DEFAULT_FROM_AYA_VERSET.toString())))
            .andExpect(jsonPath("$.[*].toAyaVerset").value(hasItem(DEFAULT_TO_AYA_VERSET.toString())))
            .andExpect(jsonPath("$.[*].tilawaType").value(hasItem(DEFAULT_TILAWA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].taskDone").value(hasItem(DEFAULT_TASK_DONE.booleanValue())))
            .andExpect(jsonPath("$.[*].tajweedScore").value(hasItem(DEFAULT_TAJWEED_SCORE)))
            .andExpect(jsonPath("$.[*].hifdScore").value(hasItem(DEFAULT_HIFD_SCORE)))
            .andExpect(jsonPath("$.[*].adaeScore").value(hasItem(DEFAULT_ADAE_SCORE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }
}
