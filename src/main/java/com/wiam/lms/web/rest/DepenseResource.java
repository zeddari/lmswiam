package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Depense;
import com.wiam.lms.repository.DepenseRepository;
import com.wiam.lms.repository.search.DepenseSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.Depense}.
 */
@RestController
@RequestMapping("/api/depenses")
@Transactional
public class DepenseResource {

    private final Logger log = LoggerFactory.getLogger(DepenseResource.class);

    private static final String ENTITY_NAME = "depense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepenseRepository depenseRepository;

    private final DepenseSearchRepository depenseSearchRepository;

    public DepenseResource(DepenseRepository depenseRepository, DepenseSearchRepository depenseSearchRepository) {
        this.depenseRepository = depenseRepository;
        this.depenseSearchRepository = depenseSearchRepository;
    }

    /**
     * {@code POST  /depenses} : Create a new depense.
     *
     * @param depense the depense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depense, or with status {@code 400 (Bad Request)} if the depense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Depense> createDepense(@Valid @RequestBody Depense depense) throws URISyntaxException {
        log.debug("REST request to save Depense : {}", depense);
        if (depense.getId() != null) {
            throw new BadRequestAlertException("A new depense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Depense result = depenseRepository.save(depense);
        depenseSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depenses/:id} : Updates an existing depense.
     *
     * @param id the id of the depense to save.
     * @param depense the depense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depense,
     * or with status {@code 400 (Bad Request)} if the depense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Depense depense
    ) throws URISyntaxException {
        log.debug("REST request to update Depense : {}, {}", id, depense);
        if (depense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Depense result = depenseRepository.save(depense);
        depenseSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depense.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depenses/:id} : Partial updates given fields of an existing depense, field will ignore if it is null
     *
     * @param id the id of the depense to save.
     * @param depense the depense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depense,
     * or with status {@code 400 (Bad Request)} if the depense is not valid,
     * or with status {@code 404 (Not Found)} if the depense is not found,
     * or with status {@code 500 (Internal Server Error)} if the depense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Depense> partialUpdateDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Depense depense
    ) throws URISyntaxException {
        log.debug("REST request to partial update Depense partially : {}, {}", id, depense);
        if (depense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Depense> result = depenseRepository
            .findById(depense.getId())
            .map(existingDepense -> {
                if (depense.getType() != null) {
                    existingDepense.setType(depense.getType());
                }
                if (depense.getTarget() != null) {
                    existingDepense.setTarget(depense.getTarget());
                }
                if (depense.getFrequency() != null) {
                    existingDepense.setFrequency(depense.getFrequency());
                }
                if (depense.getAmount() != null) {
                    existingDepense.setAmount(depense.getAmount());
                }
                if (depense.getRef() != null) {
                    existingDepense.setRef(depense.getRef());
                }
                if (depense.getMessage() != null) {
                    existingDepense.setMessage(depense.getMessage());
                }

                return existingDepense;
            })
            .map(depenseRepository::save)
            .map(savedDepense -> {
                depenseSearchRepository.index(savedDepense);
                return savedDepense;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depense.getId().toString())
        );
    }

    /**
     * {@code GET  /depenses} : get all the depenses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depenses in body.
     */
    @GetMapping("")
    public List<Depense> getAllDepenses(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Depenses");
        if (eagerload) {
            return depenseRepository.findAllWithEagerRelationships();
        } else {
            return depenseRepository.findAll();
        }
    }

    /**
     * {@code GET  /depenses/:id} : get the "id" depense.
     *
     * @param id the id of the depense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepense(@PathVariable("id") Long id) {
        log.debug("REST request to get Depense : {}", id);
        Optional<Depense> depense = depenseRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(depense);
    }

    /**
     * {@code DELETE  /depenses/:id} : delete the "id" depense.
     *
     * @param id the id of the depense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable("id") Long id) {
        log.debug("REST request to delete Depense : {}", id);
        depenseRepository.deleteById(id);
        depenseSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /depenses/_search?query=:query} : search for the depense corresponding
     * to the query.
     *
     * @param query the query of the depense search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Depense> searchDepenses(@RequestParam("query") String query) {
        log.debug("REST request to search Depenses for query {}", query);
        try {
            return StreamSupport.stream(depenseSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
