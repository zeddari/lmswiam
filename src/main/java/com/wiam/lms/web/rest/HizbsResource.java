package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Hizbs;
import com.wiam.lms.repository.HizbsRepository;
import com.wiam.lms.repository.search.HizbsSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.Hizbs}.
 */
@RestController
@RequestMapping("/api/hizbs")
@Transactional
public class HizbsResource {

    private final Logger log = LoggerFactory.getLogger(HizbsResource.class);

    private static final String ENTITY_NAME = "hizbs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HizbsRepository hizbsRepository;

    private final HizbsSearchRepository hizbsSearchRepository;

    public HizbsResource(HizbsRepository hizbsRepository, HizbsSearchRepository hizbsSearchRepository) {
        this.hizbsRepository = hizbsRepository;
        this.hizbsSearchRepository = hizbsSearchRepository;
    }

    /**
     * {@code POST  /hizbs} : Create a new hizbs.
     *
     * @param hizbs the hizbs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hizbs, or with status {@code 400 (Bad Request)} if the hizbs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Hizbs> createHizbs(@RequestBody Hizbs hizbs) throws URISyntaxException {
        log.debug("REST request to save Hizbs : {}", hizbs);
        if (hizbs.getId() != null) {
            throw new BadRequestAlertException("A new hizbs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hizbs result = hizbsRepository.save(hizbs);
        hizbsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/hizbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hizbs/:id} : Updates an existing hizbs.
     *
     * @param id the id of the hizbs to save.
     * @param hizbs the hizbs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hizbs,
     * or with status {@code 400 (Bad Request)} if the hizbs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hizbs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Hizbs> updateHizbs(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Hizbs hizbs)
        throws URISyntaxException {
        log.debug("REST request to update Hizbs : {}, {}", id, hizbs);
        if (hizbs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hizbs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hizbsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hizbs result = hizbsRepository.save(hizbs);
        hizbsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hizbs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hizbs/:id} : Partial updates given fields of an existing hizbs, field will ignore if it is null
     *
     * @param id the id of the hizbs to save.
     * @param hizbs the hizbs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hizbs,
     * or with status {@code 400 (Bad Request)} if the hizbs is not valid,
     * or with status {@code 404 (Not Found)} if the hizbs is not found,
     * or with status {@code 500 (Internal Server Error)} if the hizbs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hizbs> partialUpdateHizbs(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Hizbs hizbs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hizbs partially : {}, {}", id, hizbs);
        if (hizbs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hizbs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hizbsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hizbs> result = hizbsRepository
            .findById(hizbs.getId())
            .map(existingHizbs -> {
                if (hizbs.getCreatedAt() != null) {
                    existingHizbs.setCreatedAt(hizbs.getCreatedAt());
                }
                if (hizbs.getUpdatedAt() != null) {
                    existingHizbs.setUpdatedAt(hizbs.getUpdatedAt());
                }

                return existingHizbs;
            })
            .map(hizbsRepository::save)
            .map(savedHizbs -> {
                hizbsSearchRepository.index(savedHizbs);
                return savedHizbs;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hizbs.getId().toString())
        );
    }

    /**
     * {@code GET  /hizbs} : get all the hizbs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hizbs in body.
     */
    @GetMapping("")
    public List<Hizbs> getAllHizbs() {
        log.debug("REST request to get all Hizbs");
        return hizbsRepository.findAll();
    }

    /**
     * {@code GET  /hizbs/:id} : get the "id" hizbs.
     *
     * @param id the id of the hizbs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hizbs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Hizbs> getHizbs(@PathVariable Integer id) {
        log.debug("REST request to get Hizbs : {}", id);
        Optional<Hizbs> hizbs = hizbsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hizbs);
    }

    /**
     * {@code DELETE  /hizbs/:id} : delete the "id" hizbs.
     *
     * @param id the id of the hizbs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHizbs(@PathVariable Integer id) {
        log.debug("REST request to delete Hizbs : {}", id);
        hizbsRepository.deleteById(id);
        hizbsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /hizbs/_search?query=:query} : search for the hizbs corresponding
     * to the query.
     *
     * @param query the query of the hizbs search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Hizbs> searchHizbs(@RequestParam String query) {
        log.debug("REST request to search Hizbs for query {}", query);
        try {
            return StreamSupport.stream(hizbsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
