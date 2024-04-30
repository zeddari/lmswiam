package com.wiam.lms.web.rest;

import com.wiam.lms.domain.TypeProject;
import com.wiam.lms.repository.TypeProjectRepository;
import com.wiam.lms.repository.search.TypeProjectSearchRepository;
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
 * REST controller for managing {@link com.wiam.lms.domain.TypeProject}.
 */
@RestController
@RequestMapping("/api/type-projects")
@Transactional
public class TypeProjectResource {

    private final Logger log = LoggerFactory.getLogger(TypeProjectResource.class);

    private static final String ENTITY_NAME = "typeProject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeProjectRepository typeProjectRepository;

    private final TypeProjectSearchRepository typeProjectSearchRepository;

    public TypeProjectResource(TypeProjectRepository typeProjectRepository, TypeProjectSearchRepository typeProjectSearchRepository) {
        this.typeProjectRepository = typeProjectRepository;
        this.typeProjectSearchRepository = typeProjectSearchRepository;
    }

    /**
     * {@code POST  /type-projects} : Create a new typeProject.
     *
     * @param typeProject the typeProject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeProject, or with status {@code 400 (Bad Request)} if the typeProject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeProject> createTypeProject(@Valid @RequestBody TypeProject typeProject) throws URISyntaxException {
        log.debug("REST request to save TypeProject : {}", typeProject);
        if (typeProject.getId() != null) {
            throw new BadRequestAlertException("A new typeProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeProject result = typeProjectRepository.save(typeProject);
        typeProjectSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/type-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-projects/:id} : Updates an existing typeProject.
     *
     * @param id the id of the typeProject to save.
     * @param typeProject the typeProject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeProject,
     * or with status {@code 400 (Bad Request)} if the typeProject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeProject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeProject> updateTypeProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeProject typeProject
    ) throws URISyntaxException {
        log.debug("REST request to update TypeProject : {}, {}", id, typeProject);
        if (typeProject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeProject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeProject result = typeProjectRepository.save(typeProject);
        typeProjectSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeProject.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-projects/:id} : Partial updates given fields of an existing typeProject, field will ignore if it is null
     *
     * @param id the id of the typeProject to save.
     * @param typeProject the typeProject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeProject,
     * or with status {@code 400 (Bad Request)} if the typeProject is not valid,
     * or with status {@code 404 (Not Found)} if the typeProject is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeProject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeProject> partialUpdateTypeProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeProject typeProject
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeProject partially : {}, {}", id, typeProject);
        if (typeProject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeProject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeProject> result = typeProjectRepository
            .findById(typeProject.getId())
            .map(existingTypeProject -> {
                if (typeProject.getNameAr() != null) {
                    existingTypeProject.setNameAr(typeProject.getNameAr());
                }
                if (typeProject.getNameLat() != null) {
                    existingTypeProject.setNameLat(typeProject.getNameLat());
                }

                return existingTypeProject;
            })
            .map(typeProjectRepository::save)
            .map(savedTypeProject -> {
                typeProjectSearchRepository.index(savedTypeProject);
                return savedTypeProject;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeProject.getId().toString())
        );
    }

    /**
     * {@code GET  /type-projects} : get all the typeProjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeProjects in body.
     */
    @GetMapping("")
    public List<TypeProject> getAllTypeProjects() {
        log.debug("REST request to get all TypeProjects");
        return typeProjectRepository.findAll();
    }

    /**
     * {@code GET  /type-projects/:id} : get the "id" typeProject.
     *
     * @param id the id of the typeProject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeProject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeProject> getTypeProject(@PathVariable Long id) {
        log.debug("REST request to get TypeProject : {}", id);
        Optional<TypeProject> typeProject = typeProjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeProject);
    }

    /**
     * {@code DELETE  /type-projects/:id} : delete the "id" typeProject.
     *
     * @param id the id of the typeProject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeProject(@PathVariable Long id) {
        log.debug("REST request to delete TypeProject : {}", id);
        typeProjectRepository.deleteById(id);
        typeProjectSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /type-projects/_search?query=:query} : search for the typeProject corresponding
     * to the query.
     *
     * @param query the query of the typeProject search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<TypeProject> searchTypeProjects(@RequestParam String query) {
        log.debug("REST request to search TypeProjects for query {}", query);
        try {
            return StreamSupport.stream(typeProjectSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
