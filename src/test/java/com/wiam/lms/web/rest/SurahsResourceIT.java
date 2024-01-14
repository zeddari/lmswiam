package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Surahs;
import com.wiam.lms.repository.SurahsRepository;
import com.wiam.lms.repository.search.SurahsSearchRepository;
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
 * Integration tests for the {@link SurahsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurahsResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN_TRANSLATION = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN_TRANSLATION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/surahs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/surahs/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private SurahsRepository surahsRepository;

    @Autowired
    private SurahsSearchRepository surahsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurahsMockMvc;

    private Surahs surahs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surahs createEntity(EntityManager em) {
        Surahs surahs = new Surahs()
            .number(DEFAULT_NUMBER)
            .nameAr(DEFAULT_NAME_AR)
            .nameEn(DEFAULT_NAME_EN)
            .nameEnTranslation(DEFAULT_NAME_EN_TRANSLATION)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return surahs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surahs createUpdatedEntity(EntityManager em) {
        Surahs surahs = new Surahs()
            .number(UPDATED_NUMBER)
            .nameAr(UPDATED_NAME_AR)
            .nameEn(UPDATED_NAME_EN)
            .nameEnTranslation(UPDATED_NAME_EN_TRANSLATION)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return surahs;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        surahsSearchRepository.deleteAll();
        assertThat(surahsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        surahs = createEntity(em);
    }

    @Test
    @Transactional
    void createSurahs() throws Exception {
        int databaseSizeBeforeCreate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        // Create the Surahs
        restSurahsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surahs)))
            .andExpect(status().isCreated());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Surahs testSurahs = surahsList.get(surahsList.size() - 1);
        assertThat(testSurahs.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSurahs.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testSurahs.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testSurahs.getNameEnTranslation()).isEqualTo(DEFAULT_NAME_EN_TRANSLATION);
        assertThat(testSurahs.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSurahs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSurahs.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createSurahsWithExistingId() throws Exception {
        // Create the Surahs with an existing ID
        surahs.setId(1);

        int databaseSizeBeforeCreate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurahsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surahs)))
            .andExpect(status().isBadRequest());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSurahs() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);

        // Get all the surahsList
        restSurahsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surahs.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR.toString())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN.toString())))
            .andExpect(jsonPath("$.[*].nameEnTranslation").value(hasItem(DEFAULT_NAME_EN_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getSurahs() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);

        // Get the surahs
        restSurahsMockMvc
            .perform(get(ENTITY_API_URL_ID, surahs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surahs.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR.toString()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN.toString()))
            .andExpect(jsonPath("$.nameEnTranslation").value(DEFAULT_NAME_EN_TRANSLATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingSurahs() throws Exception {
        // Get the surahs
        restSurahsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSurahs() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);

        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        surahsSearchRepository.save(surahs);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());

        // Update the surahs
        Surahs updatedSurahs = surahsRepository.findById(surahs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurahs are not directly saved in db
        em.detach(updatedSurahs);
        updatedSurahs
            .number(UPDATED_NUMBER)
            .nameAr(UPDATED_NAME_AR)
            .nameEn(UPDATED_NAME_EN)
            .nameEnTranslation(UPDATED_NAME_EN_TRANSLATION)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSurahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurahs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurahs))
            )
            .andExpect(status().isOk());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        Surahs testSurahs = surahsList.get(surahsList.size() - 1);
        assertThat(testSurahs.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSurahs.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testSurahs.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testSurahs.getNameEnTranslation()).isEqualTo(UPDATED_NAME_EN_TRANSLATION);
        assertThat(testSurahs.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSurahs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSurahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Surahs> surahsSearchList = IterableUtils.toList(surahsSearchRepository.findAll());
                Surahs testSurahsSearch = surahsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSurahsSearch.getNumber()).isEqualTo(UPDATED_NUMBER);
                assertThat(testSurahsSearch.getNameAr()).isEqualTo(UPDATED_NAME_AR);
                assertThat(testSurahsSearch.getNameEn()).isEqualTo(UPDATED_NAME_EN);
                assertThat(testSurahsSearch.getNameEnTranslation()).isEqualTo(UPDATED_NAME_EN_TRANSLATION);
                assertThat(testSurahsSearch.getType()).isEqualTo(UPDATED_TYPE);
                assertThat(testSurahsSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testSurahsSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surahs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surahs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSurahsWithPatch() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);

        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();

        // Update the surahs using partial update
        Surahs partialUpdatedSurahs = new Surahs();
        partialUpdatedSurahs.setId(surahs.getId());

        partialUpdatedSurahs.nameEnTranslation(UPDATED_NAME_EN_TRANSLATION).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restSurahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurahs))
            )
            .andExpect(status().isOk());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        Surahs testSurahs = surahsList.get(surahsList.size() - 1);
        assertThat(testSurahs.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSurahs.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testSurahs.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testSurahs.getNameEnTranslation()).isEqualTo(UPDATED_NAME_EN_TRANSLATION);
        assertThat(testSurahs.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSurahs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSurahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSurahsWithPatch() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);

        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();

        // Update the surahs using partial update
        Surahs partialUpdatedSurahs = new Surahs();
        partialUpdatedSurahs.setId(surahs.getId());

        partialUpdatedSurahs
            .number(UPDATED_NUMBER)
            .nameAr(UPDATED_NAME_AR)
            .nameEn(UPDATED_NAME_EN)
            .nameEnTranslation(UPDATED_NAME_EN_TRANSLATION)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restSurahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurahs))
            )
            .andExpect(status().isOk());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        Surahs testSurahs = surahsList.get(surahsList.size() - 1);
        assertThat(testSurahs.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSurahs.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testSurahs.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testSurahs.getNameEnTranslation()).isEqualTo(UPDATED_NAME_EN_TRANSLATION);
        assertThat(testSurahs.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSurahs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSurahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurahs() throws Exception {
        int databaseSizeBeforeUpdate = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        surahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(surahs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surahs in the database
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSurahs() throws Exception {
        // Initialize the database
        surahsRepository.saveAndFlush(surahs);
        surahsRepository.save(surahs);
        surahsSearchRepository.save(surahs);

        int databaseSizeBeforeDelete = surahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the surahs
        restSurahsMockMvc
            .perform(delete(ENTITY_API_URL_ID, surahs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Surahs> surahsList = surahsRepository.findAll();
        assertThat(surahsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(surahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSurahs() throws Exception {
        // Initialize the database
        surahs = surahsRepository.saveAndFlush(surahs);
        surahsSearchRepository.save(surahs);

        // Search the surahs
        restSurahsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + surahs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surahs.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR.toString())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN.toString())))
            .andExpect(jsonPath("$.[*].nameEnTranslation").value(hasItem(DEFAULT_NAME_EN_TRANSLATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
