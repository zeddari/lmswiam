package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Nationality;
import com.wiam.lms.repository.NationalityRepository;
import com.wiam.lms.repository.search.NationalitySearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.Nationality}.
 */
@RestController
@RequestMapping("/api/nationalities")
@Transactional
public class NationalityResource {

    private final Logger log = LoggerFactory.getLogger(NationalityResource.class);

    private static final String ENTITY_NAME = "nationality";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NationalityRepository nationalityRepository;

    private final NationalitySearchRepository nationalitySearchRepository;

    public NationalityResource(NationalityRepository nationalityRepository, NationalitySearchRepository nationalitySearchRepository) {
        this.nationalityRepository = nationalityRepository;
        this.nationalitySearchRepository = nationalitySearchRepository;
    }

    /**
     * {@code POST  /nationalities} : Create a new nationality.
     *
     * @param nationality the nationality to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nationality, or with status {@code 400 (Bad Request)} if the nationality has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Nationality> createNationality(@Valid @RequestBody Nationality nationality) throws URISyntaxException {
        log.debug("REST request to save Nationality : {}", nationality);
        if (nationality.getId() != null) {
            throw new BadRequestAlertException("A new nationality cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Nationality result = nationalityRepository.save(nationality);
        nationalitySearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/nationalities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nationalities/:id} : Updates an existing nationality.
     *
     * @param id the id of the nationality to save.
     * @param nationality the nationality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nationality,
     * or with status {@code 400 (Bad Request)} if the nationality is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nationality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Nationality> updateNationality(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Nationality nationality
    ) throws URISyntaxException {
        log.debug("REST request to update Nationality : {}, {}", id, nationality);
        if (nationality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nationality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nationalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Nationality result = nationalityRepository.save(nationality);
        nationalitySearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nationality.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nationalities/:id} : Partial updates given fields of an existing nationality, field will ignore if it is null
     *
     * @param id the id of the nationality to save.
     * @param nationality the nationality to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nationality,
     * or with status {@code 400 (Bad Request)} if the nationality is not valid,
     * or with status {@code 404 (Not Found)} if the nationality is not found,
     * or with status {@code 500 (Internal Server Error)} if the nationality couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Nationality> partialUpdateNationality(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Nationality nationality
    ) throws URISyntaxException {
        log.debug("REST request to partial update Nationality partially : {}, {}", id, nationality);
        if (nationality.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nationality.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nationalityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Nationality> result = nationalityRepository
            .findById(nationality.getId())
            .map(existingNationality -> {
                if (nationality.getNameAr() != null) {
                    existingNationality.setNameAr(nationality.getNameAr());
                }
                if (nationality.getNameLat() != null) {
                    existingNationality.setNameLat(nationality.getNameLat());
                }

                return existingNationality;
            })
            .map(nationalityRepository::save)
            .map(savedNationality -> {
                nationalitySearchRepository.index(savedNationality);
                return savedNationality;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nationality.getId().toString())
        );
    }

    /**
     * {@code GET  /nationalities} : get all the nationalities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nationalities in body.
     */
    @GetMapping("")
    public List<Nationality> getAllNationalities() {
        log.debug("REST request to get all Nationalities");
        return nationalityRepository.findAll();
    }

    /**
     * {@code GET  /nationalities/:id} : get the "id" nationality.
     *
     * @param id the id of the nationality to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nationality, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Nationality> getNationality(@PathVariable Long id) {
        log.debug("REST request to get Nationality : {}", id);
        Optional<Nationality> nationality = nationalityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nationality);
    }

    /**
     * {@code DELETE  /nationalities/:id} : delete the "id" nationality.
     *
     * @param id the id of the nationality to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
        log.debug("REST request to delete Nationality : {}", id);
        nationalityRepository.deleteById(id);
        nationalitySearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /nationalities/_search?query=:query} : search for the nationality corresponding
     * to the query.
     *
     * @param query the query of the nationality search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Nationality> searchNationalities(@RequestParam String query) {
        log.debug("REST request to search Nationalities for query {}", query);
        try {
            return StreamSupport.stream(nationalitySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
