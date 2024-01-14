package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Certificate;
import com.wiam.lms.domain.enumeration.CertificateType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.repository.CertificateRepository;
import com.wiam.lms.repository.search.CertificateSearchRepository;
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
 * Integration tests for the {@link CertificateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CertificateResourceIT {

    private static final CertificateType DEFAULT_CERTIFICATE_TYPE = CertificateType.HIFDH;
    private static final CertificateType UPDATED_CERTIFICATE_TYPE = CertificateType.TAJWID;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Riwayats DEFAULT_RIWAYA = Riwayats.WARSHS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_VIA_AL_SHATIBIYYAH;
    private static final Riwayats UPDATED_RIWAYA = Riwayats.QALOUNS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_ON_THE_AUTHORITY_OF_AL_SHATIBIYYAH;

    private static final Integer DEFAULT_MIQDAR = 1;
    private static final Integer UPDATED_MIQDAR = 2;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/certificates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/certificates/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CertificateRepository certificateRepository;

    @Mock
    private CertificateRepository certificateRepositoryMock;

    @Autowired
    private CertificateSearchRepository certificateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCertificateMockMvc;

    private Certificate certificate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .certificateType(DEFAULT_CERTIFICATE_TYPE)
            .title(DEFAULT_TITLE)
            .riwaya(DEFAULT_RIWAYA)
            .miqdar(DEFAULT_MIQDAR)
            .observation(DEFAULT_OBSERVATION);
        return certificate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificate createUpdatedEntity(EntityManager em) {
        Certificate certificate = new Certificate()
            .certificateType(UPDATED_CERTIFICATE_TYPE)
            .title(UPDATED_TITLE)
            .riwaya(UPDATED_RIWAYA)
            .miqdar(UPDATED_MIQDAR)
            .observation(UPDATED_OBSERVATION);
        return certificate;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        certificateSearchRepository.deleteAll();
        assertThat(certificateSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        certificate = createEntity(em);
    }

    @Test
    @Transactional
    void createCertificate() throws Exception {
        int databaseSizeBeforeCreate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        // Create the Certificate
        restCertificateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isCreated());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateType()).isEqualTo(DEFAULT_CERTIFICATE_TYPE);
        assertThat(testCertificate.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCertificate.getRiwaya()).isEqualTo(DEFAULT_RIWAYA);
        assertThat(testCertificate.getMiqdar()).isEqualTo(DEFAULT_MIQDAR);
        assertThat(testCertificate.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createCertificateWithExistingId() throws Exception {
        // Create the Certificate with an existing ID
        certificate.setId(1L);

        int databaseSizeBeforeCreate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        // set the field null
        certificate.setTitle(null);

        // Create the Certificate, which fails.

        restCertificateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isBadRequest());

        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMiqdarIsRequired() throws Exception {
        int databaseSizeBeforeTest = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        // set the field null
        certificate.setMiqdar(null);

        // Create the Certificate, which fails.

        restCertificateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isBadRequest());

        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCertificates() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get all the certificateList
        restCertificateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].certificateType").value(hasItem(DEFAULT_CERTIFICATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].riwaya").value(hasItem(DEFAULT_RIWAYA.toString())))
            .andExpect(jsonPath("$.[*].miqdar").value(hasItem(DEFAULT_MIQDAR)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCertificatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(certificateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(certificateRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCertificatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(certificateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(certificateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        // Get the certificate
        restCertificateMockMvc
            .perform(get(ENTITY_API_URL_ID, certificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(certificate.getId().intValue()))
            .andExpect(jsonPath("$.certificateType").value(DEFAULT_CERTIFICATE_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.riwaya").value(DEFAULT_RIWAYA.toString()))
            .andExpect(jsonPath("$.miqdar").value(DEFAULT_MIQDAR))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCertificate() throws Exception {
        // Get the certificate
        restCertificateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        certificateSearchRepository.save(certificate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());

        // Update the certificate
        Certificate updatedCertificate = certificateRepository.findById(certificate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCertificate are not directly saved in db
        em.detach(updatedCertificate);
        updatedCertificate
            .certificateType(UPDATED_CERTIFICATE_TYPE)
            .title(UPDATED_TITLE)
            .riwaya(UPDATED_RIWAYA)
            .miqdar(UPDATED_MIQDAR)
            .observation(UPDATED_OBSERVATION);

        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCertificate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCertificate))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateType()).isEqualTo(UPDATED_CERTIFICATE_TYPE);
        assertThat(testCertificate.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCertificate.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
        assertThat(testCertificate.getMiqdar()).isEqualTo(UPDATED_MIQDAR);
        assertThat(testCertificate.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Certificate> certificateSearchList = IterableUtils.toList(certificateSearchRepository.findAll());
                Certificate testCertificateSearch = certificateSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCertificateSearch.getCertificateType()).isEqualTo(UPDATED_CERTIFICATE_TYPE);
                assertThat(testCertificateSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testCertificateSearch.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
                assertThat(testCertificateSearch.getMiqdar()).isEqualTo(UPDATED_MIQDAR);
                assertThat(testCertificateSearch.getObservation()).isEqualTo(UPDATED_OBSERVATION);
            });
    }

    @Test
    @Transactional
    void putNonExistingCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate.title(UPDATED_TITLE).observation(UPDATED_OBSERVATION);

        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificate))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateType()).isEqualTo(DEFAULT_CERTIFICATE_TYPE);
        assertThat(testCertificate.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCertificate.getRiwaya()).isEqualTo(DEFAULT_RIWAYA);
        assertThat(testCertificate.getMiqdar()).isEqualTo(DEFAULT_MIQDAR);
        assertThat(testCertificate.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateCertificateWithPatch() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);

        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();

        // Update the certificate using partial update
        Certificate partialUpdatedCertificate = new Certificate();
        partialUpdatedCertificate.setId(certificate.getId());

        partialUpdatedCertificate
            .certificateType(UPDATED_CERTIFICATE_TYPE)
            .title(UPDATED_TITLE)
            .riwaya(UPDATED_RIWAYA)
            .miqdar(UPDATED_MIQDAR)
            .observation(UPDATED_OBSERVATION);

        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificate))
            )
            .andExpect(status().isOk());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        Certificate testCertificate = certificateList.get(certificateList.size() - 1);
        assertThat(testCertificate.getCertificateType()).isEqualTo(UPDATED_CERTIFICATE_TYPE);
        assertThat(testCertificate.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCertificate.getRiwaya()).isEqualTo(UPDATED_RIWAYA);
        assertThat(testCertificate.getMiqdar()).isEqualTo(UPDATED_MIQDAR);
        assertThat(testCertificate.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, certificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCertificate() throws Exception {
        int databaseSizeBeforeUpdate = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        certificate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(certificate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificate in the database
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCertificate() throws Exception {
        // Initialize the database
        certificateRepository.saveAndFlush(certificate);
        certificateRepository.save(certificate);
        certificateSearchRepository.save(certificate);

        int databaseSizeBeforeDelete = certificateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the certificate
        restCertificateMockMvc
            .perform(delete(ENTITY_API_URL_ID, certificate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Certificate> certificateList = certificateRepository.findAll();
        assertThat(certificateList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(certificateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCertificate() throws Exception {
        // Initialize the database
        certificate = certificateRepository.saveAndFlush(certificate);
        certificateSearchRepository.save(certificate);

        // Search the certificate
        restCertificateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + certificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].certificateType").value(hasItem(DEFAULT_CERTIFICATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].riwaya").value(hasItem(DEFAULT_RIWAYA.toString())))
            .andExpect(jsonPath("$.[*].miqdar").value(hasItem(DEFAULT_MIQDAR)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }
}
