package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Juzs;
import com.wiam.lms.repository.JuzsRepository;
import com.wiam.lms.repository.search.JuzsSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.Juzs}.
 */
@RestController
@RequestMapping("/api/juzs")
@Transactional
public class JuzsResource {

    private final Logger log = LoggerFactory.getLogger(JuzsResource.class);

    private static final String ENTITY_NAME = "juzs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JuzsRepository juzsRepository;

    private final JuzsSearchRepository juzsSearchRepository;

    public JuzsResource(JuzsRepository juzsRepository, JuzsSearchRepository juzsSearchRepository) {
        this.juzsRepository = juzsRepository;
        this.juzsSearchRepository = juzsSearchRepository;
    }

    /**
     * {@code POST  /juzs} : Create a new juzs.
     *
     * @param juzs the juzs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new juzs, or with status {@code 400 (Bad Request)} if the juzs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Juzs> createJuzs(@RequestBody Juzs juzs) throws URISyntaxException {
        log.debug("REST request to save Juzs : {}", juzs);
        if (juzs.getId() != null) {
            throw new BadRequestAlertException("A new juzs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Juzs result = juzsRepository.save(juzs);
        juzsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/juzs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /juzs/:id} : Updates an existing juzs.
     *
     * @param id the id of the juzs to save.
     * @param juzs the juzs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated juzs,
     * or with status {@code 400 (Bad Request)} if the juzs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the juzs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Juzs> updateJuzs(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Juzs juzs)
        throws URISyntaxException {
        log.debug("REST request to update Juzs : {}, {}", id, juzs);
        if (juzs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, juzs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!juzsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Juzs result = juzsRepository.save(juzs);
        juzsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, juzs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /juzs/:id} : Partial updates given fields of an existing juzs, field will ignore if it is null
     *
     * @param id the id of the juzs to save.
     * @param juzs the juzs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated juzs,
     * or with status {@code 400 (Bad Request)} if the juzs is not valid,
     * or with status {@code 404 (Not Found)} if the juzs is not found,
     * or with status {@code 500 (Internal Server Error)} if the juzs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Juzs> partialUpdateJuzs(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Juzs juzs)
        throws URISyntaxException {
        log.debug("REST request to partial update Juzs partially : {}, {}", id, juzs);
        if (juzs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, juzs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!juzsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Juzs> result = juzsRepository
            .findById(juzs.getId())
            .map(existingJuzs -> {
                if (juzs.getCreatedAt() != null) {
                    existingJuzs.setCreatedAt(juzs.getCreatedAt());
                }
                if (juzs.getUpdatedAt() != null) {
                    existingJuzs.setUpdatedAt(juzs.getUpdatedAt());
                }

                return existingJuzs;
            })
            .map(juzsRepository::save)
            .map(savedJuzs -> {
                juzsSearchRepository.index(savedJuzs);
                return savedJuzs;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, juzs.getId().toString())
        );
    }

    /**
     * {@code GET  /juzs} : get all the juzs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of juzs in body.
     */
    @GetMapping("")
    public List<Juzs> getAllJuzs() {
        log.debug("REST request to get all Juzs");
        return juzsRepository.findAll();
    }

    /**
     * {@code GET  /juzs/:id} : get the "id" juzs.
     *
     * @param id the id of the juzs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the juzs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Juzs> getJuzs(@PathVariable("id") Integer id) {
        log.debug("REST request to get Juzs : {}", id);
        Optional<Juzs> juzs = juzsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(juzs);
    }

    /**
     * {@code DELETE  /juzs/:id} : delete the "id" juzs.
     *
     * @param id the id of the juzs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJuzs(@PathVariable("id") Integer id) {
        log.debug("REST request to delete Juzs : {}", id);
        juzsRepository.deleteById(id);
        juzsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /juzs/_search?query=:query} : search for the juzs corresponding
     * to the query.
     *
     * @param query the query of the juzs search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Juzs> searchJuzs(@RequestParam("query") String query) {
        log.debug("REST request to search Juzs for query {}", query);
        try {
            return StreamSupport.stream(juzsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
