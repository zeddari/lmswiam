package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Juzs;
import com.wiam.lms.repository.JuzsRepository;
import com.wiam.lms.repository.search.JuzsSearchRepository;
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
 * Integration tests for the {@link JuzsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JuzsResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/juzs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/juzs/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private JuzsRepository juzsRepository;

    @Autowired
    private JuzsSearchRepository juzsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJuzsMockMvc;

    private Juzs juzs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Juzs createEntity(EntityManager em) {
        Juzs juzs = new Juzs().createdAt(DEFAULT_CREATED_AT).updatedAt(DEFAULT_UPDATED_AT);
        return juzs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Juzs createUpdatedEntity(EntityManager em) {
        Juzs juzs = new Juzs().createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        return juzs;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        juzsSearchRepository.deleteAll();
        assertThat(juzsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        juzs = createEntity(em);
    }

    @Test
    @Transactional
    void createJuzs() throws Exception {
        int databaseSizeBeforeCreate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        // Create the Juzs
        restJuzsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juzs)))
            .andExpect(status().isCreated());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Juzs testJuzs = juzsList.get(juzsList.size() - 1);
        assertThat(testJuzs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testJuzs.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createJuzsWithExistingId() throws Exception {
        // Create the Juzs with an existing ID
        juzs.setId(1);

        int databaseSizeBeforeCreate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restJuzsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juzs)))
            .andExpect(status().isBadRequest());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllJuzs() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);

        // Get all the juzsList
        restJuzsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(juzs.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getJuzs() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);

        // Get the juzs
        restJuzsMockMvc
            .perform(get(ENTITY_API_URL_ID, juzs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(juzs.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingJuzs() throws Exception {
        // Get the juzs
        restJuzsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJuzs() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);

        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        juzsSearchRepository.save(juzs);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());

        // Update the juzs
        Juzs updatedJuzs = juzsRepository.findById(juzs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJuzs are not directly saved in db
        em.detach(updatedJuzs);
        updatedJuzs.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restJuzsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJuzs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJuzs))
            )
            .andExpect(status().isOk());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        Juzs testJuzs = juzsList.get(juzsList.size() - 1);
        assertThat(testJuzs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testJuzs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Juzs> juzsSearchList = IterableUtils.toList(juzsSearchRepository.findAll());
                Juzs testJuzsSearch = juzsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testJuzsSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testJuzsSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, juzs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(juzs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(juzs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(juzs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateJuzsWithPatch() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);

        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();

        // Update the juzs using partial update
        Juzs partialUpdatedJuzs = new Juzs();
        partialUpdatedJuzs.setId(juzs.getId());

        partialUpdatedJuzs.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restJuzsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJuzs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJuzs))
            )
            .andExpect(status().isOk());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        Juzs testJuzs = juzsList.get(juzsList.size() - 1);
        assertThat(testJuzs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testJuzs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateJuzsWithPatch() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);

        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();

        // Update the juzs using partial update
        Juzs partialUpdatedJuzs = new Juzs();
        partialUpdatedJuzs.setId(juzs.getId());

        partialUpdatedJuzs.createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restJuzsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJuzs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJuzs))
            )
            .andExpect(status().isOk());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        Juzs testJuzs = juzsList.get(juzsList.size() - 1);
        assertThat(testJuzs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testJuzs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, juzs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(juzs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(juzs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJuzs() throws Exception {
        int databaseSizeBeforeUpdate = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        juzs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJuzsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(juzs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Juzs in the database
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteJuzs() throws Exception {
        // Initialize the database
        juzsRepository.saveAndFlush(juzs);
        juzsRepository.save(juzs);
        juzsSearchRepository.save(juzs);

        int databaseSizeBeforeDelete = juzsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the juzs
        restJuzsMockMvc
            .perform(delete(ENTITY_API_URL_ID, juzs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Juzs> juzsList = juzsRepository.findAll();
        assertThat(juzsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(juzsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchJuzs() throws Exception {
        // Initialize the database
        juzs = juzsRepository.saveAndFlush(juzs);
        juzsSearchRepository.save(juzs);

        // Search the juzs
        restJuzsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + juzs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(juzs.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
