package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.TypeProject;
import com.wiam.lms.repository.TypeProjectRepository;
import com.wiam.lms.repository.search.TypeProjectSearchRepository;
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
 * Integration tests for the {@link TypeProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeProjectResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_LAT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_LAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/type-projects/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeProjectRepository typeProjectRepository;

    @Autowired
    private TypeProjectSearchRepository typeProjectSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeProjectMockMvc;

    private TypeProject typeProject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeProject createEntity(EntityManager em) {
        TypeProject typeProject = new TypeProject().nameAr(DEFAULT_NAME_AR).nameLat(DEFAULT_NAME_LAT);
        return typeProject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeProject createUpdatedEntity(EntityManager em) {
        TypeProject typeProject = new TypeProject().nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);
        return typeProject;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        typeProjectSearchRepository.deleteAll();
        assertThat(typeProjectSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        typeProject = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeProject() throws Exception {
        int databaseSizeBeforeCreate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        // Create the TypeProject
        restTypeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeProject)))
            .andExpect(status().isCreated());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TypeProject testTypeProject = typeProjectList.get(typeProjectList.size() - 1);
        assertThat(testTypeProject.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testTypeProject.getNameLat()).isEqualTo(DEFAULT_NAME_LAT);
    }

    @Test
    @Transactional
    void createTypeProjectWithExistingId() throws Exception {
        // Create the TypeProject with an existing ID
        typeProject.setId(1L);

        int databaseSizeBeforeCreate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeProject)))
            .andExpect(status().isBadRequest());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameArIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        // set the field null
        typeProject.setNameAr(null);

        // Create the TypeProject, which fails.

        restTypeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeProject)))
            .andExpect(status().isBadRequest());

        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        // set the field null
        typeProject.setNameLat(null);

        // Create the TypeProject, which fails.

        restTypeProjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeProject)))
            .andExpect(status().isBadRequest());

        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTypeProjects() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);

        // Get all the typeProjectList
        restTypeProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameLat").value(hasItem(DEFAULT_NAME_LAT)));
    }

    @Test
    @Transactional
    void getTypeProject() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);

        // Get the typeProject
        restTypeProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, typeProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeProject.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameLat").value(DEFAULT_NAME_LAT));
    }

    @Test
    @Transactional
    void getNonExistingTypeProject() throws Exception {
        // Get the typeProject
        restTypeProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeProject() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);

        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        typeProjectSearchRepository.save(typeProject);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());

        // Update the typeProject
        TypeProject updatedTypeProject = typeProjectRepository.findById(typeProject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeProject are not directly saved in db
        em.detach(updatedTypeProject);
        updatedTypeProject.nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);

        restTypeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeProject))
            )
            .andExpect(status().isOk());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        TypeProject testTypeProject = typeProjectList.get(typeProjectList.size() - 1);
        assertThat(testTypeProject.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testTypeProject.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TypeProject> typeProjectSearchList = IterableUtils.toList(typeProjectSearchRepository.findAll());
                TypeProject testTypeProjectSearch = typeProjectSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTypeProjectSearch.getNameAr()).isEqualTo(UPDATED_NAME_AR);
                assertThat(testTypeProjectSearch.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
            });
    }

    @Test
    @Transactional
    void putNonExistingTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeProject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeProject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTypeProjectWithPatch() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);

        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();

        // Update the typeProject using partial update
        TypeProject partialUpdatedTypeProject = new TypeProject();
        partialUpdatedTypeProject.setId(typeProject.getId());

        restTypeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeProject))
            )
            .andExpect(status().isOk());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        TypeProject testTypeProject = typeProjectList.get(typeProjectList.size() - 1);
        assertThat(testTypeProject.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testTypeProject.getNameLat()).isEqualTo(DEFAULT_NAME_LAT);
    }

    @Test
    @Transactional
    void fullUpdateTypeProjectWithPatch() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);

        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();

        // Update the typeProject using partial update
        TypeProject partialUpdatedTypeProject = new TypeProject();
        partialUpdatedTypeProject.setId(typeProject.getId());

        partialUpdatedTypeProject.nameAr(UPDATED_NAME_AR).nameLat(UPDATED_NAME_LAT);

        restTypeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeProject))
            )
            .andExpect(status().isOk());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        TypeProject testTypeProject = typeProjectList.get(typeProjectList.size() - 1);
        assertThat(testTypeProject.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testTypeProject.getNameLat()).isEqualTo(UPDATED_NAME_LAT);
    }

    @Test
    @Transactional
    void patchNonExistingTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeProject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeProject))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeProject() throws Exception {
        int databaseSizeBeforeUpdate = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        typeProject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeProjectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeProject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeProject in the database
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTypeProject() throws Exception {
        // Initialize the database
        typeProjectRepository.saveAndFlush(typeProject);
        typeProjectRepository.save(typeProject);
        typeProjectSearchRepository.save(typeProject);

        int databaseSizeBeforeDelete = typeProjectRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the typeProject
        restTypeProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeProject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeProject> typeProjectList = typeProjectRepository.findAll();
        assertThat(typeProjectList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeProjectSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTypeProject() throws Exception {
        // Initialize the database
        typeProject = typeProjectRepository.saveAndFlush(typeProject);
        typeProjectSearchRepository.save(typeProject);

        // Search the typeProject
        restTypeProjectMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameLat").value(hasItem(DEFAULT_NAME_LAT)));
    }
}
