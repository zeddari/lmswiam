package com.wiam.lms.web.rest;

import com.wiam.lms.domain.QuizResult;
import com.wiam.lms.repository.QuizResultRepository;
import com.wiam.lms.repository.search.QuizResultSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wiam.lms.domain.QuizResult}.
 */
@RestController
@RequestMapping("/api/quiz-results")
@Transactional
public class QuizResultResource {

    private final Logger log = LoggerFactory.getLogger(QuizResultResource.class);

    private static final String ENTITY_NAME = "quizResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizResultRepository quizResultRepository;

    private final QuizResultSearchRepository quizResultSearchRepository;

    public QuizResultResource(QuizResultRepository quizResultRepository, QuizResultSearchRepository quizResultSearchRepository) {
        this.quizResultRepository = quizResultRepository;
        this.quizResultSearchRepository = quizResultSearchRepository;
    }

    /**
     * {@code POST  /quiz-results} : Create a new quizResult.
     *
     * @param quizResult the quizResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizResult, or with status {@code 400 (Bad Request)} if the quizResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizResult> createQuizResult(@Valid @RequestBody QuizResult quizResult) throws URISyntaxException {
        log.debug("REST request to save QuizResult : {}", quizResult);
        if (quizResult.getId() != null) {
            throw new BadRequestAlertException("A new quizResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizResult result = quizResultRepository.save(quizResult);
        quizResultSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/quiz-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quiz-results/:id} : Updates an existing quizResult.
     *
     * @param id the id of the quizResult to save.
     * @param quizResult the quizResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizResult,
     * or with status {@code 400 (Bad Request)} if the quizResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizResult> updateQuizResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuizResult quizResult
    ) throws URISyntaxException {
        log.debug("REST request to update QuizResult : {}, {}", id, quizResult);
        if (quizResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizResult result = quizResultRepository.save(quizResult);
        quizResultSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quiz-results/:id} : Partial updates given fields of an existing quizResult, field will ignore if it is null
     *
     * @param id the id of the quizResult to save.
     * @param quizResult the quizResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizResult,
     * or with status {@code 400 (Bad Request)} if the quizResult is not valid,
     * or with status {@code 404 (Not Found)} if the quizResult is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizResult> partialUpdateQuizResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuizResult quizResult
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizResult partially : {}, {}", id, quizResult);
        if (quizResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizResult.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizResult> result = quizResultRepository
            .findById(quizResult.getId())
            .map(existingQuizResult -> {
                if (quizResult.getResult() != null) {
                    existingQuizResult.setResult(quizResult.getResult());
                }
                if (quizResult.getSubmittedAT() != null) {
                    existingQuizResult.setSubmittedAT(quizResult.getSubmittedAT());
                }

                return existingQuizResult;
            })
            .map(quizResultRepository::save)
            .map(savedQuizResult -> {
                quizResultSearchRepository.index(savedQuizResult);
                return savedQuizResult;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizResult.getId().toString())
        );
    }

    /**
     * {@code GET  /quiz-results} : get all the quizResults.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizResults in body.
     */
    @GetMapping("")
    public List<QuizResult> getAllQuizResults(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all QuizResults");
        if (eagerload) {
            return quizResultRepository.findAllWithEagerRelationships();
        } else {
            return quizResultRepository.findAll();
        }
    }

    /**
     * {@code GET  /quiz-results/:id} : get the "id" quizResult.
     *
     * @param id the id of the quizResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizResult> getQuizResult(@PathVariable("id") Long id) {
        log.debug("REST request to get QuizResult : {}", id);
        Optional<QuizResult> quizResult = quizResultRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(quizResult);
    }

    /**
     * {@code DELETE  /quiz-results/:id} : delete the "id" quizResult.
     *
     * @param id the id of the quizResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizResult(@PathVariable("id") Long id) {
        log.debug("REST request to delete QuizResult : {}", id);
        quizResultRepository.deleteById(id);
        quizResultSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /quiz-results/_search?query=:query} : search for the quizResult corresponding
     * to the query.
     *
     * @param query the query of the quizResult search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<QuizResult> searchQuizResults(@RequestParam("query") String query) {
        log.debug("REST request to search QuizResults for query {}", query);
        try {
            return StreamSupport.stream(quizResultSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
