package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.repository.AyahsRepository;
import com.wiam.lms.repository.search.AyahsSearchRepository;
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
 * Integration tests for the {@link AyahsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AyahsResourceIT {

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_TEXTDESC = "AAAAAAAAAA";
    private static final String UPDATED_TEXTDESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_IN_SURAH = 1;
    private static final Integer UPDATED_NUMBER_IN_SURAH = 2;

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer UPDATED_PAGE = 2;

    private static final Integer DEFAULT_SURAH_ID = 1;
    private static final Integer UPDATED_SURAH_ID = 2;

    private static final Integer DEFAULT_HIZB_ID = 1;
    private static final Integer UPDATED_HIZB_ID = 2;

    private static final Integer DEFAULT_JUZ_ID = 1;
    private static final Integer UPDATED_JUZ_ID = 2;

    private static final Boolean DEFAULT_SAJDA = false;
    private static final Boolean UPDATED_SAJDA = true;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/ayahs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ayahs/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private AyahsRepository ayahsRepository;

    @Autowired
    private AyahsSearchRepository ayahsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAyahsMockMvc;

    private Ayahs ayahs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ayahs createEntity(EntityManager em) {
        Ayahs ayahs = new Ayahs()
            .number(DEFAULT_NUMBER)
            .textdesc(DEFAULT_TEXTDESC)
            .numberInSurah(DEFAULT_NUMBER_IN_SURAH)
            .page(DEFAULT_PAGE)
            .surahId(DEFAULT_SURAH_ID)
            .hizbId(DEFAULT_HIZB_ID)
            .juzId(DEFAULT_JUZ_ID)
            .sajda(DEFAULT_SAJDA)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return ayahs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ayahs createUpdatedEntity(EntityManager em) {
        Ayahs ayahs = new Ayahs()
            .number(UPDATED_NUMBER)
            .textdesc(UPDATED_TEXTDESC)
            .numberInSurah(UPDATED_NUMBER_IN_SURAH)
            .page(UPDATED_PAGE)
            .surahId(UPDATED_SURAH_ID)
            .hizbId(UPDATED_HIZB_ID)
            .juzId(UPDATED_JUZ_ID)
            .sajda(UPDATED_SAJDA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return ayahs;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        ayahsSearchRepository.deleteAll();
        assertThat(ayahsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        ayahs = createEntity(em);
    }

    @Test
    @Transactional
    void createAyahs() throws Exception {
        int databaseSizeBeforeCreate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        // Create the Ayahs
        restAyahsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahs)))
            .andExpect(status().isCreated());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Ayahs testAyahs = ayahsList.get(ayahsList.size() - 1);
        assertThat(testAyahs.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testAyahs.getTextdesc()).isEqualTo(DEFAULT_TEXTDESC);
        assertThat(testAyahs.getNumberInSurah()).isEqualTo(DEFAULT_NUMBER_IN_SURAH);
        assertThat(testAyahs.getPage()).isEqualTo(DEFAULT_PAGE);
        assertThat(testAyahs.getSurahId()).isEqualTo(DEFAULT_SURAH_ID);
        assertThat(testAyahs.getHizbId()).isEqualTo(DEFAULT_HIZB_ID);
        assertThat(testAyahs.getJuzId()).isEqualTo(DEFAULT_JUZ_ID);
        assertThat(testAyahs.getSajda()).isEqualTo(DEFAULT_SAJDA);
        assertThat(testAyahs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAyahs.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createAyahsWithExistingId() throws Exception {
        // Create the Ayahs with an existing ID
        ayahs.setId(1);

        int databaseSizeBeforeCreate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAyahsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahs)))
            .andExpect(status().isBadRequest());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTextdescIsRequired() throws Exception {
        int databaseSizeBeforeTest = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        // set the field null
        ayahs.setTextdesc(null);

        // Create the Ayahs, which fails.

        restAyahsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahs)))
            .andExpect(status().isBadRequest());

        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAyahs() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);

        // Get all the ayahsList
        restAyahsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ayahs.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].textdesc").value(hasItem(DEFAULT_TEXTDESC)))
            .andExpect(jsonPath("$.[*].numberInSurah").value(hasItem(DEFAULT_NUMBER_IN_SURAH)))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE)))
            .andExpect(jsonPath("$.[*].surahId").value(hasItem(DEFAULT_SURAH_ID)))
            .andExpect(jsonPath("$.[*].hizbId").value(hasItem(DEFAULT_HIZB_ID)))
            .andExpect(jsonPath("$.[*].juzId").value(hasItem(DEFAULT_JUZ_ID)))
            .andExpect(jsonPath("$.[*].sajda").value(hasItem(DEFAULT_SAJDA.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getAyahs() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);

        // Get the ayahs
        restAyahsMockMvc
            .perform(get(ENTITY_API_URL_ID, ayahs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ayahs.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.textdesc").value(DEFAULT_TEXTDESC))
            .andExpect(jsonPath("$.numberInSurah").value(DEFAULT_NUMBER_IN_SURAH))
            .andExpect(jsonPath("$.page").value(DEFAULT_PAGE))
            .andExpect(jsonPath("$.surahId").value(DEFAULT_SURAH_ID))
            .andExpect(jsonPath("$.hizbId").value(DEFAULT_HIZB_ID))
            .andExpect(jsonPath("$.juzId").value(DEFAULT_JUZ_ID))
            .andExpect(jsonPath("$.sajda").value(DEFAULT_SAJDA.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingAyahs() throws Exception {
        // Get the ayahs
        restAyahsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAyahs() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);

        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        ayahsSearchRepository.save(ayahs);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());

        // Update the ayahs
        Ayahs updatedAyahs = ayahsRepository.findById(ayahs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAyahs are not directly saved in db
        em.detach(updatedAyahs);
        updatedAyahs
            .number(UPDATED_NUMBER)
            .textdesc(UPDATED_TEXTDESC)
            .numberInSurah(UPDATED_NUMBER_IN_SURAH)
            .page(UPDATED_PAGE)
            .surahId(UPDATED_SURAH_ID)
            .hizbId(UPDATED_HIZB_ID)
            .juzId(UPDATED_JUZ_ID)
            .sajda(UPDATED_SAJDA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAyahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAyahs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAyahs))
            )
            .andExpect(status().isOk());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        Ayahs testAyahs = ayahsList.get(ayahsList.size() - 1);
        assertThat(testAyahs.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAyahs.getTextdesc()).isEqualTo(UPDATED_TEXTDESC);
        assertThat(testAyahs.getNumberInSurah()).isEqualTo(UPDATED_NUMBER_IN_SURAH);
        assertThat(testAyahs.getPage()).isEqualTo(UPDATED_PAGE);
        assertThat(testAyahs.getSurahId()).isEqualTo(UPDATED_SURAH_ID);
        assertThat(testAyahs.getHizbId()).isEqualTo(UPDATED_HIZB_ID);
        assertThat(testAyahs.getJuzId()).isEqualTo(UPDATED_JUZ_ID);
        assertThat(testAyahs.getSajda()).isEqualTo(UPDATED_SAJDA);
        assertThat(testAyahs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAyahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Ayahs> ayahsSearchList = IterableUtils.toList(ayahsSearchRepository.findAll());
                Ayahs testAyahsSearch = ayahsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAyahsSearch.getNumber()).isEqualTo(UPDATED_NUMBER);
                assertThat(testAyahsSearch.getTextdesc()).isEqualTo(UPDATED_TEXTDESC);
                assertThat(testAyahsSearch.getNumberInSurah()).isEqualTo(UPDATED_NUMBER_IN_SURAH);
                assertThat(testAyahsSearch.getPage()).isEqualTo(UPDATED_PAGE);
                assertThat(testAyahsSearch.getSurahId()).isEqualTo(UPDATED_SURAH_ID);
                assertThat(testAyahsSearch.getHizbId()).isEqualTo(UPDATED_HIZB_ID);
                assertThat(testAyahsSearch.getJuzId()).isEqualTo(UPDATED_JUZ_ID);
                assertThat(testAyahsSearch.getSajda()).isEqualTo(UPDATED_SAJDA);
                assertThat(testAyahsSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testAyahsSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ayahs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ayahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ayahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ayahs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAyahsWithPatch() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);

        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();

        // Update the ayahs using partial update
        Ayahs partialUpdatedAyahs = new Ayahs();
        partialUpdatedAyahs.setId(ayahs.getId());

        partialUpdatedAyahs
            .number(UPDATED_NUMBER)
            .surahId(UPDATED_SURAH_ID)
            .juzId(UPDATED_JUZ_ID)
            .sajda(UPDATED_SAJDA)
            .updatedAt(UPDATED_UPDATED_AT);

        restAyahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAyahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAyahs))
            )
            .andExpect(status().isOk());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        Ayahs testAyahs = ayahsList.get(ayahsList.size() - 1);
        assertThat(testAyahs.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAyahs.getTextdesc()).isEqualTo(DEFAULT_TEXTDESC);
        assertThat(testAyahs.getNumberInSurah()).isEqualTo(DEFAULT_NUMBER_IN_SURAH);
        assertThat(testAyahs.getPage()).isEqualTo(DEFAULT_PAGE);
        assertThat(testAyahs.getSurahId()).isEqualTo(UPDATED_SURAH_ID);
        assertThat(testAyahs.getHizbId()).isEqualTo(DEFAULT_HIZB_ID);
        assertThat(testAyahs.getJuzId()).isEqualTo(UPDATED_JUZ_ID);
        assertThat(testAyahs.getSajda()).isEqualTo(UPDATED_SAJDA);
        assertThat(testAyahs.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAyahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAyahsWithPatch() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);

        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();

        // Update the ayahs using partial update
        Ayahs partialUpdatedAyahs = new Ayahs();
        partialUpdatedAyahs.setId(ayahs.getId());

        partialUpdatedAyahs
            .number(UPDATED_NUMBER)
            .textdesc(UPDATED_TEXTDESC)
            .numberInSurah(UPDATED_NUMBER_IN_SURAH)
            .page(UPDATED_PAGE)
            .surahId(UPDATED_SURAH_ID)
            .hizbId(UPDATED_HIZB_ID)
            .juzId(UPDATED_JUZ_ID)
            .sajda(UPDATED_SAJDA)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAyahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAyahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAyahs))
            )
            .andExpect(status().isOk());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        Ayahs testAyahs = ayahsList.get(ayahsList.size() - 1);
        assertThat(testAyahs.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAyahs.getTextdesc()).isEqualTo(UPDATED_TEXTDESC);
        assertThat(testAyahs.getNumberInSurah()).isEqualTo(UPDATED_NUMBER_IN_SURAH);
        assertThat(testAyahs.getPage()).isEqualTo(UPDATED_PAGE);
        assertThat(testAyahs.getSurahId()).isEqualTo(UPDATED_SURAH_ID);
        assertThat(testAyahs.getHizbId()).isEqualTo(UPDATED_HIZB_ID);
        assertThat(testAyahs.getJuzId()).isEqualTo(UPDATED_JUZ_ID);
        assertThat(testAyahs.getSajda()).isEqualTo(UPDATED_SAJDA);
        assertThat(testAyahs.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAyahs.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ayahs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ayahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ayahs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAyahs() throws Exception {
        int databaseSizeBeforeUpdate = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        ayahs.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAyahsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ayahs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ayahs in the database
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAyahs() throws Exception {
        // Initialize the database
        ayahsRepository.saveAndFlush(ayahs);
        ayahsRepository.save(ayahs);
        ayahsSearchRepository.save(ayahs);

        int databaseSizeBeforeDelete = ayahsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ayahs
        restAyahsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ayahs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ayahs> ayahsList = ayahsRepository.findAll();
        assertThat(ayahsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ayahsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAyahs() throws Exception {
        // Initialize the database
        ayahs = ayahsRepository.saveAndFlush(ayahs);
        ayahsSearchRepository.save(ayahs);

        // Search the ayahs
        restAyahsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ayahs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ayahs.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].textdesc").value(hasItem(DEFAULT_TEXTDESC)))
            .andExpect(jsonPath("$.[*].numberInSurah").value(hasItem(DEFAULT_NUMBER_IN_SURAH)))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE)))
            .andExpect(jsonPath("$.[*].surahId").value(hasItem(DEFAULT_SURAH_ID)))
            .andExpect(jsonPath("$.[*].hizbId").value(hasItem(DEFAULT_HIZB_ID)))
            .andExpect(jsonPath("$.[*].juzId").value(hasItem(DEFAULT_JUZ_ID)))
            .andExpect(jsonPath("$.[*].sajda").value(hasItem(DEFAULT_SAJDA.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
