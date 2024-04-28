package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.StudentSponsoring;
import com.wiam.lms.repository.StudentSponsoringRepository;
import com.wiam.lms.repository.search.StudentSponsoringSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StudentSponsoringResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentSponsoringResourceIT {

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ALWAYS = false;
    private static final Boolean UPDATED_IS_ALWAYS = true;

    private static final String ENTITY_API_URL = "/api/student-sponsorings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/student-sponsorings/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentSponsoringRepository studentSponsoringRepository;

    @Autowired
    private StudentSponsoringSearchRepository studentSponsoringSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentSponsoringMockMvc;

    private StudentSponsoring studentSponsoring;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSponsoring createEntity(EntityManager em) {
        StudentSponsoring studentSponsoring = new StudentSponsoring()
            .ref(DEFAULT_REF)
            .message(DEFAULT_MESSAGE)
            .amount(DEFAULT_AMOUNT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isAlways(DEFAULT_IS_ALWAYS);
        return studentSponsoring;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentSponsoring createUpdatedEntity(EntityManager em) {
        StudentSponsoring studentSponsoring = new StudentSponsoring()
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE)
            .amount(UPDATED_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isAlways(UPDATED_IS_ALWAYS);
        return studentSponsoring;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        studentSponsoringSearchRepository.deleteAll();
        assertThat(studentSponsoringSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        studentSponsoring = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentSponsoring() throws Exception {
        int databaseSizeBeforeCreate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        // Create the StudentSponsoring
        restStudentSponsoringMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isCreated());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        StudentSponsoring testStudentSponsoring = studentSponsoringList.get(studentSponsoringList.size() - 1);
        assertThat(testStudentSponsoring.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testStudentSponsoring.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testStudentSponsoring.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testStudentSponsoring.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudentSponsoring.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStudentSponsoring.getIsAlways()).isEqualTo(DEFAULT_IS_ALWAYS);
    }

    @Test
    @Transactional
    void createStudentSponsoringWithExistingId() throws Exception {
        // Create the StudentSponsoring with an existing ID
        studentSponsoring.setId(1L);

        int databaseSizeBeforeCreate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentSponsoringMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        // set the field null
        studentSponsoring.setRef(null);

        // Create the StudentSponsoring, which fails.

        restStudentSponsoringMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        // set the field null
        studentSponsoring.setAmount(null);

        // Create the StudentSponsoring, which fails.

        restStudentSponsoringMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStudentSponsorings() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);

        // Get all the studentSponsoringList
        restStudentSponsoringMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentSponsoring.getId().intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isAlways").value(hasItem(DEFAULT_IS_ALWAYS.booleanValue())));
    }

    @Test
    @Transactional
    void getStudentSponsoring() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);

        // Get the studentSponsoring
        restStudentSponsoringMockMvc
            .perform(get(ENTITY_API_URL_ID, studentSponsoring.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentSponsoring.getId().intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isAlways").value(DEFAULT_IS_ALWAYS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingStudentSponsoring() throws Exception {
        // Get the studentSponsoring
        restStudentSponsoringMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentSponsoring() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);

        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        studentSponsoringSearchRepository.save(studentSponsoring);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());

        // Update the studentSponsoring
        StudentSponsoring updatedStudentSponsoring = studentSponsoringRepository.findById(studentSponsoring.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudentSponsoring are not directly saved in db
        em.detach(updatedStudentSponsoring);
        updatedStudentSponsoring
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE)
            .amount(UPDATED_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isAlways(UPDATED_IS_ALWAYS);

        restStudentSponsoringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentSponsoring.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentSponsoring))
            )
            .andExpect(status().isOk());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        StudentSponsoring testStudentSponsoring = studentSponsoringList.get(studentSponsoringList.size() - 1);
        assertThat(testStudentSponsoring.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testStudentSponsoring.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testStudentSponsoring.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testStudentSponsoring.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudentSponsoring.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudentSponsoring.getIsAlways()).isEqualTo(UPDATED_IS_ALWAYS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<StudentSponsoring> studentSponsoringSearchList = IterableUtils.toList(studentSponsoringSearchRepository.findAll());
                StudentSponsoring testStudentSponsoringSearch = studentSponsoringSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testStudentSponsoringSearch.getRef()).isEqualTo(UPDATED_REF);
                assertThat(testStudentSponsoringSearch.getMessage()).isEqualTo(UPDATED_MESSAGE);
                assertThat(testStudentSponsoringSearch.getAmount()).isEqualTo(UPDATED_AMOUNT);
                assertThat(testStudentSponsoringSearch.getStartDate()).isEqualTo(UPDATED_START_DATE);
                assertThat(testStudentSponsoringSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testStudentSponsoringSearch.getIsAlways()).isEqualTo(UPDATED_IS_ALWAYS);
            });
    }

    @Test
    @Transactional
    void putNonExistingStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentSponsoring.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStudentSponsoringWithPatch() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);

        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();

        // Update the studentSponsoring using partial update
        StudentSponsoring partialUpdatedStudentSponsoring = new StudentSponsoring();
        partialUpdatedStudentSponsoring.setId(studentSponsoring.getId());

        partialUpdatedStudentSponsoring
            .message(UPDATED_MESSAGE)
            .amount(UPDATED_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isAlways(UPDATED_IS_ALWAYS);

        restStudentSponsoringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentSponsoring.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentSponsoring))
            )
            .andExpect(status().isOk());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        StudentSponsoring testStudentSponsoring = studentSponsoringList.get(studentSponsoringList.size() - 1);
        assertThat(testStudentSponsoring.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testStudentSponsoring.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testStudentSponsoring.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testStudentSponsoring.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudentSponsoring.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudentSponsoring.getIsAlways()).isEqualTo(UPDATED_IS_ALWAYS);
    }

    @Test
    @Transactional
    void fullUpdateStudentSponsoringWithPatch() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);

        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();

        // Update the studentSponsoring using partial update
        StudentSponsoring partialUpdatedStudentSponsoring = new StudentSponsoring();
        partialUpdatedStudentSponsoring.setId(studentSponsoring.getId());

        partialUpdatedStudentSponsoring
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE)
            .amount(UPDATED_AMOUNT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isAlways(UPDATED_IS_ALWAYS);

        restStudentSponsoringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentSponsoring.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentSponsoring))
            )
            .andExpect(status().isOk());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        StudentSponsoring testStudentSponsoring = studentSponsoringList.get(studentSponsoringList.size() - 1);
        assertThat(testStudentSponsoring.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testStudentSponsoring.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testStudentSponsoring.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testStudentSponsoring.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudentSponsoring.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudentSponsoring.getIsAlways()).isEqualTo(UPDATED_IS_ALWAYS);
    }

    @Test
    @Transactional
    void patchNonExistingStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentSponsoring.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentSponsoring() throws Exception {
        int databaseSizeBeforeUpdate = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        studentSponsoring.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentSponsoringMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentSponsoring))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentSponsoring in the database
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteStudentSponsoring() throws Exception {
        // Initialize the database
        studentSponsoringRepository.saveAndFlush(studentSponsoring);
        studentSponsoringRepository.save(studentSponsoring);
        studentSponsoringSearchRepository.save(studentSponsoring);

        int databaseSizeBeforeDelete = studentSponsoringRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the studentSponsoring
        restStudentSponsoringMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentSponsoring.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentSponsoring> studentSponsoringList = studentSponsoringRepository.findAll();
        assertThat(studentSponsoringList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(studentSponsoringSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchStudentSponsoring() throws Exception {
        // Initialize the database
        studentSponsoring = studentSponsoringRepository.saveAndFlush(studentSponsoring);
        studentSponsoringSearchRepository.save(studentSponsoring);

        // Search the studentSponsoring
        restStudentSponsoringMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + studentSponsoring.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentSponsoring.getId().intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isAlways").value(hasItem(DEFAULT_IS_ALWAYS.booleanValue())));
    }
}
