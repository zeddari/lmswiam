package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Hizbs;
import com.wiam.lms.repository.HizbsRepository;
import com.wiam.lms.repository.search.HizbsSearchRepository;
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
 * Integration tests for the {@link HizbsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HizbsResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/hizbs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/hizbs/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private HizbsRepository hizbsRepository;

    @Autowired
    private HizbsSearchRepository hizbsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHizbsMockMvc;

    private Hizbs hizbs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hizbs createEntity(EntityManager em) {
        Hizbs hizbs = new Hizbs().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        return hizbs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hizbs createUpdatedEntity(EntityManager em) {
        Hizbs hizbs = new Hizbs().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        return hizbs;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        hizbsSearchRepository.deleteAll();
        assertThat(hizbsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        hizbs = createEntity(em);
    }

    @Test
    @Transactional
    void createHizbs() throws Exception {
        int databaseSizeBeforeCreate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        // Create the Hizbs
        restHizbsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hizbs)))
            .andExpect(status().isCreated());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Hizbs testHizbs = hizbsList.get(hizbsList.size() - 1);
        assertThat(testHizbs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testHizbs.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createHizbsWithExistingId() throws Exception {
        // Create the Hizbs with an existing ID
        hizbs.setId(1);

        int databaseSizeBeforeCreate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHizbsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hizbs)))
            .andExpect(status().isBadRequest());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllHizbs() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);

        // Get all the hizbsList
        restHizbsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hizbs.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getHizbs() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);

        // Get the hizbs
        restHizbsMockMvc
            .perform(get(ENTITY_API_URL_ID, hizbs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hizbs.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingHizbs() throws Exception {
        // Get the hizbs
        restHizbsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHizbs() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);

        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        hizbsSearchRepository.save(hizbs);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());

        // Update the hizbs
        Hizbs updatedHizbs = hizbsRepository.findById(hizbs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHizbs are not directly saved in db
        em.detach(updatedHizbs);
        updatedHizbs.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restHizbsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHizbs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHizbs))
            )
            .andExpect(status().isOk());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        Hizbs testHizbs = hizbsList.get(hizbsList.size() - 1);
        assertThat(testHizbs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHizbs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Hizbs> hizbsSearchList = IterableUtils.toList(hizbsSearchRepository.findAll());
                Hizbs testHizbsSearch = hizbsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHizbsSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testHizbsSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hizbs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hizbs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hizbs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hizbs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateHizbsWithPatch() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);

        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();

        // Update the hizbs using partial update
        Hizbs partialUpdatedHizbs = new Hizbs();
        partialUpdatedHizbs.setId(hizbs.getId());

        partialUpdatedHizbs.createdAt(UPDATED_CREATED_AT);

        restHizbsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHizbs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHizbs))
            )
            .andExpect(status().isOk());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        Hizbs testHizbs = hizbsList.get(hizbsList.size() - 1);
        assertThat(testHizbs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHizbs.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateHizbsWithPatch() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);

        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();

        // Update the hizbs using partial update
        Hizbs partialUpdatedHizbs = new Hizbs();
        partialUpdatedHizbs.setId(hizbs.getId());

        partialUpdatedHizbs.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restHizbsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHizbs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHizbs))
            )
            .andExpect(status().isOk());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        Hizbs testHizbs = hizbsList.get(hizbsList.size() - 1);
        assertThat(testHizbs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHizbs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hizbs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hizbs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hizbs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHizbs() throws Exception {
        int databaseSizeBeforeUpdate = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        hizbs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHizbsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hizbs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hizbs in the database
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteHizbs() throws Exception {
        // Initialize the database
        hizbsRepository.saveAndFlush(hizbs);
        hizbsRepository.save(hizbs);
        hizbsSearchRepository.save(hizbs);

        int databaseSizeBeforeDelete = hizbsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the hizbs
        restHizbsMockMvc
            .perform(delete(ENTITY_API_URL_ID, hizbs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hizbs> hizbsList = hizbsRepository.findAll();
        assertThat(hizbsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hizbsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchHizbs() throws Exception {
        // Initialize the database
        hizbs = hizbsRepository.saveAndFlush(hizbs);
        hizbsSearchRepository.save(hizbs);

        // Search the hizbs
        restHizbsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + hizbs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hizbs.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
