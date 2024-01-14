package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.AyahEdition;
import com.wiam.lms.repository.AyahEditionRepository;
import com.wiam.lms.repository.search.AyahEditionSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
 * Integration tests for the {@link AyahEditionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AyahEditionResourceIT {

    private static final Integer DEFAULT_AYAH_ID = 1;
    private static final Integer UPDATED_AYAH_ID = 2;

    private static final Integer DEFAULT_EDITION_ID = 1;
    private static final Integer UPDATED_EDITION_ID = 2;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_AUDIO = false;
    private static final Boolean UPDATED_IS_AUDIO = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ayah-editions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ayah-editions/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private AyahEditionRepository ayahEditionRepository;

    @Autowired
    private AyahEditionSearchRepository ayahEditionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAyahEditionMockMvc;

    private AyahEdition ayahEdition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AyahEdition createEntity(EntityManager em) {
        AyahEdition ayahEdition = new AyahEdition()
            .ayahId(DEFAULT_AYAH_ID)
            .editionId(DEFAULT_EDITION_ID)
            .data(DEFAULT_DATA)
            .isAudio(DEFAULT_IS_AUDIO)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return ayahEdition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AyahEdition createUpdatedEntity(EntityManager em) {
        AyahEdition ayahEdition = new AyahEdition()
            .ayahId(UPDATED_AYAH_ID)
            .editionId(UPDATED_EDITION_ID)
            .data(UPDATED_DATA)
            .isAudio(UPDATED_IS_AUDIO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return ayahEdition;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        ayahEditionSearchRepository.deleteAll();
        assertThat(ayahEditionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        ayahEdition = createEntity(em);
    }

    @Test
    @Transactional
    void createAyahEdition() throws Exception {
        int databaseSizeBeforeCreate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        // Create the AyahEdition
        restAyahEditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahEdition)))
            .andExpect(status().isCreated());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AyahEdition testAyahEdition = ayahEditionList.get(ayahEditionList.size() - 1);
        assertThat(testAyahEdition.getAyahId()).isEqualTo(DEFAULT_AYAH_ID);
        assertThat(testAyahEdition.getEditionId()).isEqualTo(DEFAULT_EDITION_ID);
        assertThat(testAyahEdition.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testAyahEdition.getIsAudio()).isEqualTo(DEFAULT_IS_AUDIO);
        assertThat(testAyahEdition.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAyahEdition.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAyahEditionWithExistingId() throws Exception {
        // Create the AyahEdition with an existing ID
        ayahEdition.setId(1);

        int databaseSizeBeforeCreate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAyahEditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahEdition)))
            .andExpect(status().isBadRequest());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        // set the field null
        ayahEdition.setData(null);

        // Create the AyahEdition, which fails.

        restAyahEditionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahEdition)))
            .andExpect(status().isBadRequest());

        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAyahEditions() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);

        // Get all the ayahEditionList
        restAyahEditionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ayahEdition.getId().intValue())))
            .andExpect(jsonPath("$.[*].ayahId").value(hasItem(DEFAULT_AYAH_ID)))
            .andExpect(jsonPath("$.[*].editionId").value(hasItem(DEFAULT_EDITION_ID)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].isAudio").value(hasItem(DEFAULT_IS_AUDIO.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getAyahEdition() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);

        // Get the ayahEdition
        restAyahEditionMockMvc
            .perform(get(ENTITY_API_URL_ID, ayahEdition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ayahEdition.getId().intValue()))
            .andExpect(jsonPath("$.ayahId").value(DEFAULT_AYAH_ID))
            .andExpect(jsonPath("$.editionId").value(DEFAULT_EDITION_ID))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.isAudio").value(DEFAULT_IS_AUDIO.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingAyahEdition() throws Exception {
        // Get the ayahEdition
        restAyahEditionMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAyahEdition() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);

        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        ayahEditionSearchRepository.save(ayahEdition);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());

        // Update the ayahEdition
        AyahEdition updatedAyahEdition = ayahEditionRepository.findById(ayahEdition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAyahEdition are not directly saved in db
        em.detach(updatedAyahEdition);
        updatedAyahEdition
            .ayahId(UPDATED_AYAH_ID)
            .editionId(UPDATED_EDITION_ID)
            .data(UPDATED_DATA)
            .isAudio(UPDATED_IS_AUDIO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAyahEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAyahEdition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAyahEdition))
            )
            .andExpect(status().isOk());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        AyahEdition testAyahEdition = ayahEditionList.get(ayahEditionList.size() - 1);
        assertThat(testAyahEdition.getAyahId()).isEqualTo(UPDATED_AYAH_ID);
        assertThat(testAyahEdition.getEditionId()).isEqualTo(UPDATED_EDITION_ID);
        assertThat(testAyahEdition.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAyahEdition.getIsAudio()).isEqualTo(UPDATED_IS_AUDIO);
        assertThat(testAyahEdition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAyahEdition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AyahEdition> ayahEditionSearchList = IterableUtils.toList(ayahEditionSearchRepository.findAll());
                AyahEdition testAyahEditionSearch = ayahEditionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAyahEditionSearch.getAyahId()).isEqualTo(UPDATED_AYAH_ID);
                assertThat(testAyahEditionSearch.getEditionId()).isEqualTo(UPDATED_EDITION_ID);
                assertThat(testAyahEditionSearch.getData()).isEqualTo(UPDATED_DATA);
                assertThat(testAyahEditionSearch.getIsAudio()).isEqualTo(UPDATED_IS_AUDIO);
                assertThat(testAyahEditionSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testAyahEditionSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ayahEdition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ayahEdition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ayahEdition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahEdition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAyahEditionWithPatch() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);

        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();

        // Update the ayahEdition using partial update
        AyahEdition partialUpdatedAyahEdition = new AyahEdition();
        partialUpdatedAyahEdition.setId(ayahEdition.getId());

        partialUpdatedAyahEdition.editionId(UPDATED_EDITION_ID).data(UPDATED_DATA).isAudio(UPDATED_IS_AUDIO).updatedAt(UPDATED_UPDATED_AT);

        restAyahEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAyahEdition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAyahEdition))
            )
            .andExpect(status().isOk());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        AyahEdition testAyahEdition = ayahEditionList.get(ayahEditionList.size() - 1);
        assertThat(testAyahEdition.getAyahId()).isEqualTo(DEFAULT_AYAH_ID);
        assertThat(testAyahEdition.getEditionId()).isEqualTo(UPDATED_EDITION_ID);
        assertThat(testAyahEdition.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAyahEdition.getIsAudio()).isEqualTo(UPDATED_IS_AUDIO);
        assertThat(testAyahEdition.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAyahEdition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAyahEditionWithPatch() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);

        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();

        // Update the ayahEdition using partial update
        AyahEdition partialUpdatedAyahEdition = new AyahEdition();
        partialUpdatedAyahEdition.setId(ayahEdition.getId());

        partialUpdatedAyahEdition
            .ayahId(UPDATED_AYAH_ID)
            .editionId(UPDATED_EDITION_ID)
            .data(UPDATED_DATA)
            .isAudio(UPDATED_IS_AUDIO)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAyahEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAyahEdition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAyahEdition))
            )
            .andExpect(status().isOk());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        AyahEdition testAyahEdition = ayahEditionList.get(ayahEditionList.size() - 1);
        assertThat(testAyahEdition.getAyahId()).isEqualTo(UPDATED_AYAH_ID);
        assertThat(testAyahEdition.getEditionId()).isEqualTo(UPDATED_EDITION_ID);
        assertThat(testAyahEdition.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAyahEdition.getIsAudio()).isEqualTo(UPDATED_IS_AUDIO);
        assertThat(testAyahEdition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAyahEdition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ayahEdition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ayahEdition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ayahEdition))
            )
            .andExpect(status().isBadRequest());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAyahEdition() throws Exception {
        int databaseSizeBeforeUpdate = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        ayahEdition.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahEditionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ayahEdition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AyahEdition in the database
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAyahEdition() throws Exception {
        // Initialize the database
        ayahEditionRepository.saveAndFlush(ayahEdition);
        ayahEditionRepository.save(ayahEdition);
        ayahEditionSearchRepository.save(ayahEdition);

        int databaseSizeBeforeDelete = ayahEditionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ayahEdition
        restAyahEditionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ayahEdition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AyahEdition> ayahEditionList = ayahEditionRepository.findAll();
        assertThat(ayahEditionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahEditionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAyahEdition() throws Exception {
        // Initialize the database
        ayahEdition = ayahEditionRepository.saveAndFlush(ayahEdition);
        ayahEditionSearchRepository.save(ayahEdition);

        // Search the ayahEdition
        restAyahEditionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ayahEdition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ayahEdition.getId().intValue())))
            .andExpect(jsonPath("$.[*].ayahId").value(hasItem(DEFAULT_AYAH_ID)))
            .andExpect(jsonPath("$.[*].editionId").value(hasItem(DEFAULT_EDITION_ID)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].isAudio").value(hasItem(DEFAULT_IS_AUDIO.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
