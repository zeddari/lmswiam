package com.wiam.lms.web.rest;

import static com.wiam.lms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Payment;
import com.wiam.lms.domain.enumeration.PaymentMode;
import com.wiam.lms.domain.enumeration.PaymentSide;
import com.wiam.lms.domain.enumeration.PaymentType;
import com.wiam.lms.repository.PaymentRepository;
import com.wiam.lms.repository.search.PaymentSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final PaymentMode DEFAULT_PAYMENT_METHOD = PaymentMode.CASH;
    private static final PaymentMode UPDATED_PAYMENT_METHOD = PaymentMode.TRANSFER;

    private static final String DEFAULT_PAIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_PAIED_BY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROOF = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROOF = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROOF_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROOF_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_PAID_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAID_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final PaymentType DEFAULT_TYPE = PaymentType.REGISTER;
    private static final PaymentType UPDATED_TYPE = PaymentType.MONTHLY_FEES;

    private static final PaymentSide DEFAULT_SIDE = PaymentSide.IN;
    private static final PaymentSide UPDATED_SIDE = PaymentSide.OUT;

    private static final ZonedDateTime DEFAULT_VALIDITY_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDITY_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_VALIDITY_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VALIDITY_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Autowired
    private PaymentSearchRepository paymentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(DEFAULT_AMOUNT)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .paiedBy(DEFAULT_PAIED_BY)
            .proof(DEFAULT_PROOF)
            .proofContentType(DEFAULT_PROOF_CONTENT_TYPE)
            .paidAt(DEFAULT_PAID_AT)
            .type(DEFAULT_TYPE)
            .side(DEFAULT_SIDE)
            .validityStartTime(DEFAULT_VALIDITY_START_TIME)
            .validityEndTime(DEFAULT_VALIDITY_END_TIME)
            .details(DEFAULT_DETAILS);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paiedBy(UPDATED_PAIED_BY)
            .proof(UPDATED_PROOF)
            .proofContentType(UPDATED_PROOF_CONTENT_TYPE)
            .paidAt(UPDATED_PAID_AT)
            .type(UPDATED_TYPE)
            .side(UPDATED_SIDE)
            .validityStartTime(UPDATED_VALIDITY_START_TIME)
            .validityEndTime(UPDATED_VALIDITY_END_TIME)
            .details(UPDATED_DETAILS);
        return payment;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        paymentSearchRepository.deleteAll();
        assertThat(paymentSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // Create the Payment
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testPayment.getPaiedBy()).isEqualTo(DEFAULT_PAIED_BY);
        assertThat(testPayment.getProof()).isEqualTo(DEFAULT_PROOF);
        assertThat(testPayment.getProofContentType()).isEqualTo(DEFAULT_PROOF_CONTENT_TYPE);
        assertThat(testPayment.getPaidAt()).isEqualTo(DEFAULT_PAID_AT);
        assertThat(testPayment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPayment.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testPayment.getValidityStartTime()).isEqualTo(DEFAULT_VALIDITY_START_TIME);
        assertThat(testPayment.getValidityEndTime()).isEqualTo(DEFAULT_VALIDITY_END_TIME);
        assertThat(testPayment.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPaymentMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setPaymentMethod(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPaiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setPaiedBy(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPaidAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setPaidAt(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setType(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setSide(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValidityStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setValidityStartTime(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValidityEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        // set the field null
        payment.setValidityEndTime(null);

        // Create the Payment, which fails.

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paiedBy").value(hasItem(DEFAULT_PAIED_BY)))
            .andExpect(jsonPath("$.[*].proofContentType").value(hasItem(DEFAULT_PROOF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].proof").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROOF))))
            .andExpect(jsonPath("$.[*].paidAt").value(hasItem(sameInstant(DEFAULT_PAID_AT))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].validityStartTime").value(hasItem(sameInstant(DEFAULT_VALIDITY_START_TIME))))
            .andExpect(jsonPath("$.[*].validityEndTime").value(hasItem(sameInstant(DEFAULT_VALIDITY_END_TIME))))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paymentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.paiedBy").value(DEFAULT_PAIED_BY))
            .andExpect(jsonPath("$.proofContentType").value(DEFAULT_PROOF_CONTENT_TYPE))
            .andExpect(jsonPath("$.proof").value(Base64.getEncoder().encodeToString(DEFAULT_PROOF)))
            .andExpect(jsonPath("$.paidAt").value(sameInstant(DEFAULT_PAID_AT)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.validityStartTime").value(sameInstant(DEFAULT_VALIDITY_START_TIME)))
            .andExpect(jsonPath("$.validityEndTime").value(sameInstant(DEFAULT_VALIDITY_END_TIME)))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        paymentSearchRepository.save(payment);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paiedBy(UPDATED_PAIED_BY)
            .proof(UPDATED_PROOF)
            .proofContentType(UPDATED_PROOF_CONTENT_TYPE)
            .paidAt(UPDATED_PAID_AT)
            .type(UPDATED_TYPE)
            .side(UPDATED_SIDE)
            .validityStartTime(UPDATED_VALIDITY_START_TIME)
            .validityEndTime(UPDATED_VALIDITY_END_TIME)
            .details(UPDATED_DETAILS);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPayment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getPaiedBy()).isEqualTo(UPDATED_PAIED_BY);
        assertThat(testPayment.getProof()).isEqualTo(UPDATED_PROOF);
        assertThat(testPayment.getProofContentType()).isEqualTo(UPDATED_PROOF_CONTENT_TYPE);
        assertThat(testPayment.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPayment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPayment.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testPayment.getValidityStartTime()).isEqualTo(UPDATED_VALIDITY_START_TIME);
        assertThat(testPayment.getValidityEndTime()).isEqualTo(UPDATED_VALIDITY_END_TIME);
        assertThat(testPayment.getDetails()).isEqualTo(UPDATED_DETAILS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Payment> paymentSearchList = IterableUtils.toList(paymentSearchRepository.findAll());
                Payment testPaymentSearch = paymentSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPaymentSearch.getAmount()).isEqualTo(UPDATED_AMOUNT);
                assertThat(testPaymentSearch.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
                assertThat(testPaymentSearch.getPaiedBy()).isEqualTo(UPDATED_PAIED_BY);
                assertThat(testPaymentSearch.getProof()).isEqualTo(UPDATED_PROOF);
                assertThat(testPaymentSearch.getProofContentType()).isEqualTo(UPDATED_PROOF_CONTENT_TYPE);
                assertThat(testPaymentSearch.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
                assertThat(testPaymentSearch.getType()).isEqualTo(UPDATED_TYPE);
                assertThat(testPaymentSearch.getSide()).isEqualTo(UPDATED_SIDE);
                assertThat(testPaymentSearch.getValidityStartTime()).isEqualTo(UPDATED_VALIDITY_START_TIME);
                assertThat(testPaymentSearch.getValidityEndTime()).isEqualTo(UPDATED_VALIDITY_END_TIME);
                assertThat(testPaymentSearch.getDetails()).isEqualTo(UPDATED_DETAILS);
            });
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, payment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .proof(UPDATED_PROOF)
            .proofContentType(UPDATED_PROOF_CONTENT_TYPE)
            .side(UPDATED_SIDE)
            .details(UPDATED_DETAILS);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getPaiedBy()).isEqualTo(DEFAULT_PAIED_BY);
        assertThat(testPayment.getProof()).isEqualTo(UPDATED_PROOF);
        assertThat(testPayment.getProofContentType()).isEqualTo(UPDATED_PROOF_CONTENT_TYPE);
        assertThat(testPayment.getPaidAt()).isEqualTo(DEFAULT_PAID_AT);
        assertThat(testPayment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPayment.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testPayment.getValidityStartTime()).isEqualTo(DEFAULT_VALIDITY_START_TIME);
        assertThat(testPayment.getValidityEndTime()).isEqualTo(DEFAULT_VALIDITY_END_TIME);
        assertThat(testPayment.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .amount(UPDATED_AMOUNT)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .paiedBy(UPDATED_PAIED_BY)
            .proof(UPDATED_PROOF)
            .proofContentType(UPDATED_PROOF_CONTENT_TYPE)
            .paidAt(UPDATED_PAID_AT)
            .type(UPDATED_TYPE)
            .side(UPDATED_SIDE)
            .validityStartTime(UPDATED_VALIDITY_START_TIME)
            .validityEndTime(UPDATED_VALIDITY_END_TIME)
            .details(UPDATED_DETAILS);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testPayment.getPaiedBy()).isEqualTo(UPDATED_PAIED_BY);
        assertThat(testPayment.getProof()).isEqualTo(UPDATED_PROOF);
        assertThat(testPayment.getProofContentType()).isEqualTo(UPDATED_PROOF_CONTENT_TYPE);
        assertThat(testPayment.getPaidAt()).isEqualTo(UPDATED_PAID_AT);
        assertThat(testPayment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPayment.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testPayment.getValidityStartTime()).isEqualTo(UPDATED_VALIDITY_START_TIME);
        assertThat(testPayment.getValidityEndTime()).isEqualTo(UPDATED_VALIDITY_END_TIME);
        assertThat(testPayment.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, payment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(payment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        payment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(payment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        paymentRepository.save(payment);
        paymentSearchRepository.save(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPayment() throws Exception {
        // Initialize the database
        payment = paymentRepository.saveAndFlush(payment);
        paymentSearchRepository.save(payment);

        // Search the payment
        restPaymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].paiedBy").value(hasItem(DEFAULT_PAIED_BY)))
            .andExpect(jsonPath("$.[*].proofContentType").value(hasItem(DEFAULT_PROOF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].proof").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROOF))))
            .andExpect(jsonPath("$.[*].paidAt").value(hasItem(sameInstant(DEFAULT_PAID_AT))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].validityStartTime").value(hasItem(sameInstant(DEFAULT_VALIDITY_START_TIME))))
            .andExpect(jsonPath("$.[*].validityEndTime").value(hasItem(sameInstant(DEFAULT_VALIDITY_END_TIME))))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }
}
