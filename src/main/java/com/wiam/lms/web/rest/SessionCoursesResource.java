package com.wiam.lms.web.rest;

import com.wiam.lms.domain.SessionCourses;
import com.wiam.lms.repository.SessionCoursesRepository;
import com.wiam.lms.repository.search.SessionCoursesSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.SessionCourses}.
 */
@RestController
@RequestMapping("/api/session-courses")
@Transactional
public class SessionCoursesResource {

    private final Logger log = LoggerFactory.getLogger(SessionCoursesResource.class);

    private static final String ENTITY_NAME = "sessionCourses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionCoursesRepository sessionCoursesRepository;

    private final SessionCoursesSearchRepository sessionCoursesSearchRepository;

    public SessionCoursesResource(
        SessionCoursesRepository sessionCoursesRepository,
        SessionCoursesSearchRepository sessionCoursesSearchRepository
    ) {
        this.sessionCoursesRepository = sessionCoursesRepository;
        this.sessionCoursesSearchRepository = sessionCoursesSearchRepository;
    }

    /**
     * {@code POST  /session-courses} : Create a new sessionCourses.
     *
     * @param sessionCourses the sessionCourses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionCourses, or with status {@code 400 (Bad Request)} if the sessionCourses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SessionCourses> createSessionCourses(@Valid @RequestBody SessionCourses sessionCourses)
        throws URISyntaxException {
        log.debug("REST request to save SessionCourses : {}", sessionCourses);
        if (sessionCourses.getId() != null) {
            throw new BadRequestAlertException("A new sessionCourses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionCourses result = sessionCoursesRepository.save(sessionCourses);
        sessionCoursesSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/session-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-courses/:id} : Updates an existing sessionCourses.
     *
     * @param id the id of the sessionCourses to save.
     * @param sessionCourses the sessionCourses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionCourses,
     * or with status {@code 400 (Bad Request)} if the sessionCourses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionCourses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessionCourses> updateSessionCourses(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionCourses sessionCourses
    ) throws URISyntaxException {
        log.debug("REST request to update SessionCourses : {}, {}", id, sessionCourses);
        if (sessionCourses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionCourses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionCoursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionCourses result = sessionCoursesRepository.save(sessionCourses);
        sessionCoursesSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionCourses.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-courses/:id} : Partial updates given fields of an existing sessionCourses, field will ignore if it is null
     *
     * @param id the id of the sessionCourses to save.
     * @param sessionCourses the sessionCourses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionCourses,
     * or with status {@code 400 (Bad Request)} if the sessionCourses is not valid,
     * or with status {@code 404 (Not Found)} if the sessionCourses is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionCourses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionCourses> partialUpdateSessionCourses(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionCourses sessionCourses
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionCourses partially : {}, {}", id, sessionCourses);
        if (sessionCourses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionCourses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionCoursesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionCourses> result = sessionCoursesRepository
            .findById(sessionCourses.getId())
            .map(existingSessionCourses -> {
                if (sessionCourses.getTitle() != null) {
                    existingSessionCourses.setTitle(sessionCourses.getTitle());
                }
                if (sessionCourses.getDescription() != null) {
                    existingSessionCourses.setDescription(sessionCourses.getDescription());
                }
                if (sessionCourses.getResourceLink() != null) {
                    existingSessionCourses.setResourceLink(sessionCourses.getResourceLink());
                }
                if (sessionCourses.getResourceFile() != null) {
                    existingSessionCourses.setResourceFile(sessionCourses.getResourceFile());
                }
                if (sessionCourses.getResourceFileContentType() != null) {
                    existingSessionCourses.setResourceFileContentType(sessionCourses.getResourceFileContentType());
                }

                return existingSessionCourses;
            })
            .map(sessionCoursesRepository::save)
            .map(savedSessionCourses -> {
                sessionCoursesSearchRepository.index(savedSessionCourses);
                return savedSessionCourses;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionCourses.getId().toString())
        );
    }

    /**
     * {@code GET  /session-courses} : get all the sessionCourses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionCourses in body.
     */
    @GetMapping("")
    public List<SessionCourses> getAllSessionCourses() {
        log.debug("REST request to get all SessionCourses");
        return sessionCoursesRepository.findAll();
    }

    /**
     * {@code GET  /session-courses/:id} : get the "id" sessionCourses.
     *
     * @param id the id of the sessionCourses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionCourses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionCourses> getSessionCourses(@PathVariable("id") Long id) {
        log.debug("REST request to get SessionCourses : {}", id);
        Optional<SessionCourses> sessionCourses = sessionCoursesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sessionCourses);
    }

    /**
     * {@code DELETE  /session-courses/:id} : delete the "id" sessionCourses.
     *
     * @param id the id of the sessionCourses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionCourses(@PathVariable("id") Long id) {
        log.debug("REST request to delete SessionCourses : {}", id);
        sessionCoursesRepository.deleteById(id);
        sessionCoursesSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /session-courses/_search?query=:query} : search for the sessionCourses corresponding
     * to the query.
     *
     * @param query the query of the sessionCourses search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SessionCourses> searchSessionCourses(@RequestParam("query") String query) {
        log.debug("REST request to search SessionCourses for query {}", query);
        try {
            return StreamSupport.stream(sessionCoursesSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
