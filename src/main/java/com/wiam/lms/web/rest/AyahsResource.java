package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.repository.AyahsRepository;
import com.wiam.lms.repository.search.AyahsSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
 * REST controller for managing {@link com.wiam.lms.domain.Ayahs}.
 */
@RestController
@RequestMapping("/api/ayahs")
@Transactional
public class AyahsResource {

    private final Logger log = LoggerFactory.getLogger(AyahsResource.class);

    private static final String ENTITY_NAME = "ayahs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AyahsRepository ayahsRepository;

    private final AyahsSearchRepository ayahsSearchRepository;

    public AyahsResource(AyahsRepository ayahsRepository, AyahsSearchRepository ayahsSearchRepository) {
        this.ayahsRepository = ayahsRepository;
        this.ayahsSearchRepository = ayahsSearchRepository;
    }

    /**
     * {@code POST  /ayahs} : Create a new ayahs.
     *
     * @param ayahs the ayahs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ayahs, or with status {@code 400 (Bad Request)} if the ayahs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ayahs> createAyahs(@Valid @RequestBody Ayahs ayahs) throws URISyntaxException {
        log.debug("REST request to save Ayahs : {}", ayahs);
        if (ayahs.getId() != null) {
            throw new BadRequestAlertException("A new ayahs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ayahs result = ayahsRepository.save(ayahs);
        ayahsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/ayahs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ayahs/:id} : Updates an existing ayahs.
     *
     * @param id the id of the ayahs to save.
     * @param ayahs the ayahs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ayahs,
     * or with status {@code 400 (Bad Request)} if the ayahs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ayahs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ayahs> updateAyahs(
        @PathVariable(value = "id", required = false) final Integer id,
        @Valid @RequestBody Ayahs ayahs
    ) throws URISyntaxException {
        log.debug("REST request to update Ayahs : {}, {}", id, ayahs);
        if (ayahs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ayahs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ayahsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ayahs result = ayahsRepository.save(ayahs);
        ayahsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ayahs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ayahs/:id} : Partial updates given fields of an existing ayahs, field will ignore if it is null
     *
     * @param id the id of the ayahs to save.
     * @param ayahs the ayahs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ayahs,
     * or with status {@code 400 (Bad Request)} if the ayahs is not valid,
     * or with status {@code 404 (Not Found)} if the ayahs is not found,
     * or with status {@code 500 (Internal Server Error)} if the ayahs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ayahs> partialUpdateAyahs(
        @PathVariable(value = "id", required = false) final Integer id,
        @NotNull @RequestBody Ayahs ayahs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ayahs partially : {}, {}", id, ayahs);
        if (ayahs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ayahs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ayahsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ayahs> result = ayahsRepository
            .findById(ayahs.getId())
            .map(existingAyahs -> {
                if (ayahs.getNumber() != null) {
                    existingAyahs.setNumber(ayahs.getNumber());
                }
                if (ayahs.getTextdesc() != null) {
                    existingAyahs.setTextdesc(ayahs.getTextdesc());
                }
                if (ayahs.getNumberInSurah() != null) {
                    existingAyahs.setNumberInSurah(ayahs.getNumberInSurah());
                }
                if (ayahs.getPage() != null) {
                    existingAyahs.setPage(ayahs.getPage());
                }
                if (ayahs.getSurahId() != null) {
                    existingAyahs.setSurahId(ayahs.getSurahId());
                }
                if (ayahs.getHizbId() != null) {
                    existingAyahs.setHizbId(ayahs.getHizbId());
                }
                if (ayahs.getJuzId() != null) {
                    existingAyahs.setJuzId(ayahs.getJuzId());
                }
                if (ayahs.getSajda() != null) {
                    existingAyahs.setSajda(ayahs.getSajda());
                }
                if (ayahs.getCreatedAt() != null) {
                    existingAyahs.setCreatedAt(ayahs.getCreatedAt());
                }
                if (ayahs.getUpdatedAt() != null) {
                    existingAyahs.setUpdatedAt(ayahs.getUpdatedAt());
                }

                return existingAyahs;
            })
            .map(ayahsRepository::save)
            .map(savedAyahs -> {
                ayahsSearchRepository.index(savedAyahs);
                return savedAyahs;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ayahs.getId().toString())
        );
    }

    /**
     * {@code GET  /ayahs} : get all the ayahs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ayahs in body.
     */
    @GetMapping("")
    public List<Ayahs> getAllAyahs() {
        log.debug("REST request to get all Ayahs");
        return ayahsRepository.findAll();
    }

    @GetMapping("/{surahId}/bySurah")
    public List<Ayahs> getAllAyahsBySurahsId(@PathVariable("surahId") Integer surahId) {
        log.debug("REST request to get all Ayahs");
        return ayahsRepository.findAllBySurahsId(surahId);
    }

    @GetMapping("/bySurahAya")
    public ResponseEntity<Ayahs> getAyahs(@RequestParam("surahId") Integer surahId, @RequestParam("ayaId") Integer ayaId) {
        log.debug("REST request to get Ayahs : {}", surahId);
        Optional<Ayahs> ayahs = ayahsRepository.findByAyaSurah(ayaId, surahId);
        return ResponseUtil.wrapOrNotFound(ayahs);
    }

    /**
     * {@code GET  /ayahs/:id} : get the "id" ayahs.
     *
     * @param id the id of the ayahs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ayahs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ayahs> getAyahs(@PathVariable("id") Integer id) {
        log.debug("REST request to get Ayahs : {}", id);
        Optional<Ayahs> ayahs = ayahsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ayahs);
    }

    @GetMapping("/getAya")
    public ResponseEntity<Ayahs> getAya(@RequestParam("ayahNumber") Integer ayahNumber, @RequestParam("surahNumber") Integer surahNumber) {
        //log.debug("REST request to get Ayahs : {}", id);
        Optional<Ayahs> ayahs = ayahsRepository.findByAyaSurah(ayahNumber, surahNumber);
        return ResponseUtil.wrapOrNotFound(ayahs);
    }

    @GetMapping("/getfromtoAya")
    public List<Ayahs> getfromtoAya(
        @RequestParam("fromayahNumber") Integer fromayahNumber,
        @RequestParam("fromsurahNumber") Integer fromsurahNumber,
        @RequestParam("toayahNumber") Integer toayahNumber,
        @RequestParam("tosurahNumber") Integer tosurahNumber
    ) {
        //log.debug("REST request to get Ayahs : {}", id);

        List<Ayahs> ayahs = new ArrayList<Ayahs>();
        Optional<Ayahs> fromayahs = ayahsRepository.findByAyaSurah(fromayahNumber, fromsurahNumber);
        ayahs.add(fromayahs.get());
        Optional<Ayahs> toayahs = ayahsRepository.findByAyaSurah(toayahNumber, tosurahNumber);
        ayahs.add(toayahs.get());
        return ayahs;
    }

    /**
     * {@code DELETE  /ayahs/:id} : delete the "id" ayahs.
     *
     * @param id the id of the ayahs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAyahs(@PathVariable("id") Integer id) {
        log.debug("REST request to delete Ayahs : {}", id);
        ayahsRepository.deleteById(id);
        ayahsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ayahs/_search?query=:query} : search for the ayahs corresponding
     * to the query.
     *
     * @param query the query of the ayahs search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Ayahs> searchAyahs(@RequestParam("query") String query) {
        log.debug("REST request to search Ayahs for query {}", query);
        try {
            return StreamSupport.stream(ayahsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
