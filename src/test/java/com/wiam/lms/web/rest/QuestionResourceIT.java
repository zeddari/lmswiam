package com.wiam.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wiam.lms.IntegrationTest;
import com.wiam.lms.domain.Question;
import com.wiam.lms.repository.QuestionRepository;
import com.wiam.lms.repository.search.QuestionSearchRepository;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_A_1 = "AAAAAAAAAA";
    private static final String UPDATED_A_1 = "BBBBBBBBBB";

    private static final Boolean DEFAULT_A_1_V = false;
    private static final Boolean UPDATED_A_1_V = true;

    private static final String DEFAULT_A_2 = "AAAAAAAAAA";
    private static final String UPDATED_A_2 = "BBBBBBBBBB";

    private static final Boolean DEFAULT_A_2_V = false;
    private static final Boolean UPDATED_A_2_V = true;

    private static final String DEFAULT_A_3 = "AAAAAAAAAA";
    private static final String UPDATED_A_3 = "BBBBBBBBBB";

    private static final Boolean DEFAULT_A_3_V = false;
    private static final Boolean UPDATED_A_3_V = true;

    private static final String DEFAULT_A_4 = "AAAAAAAAAA";
    private static final String UPDATED_A_4 = "BBBBBBBBBB";

    private static final Boolean DEFAULT_A_4_V = false;
    private static final Boolean UPDATED_A_4_V = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/questions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionRepository questionRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Autowired
    private QuestionSearchRepository questionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .question(DEFAULT_QUESTION)
            .details(DEFAULT_DETAILS)
            .a1(DEFAULT_A_1)
            .a1v(DEFAULT_A_1_V)
            .a2(DEFAULT_A_2)
            .a2v(DEFAULT_A_2_V)
            .a3(DEFAULT_A_3)
            .a3v(DEFAULT_A_3_V)
            .a4(DEFAULT_A_4)
            .a4v(DEFAULT_A_4_V)
            .isActive(DEFAULT_IS_ACTIVE);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .question(UPDATED_QUESTION)
            .details(UPDATED_DETAILS)
            .a1(UPDATED_A_1)
            .a1v(UPDATED_A_1_V)
            .a2(UPDATED_A_2)
            .a2v(UPDATED_A_2_V)
            .a3(UPDATED_A_3)
            .a3v(UPDATED_A_3_V)
            .a4(UPDATED_A_4)
            .a4v(UPDATED_A_4_V)
            .isActive(UPDATED_IS_ACTIVE);
        return question;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        questionSearchRepository.deleteAll();
        assertThat(questionSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        // Create the Question
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testQuestion.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testQuestion.geta1()).isEqualTo(DEFAULT_A_1);
        assertThat(testQuestion.geta1v()).isEqualTo(DEFAULT_A_1_V);
        assertThat(testQuestion.geta2()).isEqualTo(DEFAULT_A_2);
        assertThat(testQuestion.geta2v()).isEqualTo(DEFAULT_A_2_V);
        assertThat(testQuestion.geta3()).isEqualTo(DEFAULT_A_3);
        assertThat(testQuestion.geta3v()).isEqualTo(DEFAULT_A_3_V);
        assertThat(testQuestion.geta4()).isEqualTo(DEFAULT_A_4);
        assertThat(testQuestion.geta4v()).isEqualTo(DEFAULT_A_4_V);
        assertThat(testQuestion.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);

        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checka1vIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        // set the field null
        question.seta1v(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checka2vIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        // set the field null
        question.seta2v(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        // set the field null
        question.setIsActive(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].a1").value(hasItem(DEFAULT_A_1.toString())))
            .andExpect(jsonPath("$.[*].a1v").value(hasItem(DEFAULT_A_1_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a2").value(hasItem(DEFAULT_A_2.toString())))
            .andExpect(jsonPath("$.[*].a2v").value(hasItem(DEFAULT_A_2_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a3").value(hasItem(DEFAULT_A_3.toString())))
            .andExpect(jsonPath("$.[*].a3v").value(hasItem(DEFAULT_A_3_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a4").value(hasItem(DEFAULT_A_4.toString())))
            .andExpect(jsonPath("$.[*].a4v").value(hasItem(DEFAULT_A_4_V.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(questionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.a1").value(DEFAULT_A_1.toString()))
            .andExpect(jsonPath("$.a1v").value(DEFAULT_A_1_V.booleanValue()))
            .andExpect(jsonPath("$.a2").value(DEFAULT_A_2.toString()))
            .andExpect(jsonPath("$.a2v").value(DEFAULT_A_2_V.booleanValue()))
            .andExpect(jsonPath("$.a3").value(DEFAULT_A_3.toString()))
            .andExpect(jsonPath("$.a3v").value(DEFAULT_A_3_V.booleanValue()))
            .andExpect(jsonPath("$.a4").value(DEFAULT_A_4.toString()))
            .andExpect(jsonPath("$.a4v").value(DEFAULT_A_4_V.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        questionSearchRepository.save(question);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .question(UPDATED_QUESTION)
            .details(UPDATED_DETAILS)
            .a1(UPDATED_A_1)
            .a1v(UPDATED_A_1_V)
            .a2(UPDATED_A_2)
            .a2v(UPDATED_A_2_V)
            .a3(UPDATED_A_3)
            .a3v(UPDATED_A_3_V)
            .a4(UPDATED_A_4)
            .a4v(UPDATED_A_4_V)
            .isActive(UPDATED_IS_ACTIVE);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestion.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testQuestion.geta1()).isEqualTo(UPDATED_A_1);
        assertThat(testQuestion.geta1v()).isEqualTo(UPDATED_A_1_V);
        assertThat(testQuestion.geta2()).isEqualTo(UPDATED_A_2);
        assertThat(testQuestion.geta2v()).isEqualTo(UPDATED_A_2_V);
        assertThat(testQuestion.geta3()).isEqualTo(UPDATED_A_3);
        assertThat(testQuestion.geta3v()).isEqualTo(UPDATED_A_3_V);
        assertThat(testQuestion.geta4()).isEqualTo(UPDATED_A_4);
        assertThat(testQuestion.geta4v()).isEqualTo(UPDATED_A_4_V);
        assertThat(testQuestion.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Question> questionSearchList = IterableUtils.toList(questionSearchRepository.findAll());
                Question testQuestionSearch = questionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testQuestionSearch.getQuestion()).isEqualTo(UPDATED_QUESTION);
                assertThat(testQuestionSearch.getDetails()).isEqualTo(UPDATED_DETAILS);
                assertThat(testQuestionSearch.geta1()).isEqualTo(UPDATED_A_1);
                assertThat(testQuestionSearch.geta1v()).isEqualTo(UPDATED_A_1_V);
                assertThat(testQuestionSearch.geta2()).isEqualTo(UPDATED_A_2);
                assertThat(testQuestionSearch.geta2v()).isEqualTo(UPDATED_A_2_V);
                assertThat(testQuestionSearch.geta3()).isEqualTo(UPDATED_A_3);
                assertThat(testQuestionSearch.geta3v()).isEqualTo(UPDATED_A_3_V);
                assertThat(testQuestionSearch.geta4()).isEqualTo(UPDATED_A_4);
                assertThat(testQuestionSearch.geta4v()).isEqualTo(UPDATED_A_4_V);
                assertThat(testQuestionSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, question.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.question(UPDATED_QUESTION).a1v(UPDATED_A_1_V).a2v(UPDATED_A_2_V).a4(UPDATED_A_4);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestion.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testQuestion.geta1()).isEqualTo(DEFAULT_A_1);
        assertThat(testQuestion.geta1v()).isEqualTo(UPDATED_A_1_V);
        assertThat(testQuestion.geta2()).isEqualTo(DEFAULT_A_2);
        assertThat(testQuestion.geta2v()).isEqualTo(UPDATED_A_2_V);
        assertThat(testQuestion.geta3()).isEqualTo(DEFAULT_A_3);
        assertThat(testQuestion.geta3v()).isEqualTo(DEFAULT_A_3_V);
        assertThat(testQuestion.geta4()).isEqualTo(UPDATED_A_4);
        assertThat(testQuestion.geta4v()).isEqualTo(DEFAULT_A_4_V);
        assertThat(testQuestion.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .question(UPDATED_QUESTION)
            .details(UPDATED_DETAILS)
            .a1(UPDATED_A_1)
            .a1v(UPDATED_A_1_V)
            .a2(UPDATED_A_2)
            .a2v(UPDATED_A_2_V)
            .a3(UPDATED_A_3)
            .a3v(UPDATED_A_3_V)
            .a4(UPDATED_A_4)
            .a4v(UPDATED_A_4_V)
            .isActive(UPDATED_IS_ACTIVE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testQuestion.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testQuestion.geta1()).isEqualTo(UPDATED_A_1);
        assertThat(testQuestion.geta1v()).isEqualTo(UPDATED_A_1_V);
        assertThat(testQuestion.geta2()).isEqualTo(UPDATED_A_2);
        assertThat(testQuestion.geta2v()).isEqualTo(UPDATED_A_2_V);
        assertThat(testQuestion.geta3()).isEqualTo(UPDATED_A_3);
        assertThat(testQuestion.geta3v()).isEqualTo(UPDATED_A_3_V);
        assertThat(testQuestion.geta4()).isEqualTo(UPDATED_A_4);
        assertThat(testQuestion.geta4v()).isEqualTo(UPDATED_A_4_V);
        assertThat(testQuestion.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, question.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        question.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        questionRepository.save(question);
        questionSearchRepository.save(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchQuestion() throws Exception {
        // Initialize the database
        question = questionRepository.saveAndFlush(question);
        questionSearchRepository.save(question);

        // Search the question
        restQuestionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].a1").value(hasItem(DEFAULT_A_1.toString())))
            .andExpect(jsonPath("$.[*].a1v").value(hasItem(DEFAULT_A_1_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a2").value(hasItem(DEFAULT_A_2.toString())))
            .andExpect(jsonPath("$.[*].a2v").value(hasItem(DEFAULT_A_2_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a3").value(hasItem(DEFAULT_A_3.toString())))
            .andExpect(jsonPath("$.[*].a3v").value(hasItem(DEFAULT_A_3_V.booleanValue())))
            .andExpect(jsonPath("$.[*].a4").value(hasItem(DEFAULT_A_4.toString())))
            .andExpect(jsonPath("$.[*].a4v").value(hasItem(DEFAULT_A_4_V.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
