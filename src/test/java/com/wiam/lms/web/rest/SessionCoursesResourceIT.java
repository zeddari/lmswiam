package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.SessionCourses;
import com.wiam.lms.repository.SessionCoursesRepository;
import com.wiam.lms.repository.search.SessionCoursesSearchRepository;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link SessionCoursesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionCoursesResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_LINK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_RESOURCE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RESOURCE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RESOURCE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RESOURCE_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/session-courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/session-courses/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionCoursesRepository sessionCoursesRepository;

    @Autowired
    private SessionCoursesSearchRepository sessionCoursesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionCoursesMockMvc;

    private SessionCourses sessionCourses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionCourses createEntity(EntityManager em) {
        SessionCourses sessionCourses = new SessionCourses()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .resourceLink(DEFAULT_RESOURCE_LINK)
            .resourceFile(DEFAULT_RESOURCE_FILE)
            .resourceFileContentType(DEFAULT_RESOURCE_FILE_CONTENT_TYPE);
        return sessionCourses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionCourses createUpdatedEntity(EntityManager em) {
        SessionCourses sessionCourses = new SessionCourses()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .resourceLink(UPDATED_RESOURCE_LINK)
            .resourceFile(UPDATED_RESOURCE_FILE)
            .resourceFileContentType(UPDATED_RESOURCE_FILE_CONTENT_TYPE);
        return sessionCourses;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        sessionCoursesSearchRepository.deleteAll();
        assertThat(sessionCoursesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        sessionCourses = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionCourses() throws Exception {
        int databaseSizeBeforeCreate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        // Create the SessionCourses
        restSessionCoursesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isCreated());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SessionCourses testSessionCourses = sessionCoursesList.get(sessionCoursesList.size() - 1);
        assertThat(testSessionCourses.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionCourses.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSessionCourses.getResourceLink()).isEqualTo(DEFAULT_RESOURCE_LINK);
        assertThat(testSessionCourses.getResourceFile()).isEqualTo(DEFAULT_RESOURCE_FILE);
        assertThat(testSessionCourses.getResourceFileContentType()).isEqualTo(DEFAULT_RESOURCE_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSessionCoursesWithExistingId() throws Exception {
        // Create the SessionCourses with an existing ID
        sessionCourses.setId(1L);

        int databaseSizeBeforeCreate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionCoursesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        // set the field null
        sessionCourses.setTitle(null);

        // Create the SessionCourses, which fails.

        restSessionCoursesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSessionCourses() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);

        // Get all the sessionCoursesList
        restSessionCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionCourses.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].resourceLink").value(hasItem(DEFAULT_RESOURCE_LINK)))
            .andExpect(jsonPath("$.[*].resourceFileContentType").value(hasItem(DEFAULT_RESOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].resourceFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_RESOURCE_FILE))));
    }

    @Test
    @Transactional
    void getSessionCourses() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);

        // Get the sessionCourses
        restSessionCoursesMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionCourses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionCourses.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.resourceLink").value(DEFAULT_RESOURCE_LINK))
            .andExpect(jsonPath("$.resourceFileContentType").value(DEFAULT_RESOURCE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.resourceFile").value(Base64.getEncoder().encodeToString(DEFAULT_RESOURCE_FILE)));
    }

    @Test
    @Transactional
    void getNonExistingSessionCourses() throws Exception {
        // Get the sessionCourses
        restSessionCoursesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionCourses() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);

        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        sessionCoursesSearchRepository.save(sessionCourses);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());

        // Update the sessionCourses
        SessionCourses updatedSessionCourses = sessionCoursesRepository.findById(sessionCourses.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSessionCourses are not directly saved in db
        em.detach(updatedSessionCourses);
        updatedSessionCourses
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .resourceLink(UPDATED_RESOURCE_LINK)
            .resourceFile(UPDATED_RESOURCE_FILE)
            .resourceFileContentType(UPDATED_RESOURCE_FILE_CONTENT_TYPE);

        restSessionCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSessionCourses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSessionCourses))
            )
            .andExpect(status().isOk());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        SessionCourses testSessionCourses = sessionCoursesList.get(sessionCoursesList.size() - 1);
        assertThat(testSessionCourses.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionCourses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSessionCourses.getResourceLink()).isEqualTo(UPDATED_RESOURCE_LINK);
        assertThat(testSessionCourses.getResourceFile()).isEqualTo(UPDATED_RESOURCE_FILE);
        assertThat(testSessionCourses.getResourceFileContentType()).isEqualTo(UPDATED_RESOURCE_FILE_CONTENT_TYPE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SessionCourses> sessionCoursesSearchList = IterableUtils.toList(sessionCoursesSearchRepository.findAll());
                SessionCourses testSessionCoursesSearch = sessionCoursesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSessionCoursesSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testSessionCoursesSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSessionCoursesSearch.getResourceLink()).isEqualTo(UPDATED_RESOURCE_LINK);
                assertThat(testSessionCoursesSearch.getResourceFile()).isEqualTo(UPDATED_RESOURCE_FILE);
                assertThat(testSessionCoursesSearch.getResourceFileContentType()).isEqualTo(UPDATED_RESOURCE_FILE_CONTENT_TYPE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionCourses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionCourses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSessionCoursesWithPatch() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);

        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();

        // Update the sessionCourses using partial update
        SessionCourses partialUpdatedSessionCourses = new SessionCourses();
        partialUpdatedSessionCourses.setId(sessionCourses.getId());

        partialUpdatedSessionCourses.resourceFile(UPDATED_RESOURCE_FILE).resourceFileContentType(UPDATED_RESOURCE_FILE_CONTENT_TYPE);

        restSessionCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionCourses))
            )
            .andExpect(status().isOk());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        SessionCourses testSessionCourses = sessionCoursesList.get(sessionCoursesList.size() - 1);
        assertThat(testSessionCourses.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSessionCourses.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSessionCourses.getResourceLink()).isEqualTo(DEFAULT_RESOURCE_LINK);
        assertThat(testSessionCourses.getResourceFile()).isEqualTo(UPDATED_RESOURCE_FILE);
        assertThat(testSessionCourses.getResourceFileContentType()).isEqualTo(UPDATED_RESOURCE_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSessionCoursesWithPatch() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);

        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();

        // Update the sessionCourses using partial update
        SessionCourses partialUpdatedSessionCourses = new SessionCourses();
        partialUpdatedSessionCourses.setId(sessionCourses.getId());

        partialUpdatedSessionCourses
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .resourceLink(UPDATED_RESOURCE_LINK)
            .resourceFile(UPDATED_RESOURCE_FILE)
            .resourceFileContentType(UPDATED_RESOURCE_FILE_CONTENT_TYPE);

        restSessionCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionCourses))
            )
            .andExpect(status().isOk());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        SessionCourses testSessionCourses = sessionCoursesList.get(sessionCoursesList.size() - 1);
        assertThat(testSessionCourses.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSessionCourses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSessionCourses.getResourceLink()).isEqualTo(UPDATED_RESOURCE_LINK);
        assertThat(testSessionCourses.getResourceFile()).isEqualTo(UPDATED_RESOURCE_FILE);
        assertThat(testSessionCourses.getResourceFileContentType()).isEqualTo(UPDATED_RESOURCE_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionCourses() throws Exception {
        int databaseSizeBeforeUpdate = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        sessionCourses.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sessionCourses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionCourses in the database
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSessionCourses() throws Exception {
        // Initialize the database
        sessionCoursesRepository.saveAndFlush(sessionCourses);
        sessionCoursesRepository.save(sessionCourses);
        sessionCoursesSearchRepository.save(sessionCourses);

        int databaseSizeBeforeDelete = sessionCoursesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the sessionCourses
        restSessionCoursesMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionCourses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionCourses> sessionCoursesList = sessionCoursesRepository.findAll();
        assertThat(sessionCoursesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sessionCoursesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSessionCourses() throws Exception {
        // Initialize the database
        sessionCourses = sessionCoursesRepository.saveAndFlush(sessionCourses);
        sessionCoursesSearchRepository.save(sessionCourses);

        // Search the sessionCourses
        restSessionCoursesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sessionCourses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionCourses.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].resourceLink").value(hasItem(DEFAULT_RESOURCE_LINK)))
            .andExpect(jsonPath("$.[*].resourceFileContentType").value(hasItem(DEFAULT_RESOURCE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].resourceFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_RESOURCE_FILE))));
    }
}
