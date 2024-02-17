package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Editions;
import com.wiam.lms.repository.EditionsRepository;
import com.wiam.lms.repository.search.EditionsSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.wiam.lms.domain.Editions}.
 */
@RestController
@RequestMapping("/api/editions")
@Transactional
public class EditionsResource {

    private final Logger log = LoggerFactory.getLogger(EditionsResource.class);

    private static final String ENTITY_NAME = "editions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EditionsRepository editionsRepository;

    private final EditionsSearchRepository editionsSearchRepository;

    public EditionsResource(EditionsRepository editionsRepository, EditionsSearchRepository editionsSearchRepository) {
        this.editionsRepository = editionsRepository;
        this.editionsSearchRepository = editionsSearchRepository;
    }

    /**
     * {@code POST  /editions} : Create a new editions.
     *
     * @param editions the editions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new editions, or with status {@code 400 (Bad Request)} if the editions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Editions> createEditions(@RequestBody Editions editions) throws URISyntaxException {
        log.debug("REST request to save Editions : {}", editions);
        if (editions.getId() != null) {
            throw new BadRequestAlertException("A new editions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Editions result = editionsRepository.save(editions);
        editionsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/editions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /editions/:id} : Updates an existing editions.
     *
     * @param id the id of the editions to save.
     * @param editions the editions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editions,
     * or with status {@code 400 (Bad Request)} if the editions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the editions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Editions> updateEditions(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Editions editions
    ) throws URISyntaxException {
        log.debug("REST request to update Editions : {}, {}", id, editions);
        if (editions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Editions result = editionsRepository.save(editions);
        editionsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editions.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /editions/:id} : Partial updates given fields of an existing editions, field will ignore if it is null
     *
     * @param id the id of the editions to save.
     * @param editions the editions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editions,
     * or with status {@code 400 (Bad Request)} if the editions is not valid,
     * or with status {@code 404 (Not Found)} if the editions is not found,
     * or with status {@code 500 (Internal Server Error)} if the editions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Editions> partialUpdateEditions(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Editions editions
    ) throws URISyntaxException {
        log.debug("REST request to partial update Editions partially : {}, {}", id, editions);
        if (editions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Editions> result = editionsRepository
            .findById(editions.getId())
            .map(existingEditions -> {
                if (editions.getIdentifier() != null) {
                    existingEditions.setIdentifier(editions.getIdentifier());
                }
                if (editions.getLanguage() != null) {
                    existingEditions.setLanguage(editions.getLanguage());
                }
                if (editions.getName() != null) {
                    existingEditions.setName(editions.getName());
                }
                if (editions.getEnglishName() != null) {
                    existingEditions.setEnglishName(editions.getEnglishName());
                }
                if (editions.getFormat() != null) {
                    existingEditions.setFormat(editions.getFormat());
                }
                if (editions.getType() != null) {
                    existingEditions.setType(editions.getType());
                }
                if (editions.getCreatedAt() != null) {
                    existingEditions.setCreatedAt(editions.getCreatedAt());
                }
                if (editions.getUpdatedAt() != null) {
                    existingEditions.setUpdatedAt(editions.getUpdatedAt());
                }

                return existingEditions;
            })
            .map(editionsRepository::save)
            .map(savedEditions -> {
                editionsSearchRepository.index(savedEditions);
                return savedEditions;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editions.getId().toString())
        );
    }

    /**
     * {@code GET  /editions} : get all the editions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of editions in body.
     */
    @GetMapping("")
    public List<Editions> getAllEditions() {
        log.debug("REST request to get all Editions");
        return editionsRepository.findAll();
    }

    /**
     * {@code GET  /editions/:id} : get the "id" editions.
     *
     * @param id the id of the editions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the editions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Editions> getEditions(@PathVariable Integer id) {
        log.debug("REST request to get Editions : {}", id);
        Optional<Editions> editions = editionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(editions);
    }

    /**
     * {@code DELETE  /editions/:id} : delete the "id" editions.
     *
     * @param id the id of the editions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditions(@PathVariable Integer id) {
        log.debug("REST request to delete Editions : {}", id);
        editionsRepository.deleteById(id);
        editionsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /editions/_search?query=:query} : search for the editions corresponding
     * to the query.
     *
     * @param query the query of the editions search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Editions> searchEditions(@RequestParam String query) {
        log.debug("REST request to search Editions for query {}", query);
        try {
            return StreamSupport.stream(editionsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
