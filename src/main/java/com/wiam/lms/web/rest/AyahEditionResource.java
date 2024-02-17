package com.wiam.lms.web.rest;

import com.wiam.lms.domain.AyahEdition;
import com.wiam.lms.repository.AyahEditionRepository;
import com.wiam.lms.repository.search.AyahEditionSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.AyahEdition}.
 */
@RestController
@RequestMapping("/api/ayah-editions")
@Transactional
public class AyahEditionResource {

    private final Logger log = LoggerFactory.getLogger(AyahEditionResource.class);

    private static final String ENTITY_NAME = "ayahEdition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AyahEditionRepository ayahEditionRepository;

    private final AyahEditionSearchRepository ayahEditionSearchRepository;

    public AyahEditionResource(AyahEditionRepository ayahEditionRepository, AyahEditionSearchRepository ayahEditionSearchRepository) {
        this.ayahEditionRepository = ayahEditionRepository;
        this.ayahEditionSearchRepository = ayahEditionSearchRepository;
    }

    /**
     * {@code POST  /ayah-editions} : Create a new ayahEdition.
     *
     * @param ayahEdition the ayahEdition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ayahEdition, or with status {@code 400 (Bad Request)} if the ayahEdition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AyahEdition> createAyahEdition(@Valid @RequestBody AyahEdition ayahEdition) throws URISyntaxException {
        log.debug("REST request to save AyahEdition : {}", ayahEdition);
        if (ayahEdition.getId() != null) {
            throw new BadRequestAlertException("A new ayahEdition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AyahEdition result = ayahEditionRepository.save(ayahEdition);
        ayahEditionSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/ayah-editions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ayah-editions/:id} : Updates an existing ayahEdition.
     *
     * @param id the id of the ayahEdition to save.
     * @param ayahEdition the ayahEdition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ayahEdition,
     * or with status {@code 400 (Bad Request)} if the ayahEdition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ayahEdition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AyahEdition> updateAyahEdition(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody AyahEdition ayahEdition
    ) throws URISyntaxException {
        log.debug("REST request to update AyahEdition : {}, {}", id, ayahEdition);
        if (ayahEdition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ayahEdition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ayahEditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AyahEdition result = ayahEditionRepository.save(ayahEdition);
        ayahEditionSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ayahEdition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ayah-editions/:id} : Partial updates given fields of an existing ayahEdition, field will ignore if it is null
     *
     * @param id the id of the ayahEdition to save.
     * @param ayahEdition the ayahEdition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ayahEdition,
     * or with status {@code 400 (Bad Request)} if the ayahEdition is not valid,
     * or with status {@code 404 (Not Found)} if the ayahEdition is not found,
     * or with status {@code 500 (Internal Server Error)} if the ayahEdition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AyahEdition> partialUpdateAyahEdition(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody AyahEdition ayahEdition
    ) throws URISyntaxException {
        log.debug("REST request to partial update AyahEdition partially : {}, {}", id, ayahEdition);
        if (ayahEdition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ayahEdition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ayahEditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AyahEdition> result = ayahEditionRepository
            .findById(ayahEdition.getId())
            .map(existingAyahEdition -> {
                if (ayahEdition.getAyahId() != null) {
                    existingAyahEdition.setAyahId(ayahEdition.getAyahId());
                }
                if (ayahEdition.getEditionId() != null) {
                    existingAyahEdition.setEditionId(ayahEdition.getEditionId());
                }
                if (ayahEdition.getData() != null) {
                    existingAyahEdition.setData(ayahEdition.getData());
                }
                if (ayahEdition.getIsAudio() != null) {
                    existingAyahEdition.setIsAudio(ayahEdition.getIsAudio());
                }
                if (ayahEdition.getCreatedAt() != null) {
                    existingAyahEdition.setCreatedAt(ayahEdition.getCreatedAt());
                }
                if (ayahEdition.getUpdatedAt() != null) {
                    existingAyahEdition.setUpdatedAt(ayahEdition.getUpdatedAt());
                }

                return existingAyahEdition;
            })
            .map(ayahEditionRepository::save)
            .map(savedAyahEdition -> {
                ayahEditionSearchRepository.index(savedAyahEdition);
                return savedAyahEdition;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ayahEdition.getId().toString())
        );
    }

    /**
     * {@code GET  /ayah-editions} : get all the ayahEditions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ayahEditions in body.
     */
    @GetMapping("")
    public List<AyahEdition> getAllAyahEditions() {
        log.debug("REST request to get all AyahEditions");
        return ayahEditionRepository.findAll();
    }

    /**
     * {@code GET  /ayah-editions/:id} : get the "id" ayahEdition.
     *
     * @param id the id of the ayahEdition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ayahEdition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AyahEdition> getAyahEdition(@PathVariable Integer id) {
        log.debug("REST request to get AyahEdition : {}", id);
        Optional<AyahEdition> ayahEdition = ayahEditionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ayahEdition);
    }

    /**
     * {@code DELETE  /ayah-editions/:id} : delete the "id" ayahEdition.
     *
     * @param id the id of the ayahEdition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAyahEdition(@PathVariable Integer id) {
        log.debug("REST request to delete AyahEdition : {}", id);
        ayahEditionRepository.deleteById(id);
        ayahEditionSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ayah-editions/_search?query=:query} : search for the ayahEdition corresponding
     * to the query.
     *
     * @param query the query of the ayahEdition search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<AyahEdition> searchAyahEditions(@RequestParam String query) {
        log.debug("REST request to search AyahEditions for query {}", query);
        try {
            return StreamSupport.stream(ayahEditionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
