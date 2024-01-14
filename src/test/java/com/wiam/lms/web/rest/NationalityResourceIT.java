package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Nationality;
import com.wiam.lms.repository.NationalityRepository;
import com.wiam.lms.repository.search.NationalitySearchRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link NationalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NationalityResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_LAT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_LAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nationalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/nationalities/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NationalityRepository nationalityRepository;

    @Autowired
    private NationalitySearchRepository nationalitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNationalityMockMvc;

    private Nationality nationality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nationality createEntity(EntityManager em) {
        Nationality nationality = new Nationality().nameAr(DEFAULT_NAME_AR).nameLat(DEFAULT_NAME_LAT);
        return nationality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nationality createUpdatedEntity(EntityManager em) {
        Nationality nationality = new Nationality().nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);
        return nationality;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        nationalitySearchRepository.deleteAll();
        assertThat(nationalitySearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        nationality = createEntity(em);
    }

    @Test
    @Transactional
    void createNationality() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        // Create the Nationality
        restNationalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testNationality.getNameLat()).isEqualTo(DEFAULT_NAME_LAT);
    }

    @Test
    @Transactional
    void createNationalityWithExistingId() throws Exception {
        // Create the Nationality with an existing ID
        nationality.setId(1L);

        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameArIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        // set the field null
        nationality.setNameAr(null);

        // Create the Nationality, which fails.

        restNationalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        // set the field null
        nationality.setNameLat(null);

        // Create the Nationality, which fails.

        restNationalityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isBadRequest());

        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllNationalities() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get all the nationalityList
        restNationalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameLat").value(hasItem(DEFAULT_NAME_LAT)));
    }

    @Test
    @Transactional
    void getNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get the nationality
        restNationalityMockMvc
            .perform(get(ENTITY_API_URL_ID, nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nationality.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameLat").value(DEFAULT_NAME_LAT));
    }

    @Test
    @Transactional
    void getNonExistingNationality() throws Exception {
        // Get the nationality
        restNationalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationalitySearchRepository.save(nationality);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());

        // Update the nationality
        Nationality updatedNationality = nationalityRepository.findById(nationality.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNationality are not directly saved in db
        em.detach(updatedNationality);
        updatedNationality.nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);

        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNationality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNationality))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testNationality.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Nationality> nationalitySearchList = IterableUtils.toList(nationalitySearchRepository.findAll());
                Nationality testNationalitySearch = nationalitySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testNationalitySearch.getNameAr()).isEqualTo(UPDATED_NAME_AR);
                assertThat(testNationalitySearch.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
            });
    }

    @Test
    @Transactional
    void putNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationality.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationality)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateNationalityWithPatch() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality using partial update
        Nationality partialUpdatedNationality = new Nationality();
        partialUpdatedNationality.setId(nationality.getId());

        partialUpdatedNationality.nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);

        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationality))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testNationality.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
    }

    @Test
    @Transactional
    void fullUpdateNationalityWithPatch() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality using partial update
        Nationality partialUpdatedNationality = new Nationality();
        partialUpdatedNationality.setId(nationality.getId());

        partialUpdatedNationality.nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);

        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationality))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testNationality.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
    }

    @Test
    @Transactional
    void patchNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nationality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationality))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        nationality.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nationality))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);
        nationalityRepository.save(nationality);
        nationalitySearchRepository.save(nationality);

        int databaseSizeBeforeDelete = nationalityRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the nationality
        restNationalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, nationality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(nationalitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchNationality() throws Exception {
        // Initialize the database
        nationality = nationalityRepository.saveAndFlush(nationality);
        nationalitySearchRepository.save(nationality);

        // Search the nationality
        restNationalityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameLat").value(hasItem(DEFAULT_NAME_LAT)));
    }
}
