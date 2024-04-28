package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Depense;
import com.wiam.lms.domain.enumeration.DepenseFrequency;
import com.wiam.lms.domain.enumeration.DepenseTarget;
import com.wiam.lms.domain.enumeration.DepenseType;
import com.wiam.lms.repository.DepenseRepository;
import com.wiam.lms.repository.search.DepenseSearchRepository;
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
 * Integration tests for the {@link DepenseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepenseResourceIT {

    private static final DepenseType DEFAULT_TYPE = DepenseType.REGISTER;
    private static final DepenseType UPDATED_TYPE = DepenseType.MONTHLY_FEES;

    private static final DepenseTarget DEFAULT_TARGET = DepenseTarget.TEACHER;
    private static final DepenseTarget UPDATED_TARGET = DepenseTarget.REDAL;

    private static final DepenseFrequency DEFAULT_FREQUENCY = DepenseFrequency.MONTHLY;
    private static final DepenseFrequency UPDATED_FREQUENCY = DepenseFrequency.YEARLY;

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/depenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/depenses/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepenseRepository depenseRepository;

    @Mock
    private DepenseRepository depenseRepositoryMock;

    @Autowired
    private DepenseSearchRepository depenseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepenseMockMvc;

    private Depense depense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createEntity(EntityManager em) {
        Depense depense = new Depense()
            .type(DEFAULT_TYPE)
            .target(DEFAULT_TARGET)
            .frequency(DEFAULT_FREQUENCY)
            .amount(DEFAULT_AMOUNT)
            .ref(DEFAULT_REF)
            .message(DEFAULT_MESSAGE);
        return depense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createUpdatedEntity(EntityManager em) {
        Depense depense = new Depense()
            .type(UPDATED_TYPE)
            .target(UPDATED_TARGET)
            .frequency(UPDATED_FREQUENCY)
            .amount(UPDATED_AMOUNT)
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE);
        return depense;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        depenseSearchRepository.deleteAll();
        assertThat(depenseSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        depense = createEntity(em);
    }

    @Test
    @Transactional
    void createDepense() throws Exception {
        int databaseSizeBeforeCreate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // Create the Depense
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isCreated());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDepense.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testDepense.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testDepense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDepense.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testDepense.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void createDepenseWithExistingId() throws Exception {
        // Create the Depense with an existing ID
        depense.setId(1L);

        int databaseSizeBeforeCreate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // set the field null
        depense.setType(null);

        // Create the Depense, which fails.

        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTargetIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // set the field null
        depense.setTarget(null);

        // Create the Depense, which fails.

        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // set the field null
        depense.setFrequency(null);

        // Create the Depense, which fails.

        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // set the field null
        depense.setAmount(null);

        // Create the Depense, which fails.

        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        // set the field null
        depense.setRef(null);

        // Create the Depense, which fails.

        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDepenses() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get all the depenseList
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepensesWithEagerRelationshipsIsEnabled() throws Exception {
        when(depenseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepenseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depenseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepensesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depenseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepenseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(depenseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get the depense
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL_ID, depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depense.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingDepense() throws Exception {
        // Get the depense
        restDepenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depenseSearchRepository.save(depense);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());

        // Update the depense
        Depense updatedDepense = depenseRepository.findById(depense.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepense are not directly saved in db
        em.detach(updatedDepense);
        updatedDepense
            .type(UPDATED_TYPE)
            .target(UPDATED_TARGET)
            .frequency(UPDATED_FREQUENCY)
            .amount(UPDATED_AMOUNT)
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE);

        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDepense.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testDepense.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDepense.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDepense.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testDepense.getMessage()).isEqualTo(UPDATED_MESSAGE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Depense> depenseSearchList = IterableUtils.toList(depenseSearchRepository.findAll());
                Depense testDepenseSearch = depenseSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDepenseSearch.getType()).isEqualTo(UPDATED_TYPE);
                assertThat(testDepenseSearch.getTarget()).isEqualTo(UPDATED_TARGET);
                assertThat(testDepenseSearch.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
                assertThat(testDepenseSearch.getAmount()).isEqualTo(UPDATED_AMOUNT);
                assertThat(testDepenseSearch.getRef()).isEqualTo(UPDATED_REF);
                assertThat(testDepenseSearch.getMessage()).isEqualTo(UPDATED_MESSAGE);
            });
    }

    @Test
    @Transactional
    void putNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense.target(UPDATED_TARGET).frequency(UPDATED_FREQUENCY).message(UPDATED_MESSAGE);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDepense.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testDepense.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDepense.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDepense.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testDepense.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense
            .type(UPDATED_TYPE)
            .target(UPDATED_TARGET)
            .frequency(UPDATED_FREQUENCY)
            .amount(UPDATED_AMOUNT)
            .ref(UPDATED_REF)
            .message(UPDATED_MESSAGE);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDepense.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testDepense.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testDepense.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDepense.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testDepense.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        depense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);
        depenseRepository.save(depense);
        depenseSearchRepository.save(depense);

        int databaseSizeBeforeDelete = depenseRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the depense
        restDepenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, depense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(depenseSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDepense() throws Exception {
        // Initialize the database
        depense = depenseRepository.saveAndFlush(depense);
        depenseSearchRepository.save(depense);

        // Search the depense
        restDepenseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }
}
