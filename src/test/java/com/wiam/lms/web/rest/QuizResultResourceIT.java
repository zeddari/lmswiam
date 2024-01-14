package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.QuizResult;
import com.wiam.lms.repository.QuizResultRepository;
import com.wiam.lms.repository.search.QuizResultSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link QuizResultResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuizResultResourceIT {

    private static final Double DEFAULT_RESULT = 1D;
    private static final Double UPDATED_RESULT = 2D;

    private static final ZonedDateTime DEFAULT_SUBMITTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SUBMITTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/quiz-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/quiz-results/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Mock
    private QuizResultRepository quizResultRepositoryMock;

    @Autowired
    private QuizResultSearchRepository quizResultSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizResultMockMvc;

    private QuizResult quizResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizResult createEntity(EntityManager em) {
        QuizResult quizResult = new QuizResult().result(DEFAULT_RESULT).submittedAT(DEFAULT_SUBMITTED_AT);
        return quizResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizResult createUpdatedEntity(EntityManager em) {
        QuizResult quizResult = new QuizResult().result(UPDATED_RESULT).submittedAT(UPDATED_SUBMITTED_AT);
        return quizResult;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        quizResultSearchRepository.deleteAll();
        assertThat(quizResultSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        quizResult = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizResult() throws Exception {
        int databaseSizeBeforeCreate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        // Create the QuizResult
        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResult)))
            .andExpect(status().isCreated());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testQuizResult.getSubmittedAT()).isEqualTo(DEFAULT_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void createQuizResultWithExistingId() throws Exception {
        // Create the QuizResult with an existing ID
        quizResult.setId(1L);

        int databaseSizeBeforeCreate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResult)))
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        // set the field null
        quizResult.setResult(null);

        // Create the QuizResult, which fails.

        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResult)))
            .andExpect(status().isBadRequest());

        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSubmittedATIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        // set the field null
        quizResult.setSubmittedAT(null);

        // Create the QuizResult, which fails.

        restQuizResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResult)))
            .andExpect(status().isBadRequest());

        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllQuizResults() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        // Get all the quizResultList
        restQuizResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.doubleValue())))
            .andExpect(jsonPath("$.[*].submittedAT").value(hasItem(sameInstant(DEFAULT_SUBMITTED_AT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizResultsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quizResultRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizResultMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quizResultRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizResultsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quizResultRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizResultMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quizResultRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        // Get the quizResult
        restQuizResultMockMvc
            .perform(get(ENTITY_API_URL_ID, quizResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizResult.getId().intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.doubleValue()))
            .andExpect(jsonPath("$.submittedAT").value(sameInstant(DEFAULT_SUBMITTED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingQuizResult() throws Exception {
        // Get the quizResult
        restQuizResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        quizResultSearchRepository.save(quizResult);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());

        // Update the quizResult
        QuizResult updatedQuizResult = quizResultRepository.findById(quizResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizResult are not directly saved in db
        em.detach(updatedQuizResult);
        updatedQuizResult.result(UPDATED_RESULT).submittedAT(UPDATED_SUBMITTED_AT);

        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuizResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuizResult))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testQuizResult.getSubmittedAT()).isEqualTo(UPDATED_SUBMITTED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<QuizResult> quizResultSearchList = IterableUtils.toList(quizResultSearchRepository.findAll());
                QuizResult testQuizResultSearch = quizResultSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testQuizResultSearch.getResult()).isEqualTo(UPDATED_RESULT);
                assertThat(testQuizResultSearch.getSubmittedAT()).isEqualTo(UPDATED_SUBMITTED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateQuizResultWithPatch() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();

        // Update the quizResult using partial update
        QuizResult partialUpdatedQuizResult = new QuizResult();
        partialUpdatedQuizResult.setId(quizResult.getId());

        partialUpdatedQuizResult.result(UPDATED_RESULT).submittedAT(UPDATED_SUBMITTED_AT);

        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizResult))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testQuizResult.getSubmittedAT()).isEqualTo(UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void fullUpdateQuizResultWithPatch() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);

        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();

        // Update the quizResult using partial update
        QuizResult partialUpdatedQuizResult = new QuizResult();
        partialUpdatedQuizResult.setId(quizResult.getId());

        partialUpdatedQuizResult.result(UPDATED_RESULT).submittedAT(UPDATED_SUBMITTED_AT);

        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizResult))
            )
            .andExpect(status().isOk());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        QuizResult testQuizResult = quizResultList.get(quizResultList.size() - 1);
        assertThat(testQuizResult.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testQuizResult.getSubmittedAT()).isEqualTo(UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizResult() throws Exception {
        int databaseSizeBeforeUpdate = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        quizResult.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quizResult))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizResult in the database
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteQuizResult() throws Exception {
        // Initialize the database
        quizResultRepository.saveAndFlush(quizResult);
        quizResultRepository.save(quizResult);
        quizResultSearchRepository.save(quizResult);

        int databaseSizeBeforeDelete = quizResultRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the quizResult
        restQuizResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizResult> quizResultList = quizResultRepository.findAll();
        assertThat(quizResultList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quizResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchQuizResult() throws Exception {
        // Initialize the database
        quizResult = quizResultRepository.saveAndFlush(quizResult);
        quizResultSearchRepository.save(quizResult);

        // Search the quizResult
        restQuizResultMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + quizResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.doubleValue())))
            .andExpect(jsonPath("$.[*].submittedAT").value(hasItem(sameInstant(DEFAULT_SUBMITTED_AT))));
    }
}
