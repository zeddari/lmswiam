package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Editions;
import com.wiam.lms.repository.EditionsRepository;
import com.wiam.lms.repository.search.EditionsSearchRepository;
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
 * Integration tests for the {@link EditionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EditionsResourceIT {

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/editions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/editions/_search";

    private static Random random = new Random();
    private static AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    @Autowired
    private EditionsRepository editionsRepository;

    @Autowired
    private EditionsSearchRepository editionsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEditionsMockMvc;

    private Editions editions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Editions createEntity(EntityManager em) {
        Editions editions = new Editions()
            .identifier(DEFAULT_IDENTIFIER)
            .language(DEFAULT_LANGUAGE)
            .name(DEFAULT_NAME)
            .englishName(DEFAULT_ENGLISH_NAME)
            .format(DEFAULT_FORMAT)
            .type(DEFAULT_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return editions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Editions createUpdatedEntity(EntityManager em) {
        Editions editions = new Editions()
            .identifier(UPDATED_IDENTIFIER)
            .language(UPDATED_LANGUAGE)
            .name(UPDATED_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .format(UPDATED_FORMAT)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return editions;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        editionsSearchRepository.deleteAll();
        assertThat(editionsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        editions = createEntity(em);
    }

    @Test
    @Transactional
    void createEditions() throws Exception {
        int databaseSizeBeforeCreate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        // Create the Editions
        restEditionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editions)))
            .andExpect(status().isCreated());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Editions testEditions = editionsList.get(editionsList.size() - 1);
        assertThat(testEditions.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testEditions.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testEditions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEditions.getEnglishName()).isEqualTo(DEFAULT_ENGLISH_NAME);
        assertThat(testEditions.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testEditions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEditions.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEditions.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEditionsWithExistingId() throws Exception {
        // Create the Editions with an existing ID
        editions.setId(1);

        int databaseSizeBeforeCreate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEditionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editions)))
            .andExpect(status().isBadRequest());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEditions() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);

        // Get all the editionsList
        restEditionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(editions.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getEditions() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);

        // Get the editions
        restEditionsMockMvc
            .perform(get(ENTITY_API_URL_ID, editions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(editions.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.englishName").value(DEFAULT_ENGLISH_NAME.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingEditions() throws Exception {
        // Get the editions
        restEditionsMockMvc.perform(get(ENTITY_API_URL_ID, Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEditions() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);

        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        editionsSearchRepository.save(editions);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());

        // Update the editions
        Editions updatedEditions = editionsRepository.findById(editions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEditions are not directly saved in db
        em.detach(updatedEditions);
        updatedEditions
            .identifier(UPDATED_IDENTIFIER)
            .language(UPDATED_LANGUAGE)
            .name(UPDATED_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .format(UPDATED_FORMAT)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEditionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEditions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEditions))
            )
            .andExpect(status().isOk());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        Editions testEditions = editionsList.get(editionsList.size() - 1);
        assertThat(testEditions.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testEditions.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testEditions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEditions.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testEditions.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testEditions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEditions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEditions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Editions> editionsSearchList = IterableUtils.toList(editionsSearchRepository.findAll());
                Editions testEditionsSearch = editionsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEditionsSearch.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
                assertThat(testEditionsSearch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
                assertThat(testEditionsSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testEditionsSearch.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
                assertThat(testEditionsSearch.getFormat()).isEqualTo(UPDATED_FORMAT);
                assertThat(testEditionsSearch.getType()).isEqualTo(UPDATED_TYPE);
                assertThat(testEditionsSearch.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
                assertThat(testEditionsSearch.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
            });
    }

    @Test
    @Transactional
    void putNonExistingEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, editions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(editions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(editions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEditionsWithPatch() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);

        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();

        // Update the editions using partial update
        Editions partialUpdatedEditions = new Editions();
        partialUpdatedEditions.setId(editions.getId());

        partialUpdatedEditions
            .identifier(UPDATED_IDENTIFIER)
            .name(UPDATED_NAME)
            .format(UPDATED_FORMAT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEditionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEditions))
            )
            .andExpect(status().isOk());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        Editions testEditions = editionsList.get(editionsList.size() - 1);
        assertThat(testEditions.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testEditions.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testEditions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEditions.getEnglishName()).isEqualTo(DEFAULT_ENGLISH_NAME);
        assertThat(testEditions.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testEditions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEditions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEditions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEditionsWithPatch() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);

        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();

        // Update the editions using partial update
        Editions partialUpdatedEditions = new Editions();
        partialUpdatedEditions.setId(editions.getId());

        partialUpdatedEditions
            .identifier(UPDATED_IDENTIFIER)
            .language(UPDATED_LANGUAGE)
            .name(UPDATED_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .format(UPDATED_FORMAT)
            .type(UPDATED_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEditionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEditions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEditions))
            )
            .andExpect(status().isOk());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        Editions testEditions = editionsList.get(editionsList.size() - 1);
        assertThat(testEditions.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testEditions.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testEditions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEditions.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testEditions.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testEditions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEditions.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEditions.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, editions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(editions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEditions() throws Exception {
        int databaseSizeBeforeUpdate = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        editions.setId(intCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEditionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(editions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Editions in the database
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEditions() throws Exception {
        // Initialize the database
        editionsRepository.saveAndFlush(editions);
        editionsRepository.save(editions);
        editionsSearchRepository.save(editions);

        int databaseSizeBeforeDelete = editionsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the editions
        restEditionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, editions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Editions> editionsList = editionsRepository.findAll();
        assertThat(editionsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(editionsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEditions() throws Exception {
        // Initialize the database
        editions = editionsRepository.saveAndFlush(editions);
        editionsSearchRepository.save(editions);

        // Search the editions
        restEditionsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + editions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(editions.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }
}
