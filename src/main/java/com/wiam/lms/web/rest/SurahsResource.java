package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Surahs;
import com.wiam.lms.repository.SurahsRepository;
import com.wiam.lms.repository.search.SurahsSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.Surahs}.
 */
@RestController
@RequestMapping("/api/surahs")
@Transactional
public class SurahsResource {

    private final Logger log = LoggerFactory.getLogger(SurahsResource.class);

    private static final String ENTITY_NAME = "surahs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurahsRepository surahsRepository;

    private final SurahsSearchRepository surahsSearchRepository;

    public SurahsResource(SurahsRepository surahsRepository, SurahsSearchRepository surahsSearchRepository) {
        this.surahsRepository = surahsRepository;
        this.surahsSearchRepository = surahsSearchRepository;
    }

    /**
     * {@code POST  /surahs} : Create a new surahs.
     *
     * @param surahs the surahs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surahs, or with status {@code 400 (Bad Request)} if the surahs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Surahs> createSurahs(@RequestBody Surahs surahs) throws URISyntaxException {
        log.debug("REST request to save Surahs : {}", surahs);
        if (surahs.getId() != null) {
            throw new BadRequestAlertException("A new surahs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Surahs result = surahsRepository.save(surahs);
        surahsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/surahs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /surahs/:id} : Updates an existing surahs.
     *
     * @param id the id of the surahs to save.
     * @param surahs the surahs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surahs,
     * or with status {@code 400 (Bad Request)} if the surahs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surahs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Surahs> updateSurahs(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Surahs surahs)
        throws URISyntaxException {
        log.debug("REST request to update Surahs : {}, {}", id, surahs);
        if (surahs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surahs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surahsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Surahs result = surahsRepository.save(surahs);
        surahsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surahs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /surahs/:id} : Partial updates given fields of an existing surahs, field will ignore if it is null
     *
     * @param id the id of the surahs to save.
     * @param surahs the surahs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surahs,
     * or with status {@code 400 (Bad Request)} if the surahs is not valid,
     * or with status {@code 404 (Not Found)} if the surahs is not found,
     * or with status {@code 500 (Internal Server Error)} if the surahs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Surahs> partialUpdateSurahs(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Surahs surahs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Surahs partially : {}, {}", id, surahs);
        if (surahs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surahs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surahsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Surahs> result = surahsRepository
            .findById(surahs.getId())
            .map(existingSurahs -> {
                if (surahs.getNumber() != null) {
                    existingSurahs.setNumber(surahs.getNumber());
                }
                if (surahs.getNameAr() != null) {
                    existingSurahs.setNameAr(surahs.getNameAr());
                }
                if (surahs.getNameEn() != null) {
                    existingSurahs.setNameEn(surahs.getNameEn());
                }
                if (surahs.getNameEnTranslation() != null) {
                    existingSurahs.setNameEnTranslation(surahs.getNameEnTranslation());
                }
                if (surahs.getType() != null) {
                    existingSurahs.setType(surahs.getType());
                }
                if (surahs.getCreatedAt() != null) {
                    existingSurahs.setCreatedAt(surahs.getCreatedAt());
                }
                if (surahs.getUpdatedAt() != null) {
                    existingSurahs.setUpdatedAt(surahs.getUpdatedAt());
                }

                return existingSurahs;
            })
            .map(surahsRepository::save)
            .map(savedSurahs -> {
                surahsSearchRepository.index(savedSurahs);
                return savedSurahs;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surahs.getId().toString())
        );
    }

    /**
     * {@code GET  /surahs} : get all the surahs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surahs in body.
     */
    @GetMapping("")
    public List<Surahs> getAllSurahs() {
        log.debug("REST request to get all Surahs");
        return surahsRepository.findAll();
    }

    /**
     * {@code GET  /surahs/:id} : get the "id" surahs.
     *
     * @param id the id of the surahs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surahs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Surahs> getSurahs(@PathVariable("id") Integer id) {
        log.debug("REST request to get Surahs : {}", id);
        Optional<Surahs> surahs = surahsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(surahs);
    }

    /**
     * {@code DELETE  /surahs/:id} : delete the "id" surahs.
     *
     * @param id the id of the surahs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurahs(@PathVariable("id") Integer id) {
        log.debug("REST request to delete Surahs : {}", id);
        surahsRepository.deleteById(id);
        surahsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /surahs/_search?query=:query} : search for the surahs corresponding
     * to the query.
     *
     * @param query the query of the surahs search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Surahs> searchSurahs(@RequestParam("query") String query) {
        log.debug("REST request to search Surahs for query {}", query);
        try {
            return StreamSupport.stream(surahsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
