package com.wiam.lms.web.rest;

import com.wiam.lms.domain.StudentSponsoring;
import com.wiam.lms.repository.StudentSponsoringRepository;
import com.wiam.lms.repository.search.StudentSponsoringSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.StudentSponsoring}.
 */
@RestController
@RequestMapping("/api/student-sponsorings")
@Transactional
public class StudentSponsoringResource {

    private final Logger log = LoggerFactory.getLogger(StudentSponsoringResource.class);

    private static final String ENTITY_NAME = "studentSponsoring";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentSponsoringRepository studentSponsoringRepository;

    private final StudentSponsoringSearchRepository studentSponsoringSearchRepository;

    public StudentSponsoringResource(
        StudentSponsoringRepository studentSponsoringRepository,
        StudentSponsoringSearchRepository studentSponsoringSearchRepository
    ) {
        this.studentSponsoringRepository = studentSponsoringRepository;
        this.studentSponsoringSearchRepository = studentSponsoringSearchRepository;
    }

    /**
     * {@code POST  /student-sponsorings} : Create a new studentSponsoring.
     *
     * @param studentSponsoring the studentSponsoring to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentSponsoring, or with status {@code 400 (Bad Request)} if the studentSponsoring has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentSponsoring> createStudentSponsoring(@Valid @RequestBody StudentSponsoring studentSponsoring)
        throws URISyntaxException {
        log.debug("REST request to save StudentSponsoring : {}", studentSponsoring);
        if (studentSponsoring.getId() != null) {
            throw new BadRequestAlertException("A new studentSponsoring cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentSponsoring result = studentSponsoringRepository.save(studentSponsoring);
        studentSponsoringSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/student-sponsorings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-sponsorings/:id} : Updates an existing studentSponsoring.
     *
     * @param id the id of the studentSponsoring to save.
     * @param studentSponsoring the studentSponsoring to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentSponsoring,
     * or with status {@code 400 (Bad Request)} if the studentSponsoring is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentSponsoring couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentSponsoring> updateStudentSponsoring(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentSponsoring studentSponsoring
    ) throws URISyntaxException {
        log.debug("REST request to update StudentSponsoring : {}, {}", id, studentSponsoring);
        if (studentSponsoring.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentSponsoring.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentSponsoringRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentSponsoring result = studentSponsoringRepository.save(studentSponsoring);
        studentSponsoringSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentSponsoring.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-sponsorings/:id} : Partial updates given fields of an existing studentSponsoring, field will ignore if it is null
     *
     * @param id the id of the studentSponsoring to save.
     * @param studentSponsoring the studentSponsoring to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentSponsoring,
     * or with status {@code 400 (Bad Request)} if the studentSponsoring is not valid,
     * or with status {@code 404 (Not Found)} if the studentSponsoring is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentSponsoring couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentSponsoring> partialUpdateStudentSponsoring(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentSponsoring studentSponsoring
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentSponsoring partially : {}, {}", id, studentSponsoring);
        if (studentSponsoring.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentSponsoring.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentSponsoringRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentSponsoring> result = studentSponsoringRepository
            .findById(studentSponsoring.getId())
            .map(existingStudentSponsoring -> {
                if (studentSponsoring.getRef() != null) {
                    existingStudentSponsoring.setRef(studentSponsoring.getRef());
                }
                if (studentSponsoring.getMessage() != null) {
                    existingStudentSponsoring.setMessage(studentSponsoring.getMessage());
                }
                if (studentSponsoring.getAmount() != null) {
                    existingStudentSponsoring.setAmount(studentSponsoring.getAmount());
                }
                if (studentSponsoring.getStartDate() != null) {
                    existingStudentSponsoring.setStartDate(studentSponsoring.getStartDate());
                }
                if (studentSponsoring.getEndDate() != null) {
                    existingStudentSponsoring.setEndDate(studentSponsoring.getEndDate());
                }
                if (studentSponsoring.getIsAlways() != null) {
                    existingStudentSponsoring.setIsAlways(studentSponsoring.getIsAlways());
                }

                return existingStudentSponsoring;
            })
            .map(studentSponsoringRepository::save)
            .map(savedStudentSponsoring -> {
                studentSponsoringSearchRepository.index(savedStudentSponsoring);
                return savedStudentSponsoring;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentSponsoring.getId().toString())
        );
    }

    /**
     * {@code GET  /student-sponsorings} : get all the studentSponsorings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentSponsorings in body.
     */
    @GetMapping("")
    public List<StudentSponsoring> getAllStudentSponsorings() {
        log.debug("REST request to get all StudentSponsorings");
        return studentSponsoringRepository.findAll();
    }

    /**
     * {@code GET  /student-sponsorings/:id} : get the "id" studentSponsoring.
     *
     * @param id the id of the studentSponsoring to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentSponsoring, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentSponsoring> getStudentSponsoring(@PathVariable("id") Long id) {
        log.debug("REST request to get StudentSponsoring : {}", id);
        Optional<StudentSponsoring> studentSponsoring = studentSponsoringRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentSponsoring);
    }

    /**
     * {@code DELETE  /student-sponsorings/:id} : delete the "id" studentSponsoring.
     *
     * @param id the id of the studentSponsoring to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentSponsoring(@PathVariable("id") Long id) {
        log.debug("REST request to delete StudentSponsoring : {}", id);
        studentSponsoringRepository.deleteById(id);
        studentSponsoringSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /student-sponsorings/_search?query=:query} : search for the studentSponsoring corresponding
     * to the query.
     *
     * @param query the query of the studentSponsoring search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<StudentSponsoring> searchStudentSponsorings(@RequestParam("query") String query) {
        log.debug("REST request to search StudentSponsorings for query {}", query);
        try {
            return StreamSupport.stream(studentSponsoringSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
