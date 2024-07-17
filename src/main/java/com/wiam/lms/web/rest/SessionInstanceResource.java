package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.RemoteSessionDto;
import com.wiam.lms.domain.dto.SessionInstanceUniqueDto;
import com.wiam.lms.repository.GroupRepository;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.SessionInstanceSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.wiam.lms.domain.SessionInstance}.
 */
@RestController
@RequestMapping("/api/session-instances")
@Transactional
public class SessionInstanceResource {

    private final Logger log = LoggerFactory.getLogger(SessionInstanceResource.class);

    private static final String ENTITY_NAME = "sessionInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionInstanceRepository sessionInstanceRepository;

    private final GroupRepository groupRepository;
    private final SessionInstanceSearchRepository sessionInstanceSearchRepository;
    private final UserCustomRepository userCustomRepository;

    public SessionInstanceResource(
        UserCustomRepository userCustomRepository,
        GroupRepository groupRepository,
        ProgressionRepository progressionRepository,
        SessionInstanceRepository sessionInstanceRepository,
        SessionInstanceSearchRepository sessionInstanceSearchRepository
    ) {
        this.userCustomRepository = userCustomRepository;
        this.groupRepository = groupRepository;

        this.sessionInstanceRepository = sessionInstanceRepository;
        this.sessionInstanceSearchRepository = sessionInstanceSearchRepository;
    }

    @PostMapping("/unique")
    public Optional<SessionInstance> getSessionInstanceUnique(@Valid @RequestBody SessionInstanceUniqueDto siuDto) {
        return sessionInstanceRepository.findOne(siuDto.getSessionId(), siuDto.getSessionDate(), siuDto.getGroupId());
    }

    @GetMapping("{id}/remote")
    public List<RemoteSessionDto> getRemoteSessions(@PathVariable Long id) {
        log.debug("REST request to get all Session instances for the given student id");
        // getting the list of the student groups
        UserCustom userCustom = userCustomRepository.findByIdforGroup(id).get();
        List<Group> myGroups = new ArrayList<Group>();
        for (Group group : userCustom.getGroups()) myGroups.add(group);

        List<RemoteSessionDto> remoteSessionDtos = new ArrayList<RemoteSessionDto>();
        List<SessionInstance> remotSessions = sessionInstanceRepository.findRemoteSessionInstances(myGroups);
        for (SessionInstance sessionInstance : remotSessions) {
            RemoteSessionDto remoteSessionDto = new RemoteSessionDto();
            if (sessionInstance.getSession1() != null) {
                remoteSessionDto.setGender(sessionInstance.getSession1().getTargetedGender().name());
                //remoteSessionDto.setSessionType(sessionInstance.getSession1().getSessionMode().name());
            }
            remoteSessionDto.setTitle(sessionInstance.getTitle());
            remoteSessionDto.setSessionDate(sessionInstance.getSessionDate());
            remoteSessionDto.setDay(sessionInstance.getSessionDate().getDayOfWeek().getValue());
            /*remoteSessionDto.setSessionStartTime(sessionInstance.getSessionStartTime());
            remoteSessionDto.setSessionEndTime(sessionInstance.getSessionEndTime());
            remoteSessionDto.setSessionStartTime(sessionInstance.getSessionStartTime().toString());
            remoteSessionDto.setSessionEndTime(sessionInstance.getSessionEndTime().toString());*/
            if (sessionInstance.getGroup() != null) remoteSessionDto.setGroupName(sessionInstance.getGroup().getNameAr());
            remoteSessionDto.setIsActive(sessionInstance.getIsActive());
            /*remoteSessionDto.setLink(sessionInstance.getSessionLink());
            remoteSessionDto.setLink(sessionInstance.getSessionLink().getLink());*/
            remoteSessionDto.setInfo(sessionInstance.getInfo());
            remoteSessionDtos.add(remoteSessionDto);
        }
        return remoteSessionDtos;
    }

    /**
     * {@code POST  /session-instances} : Create a new sessionInstance.
     *
     * @param sessionInstance the sessionInstance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionInstance, or with status {@code 400 (Bad Request)} if the sessionInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SessionInstance> createSessionInstance(@Valid @RequestBody SessionInstance sessionInstance)
        throws URISyntaxException {
        log.debug("REST request to save SessionInstance : {}", sessionInstance);
        if (sessionInstance.getId() != null) {
            throw new BadRequestAlertException("A new sessionInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionInstance result = sessionInstanceRepository.save(sessionInstance);
        sessionInstanceSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/session-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-instances/:id} : Updates an existing sessionInstance.
     *
     * @param id the id of the sessionInstance to save.
     * @param sessionInstance the sessionInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionInstance,
     * or with status {@code 400 (Bad Request)} if the sessionInstance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessionInstance> updateSessionInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionInstance sessionInstance
    ) throws URISyntaxException {
        log.debug("REST request to update SessionInstance : {}, {}", id, sessionInstance);
        if (sessionInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionInstance result = sessionInstanceRepository.save(sessionInstance);
        sessionInstanceSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionInstance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-instances/:id} : Partial updates given fields of an existing sessionInstance, field will ignore if it is null
     *
     * @param id the id of the sessionInstance to save.
     * @param sessionInstance the sessionInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionInstance,
     * or with status {@code 400 (Bad Request)} if the sessionInstance is not valid,
     * or with status {@code 404 (Not Found)} if the sessionInstance is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionInstance> partialUpdateSessionInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionInstance sessionInstance
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionInstance partially : {}, {}", id, sessionInstance);
        if (sessionInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionInstance> result = sessionInstanceRepository
            .findById(sessionInstance.getId())
            .map(existingSessionInstance -> {
                if (sessionInstance.getTitle() != null) {
                    existingSessionInstance.setTitle(sessionInstance.getTitle());
                }
                if (sessionInstance.getSessionDate() != null) {
                    existingSessionInstance.setSessionDate(sessionInstance.getSessionDate());
                }
                if (sessionInstance.getStartTime() != null) {
                    existingSessionInstance.setStartTime(sessionInstance.getStartTime());
                }
                if (sessionInstance.getDuration() != null) {
                    existingSessionInstance.setDuration(sessionInstance.getDuration());
                }
                if (sessionInstance.getInfo() != null) {
                    existingSessionInstance.setInfo(sessionInstance.getInfo());
                }
                if (sessionInstance.getAttendance() != null) {
                    existingSessionInstance.setAttendance(sessionInstance.getAttendance());
                }
                if (sessionInstance.getJustifRef() != null) {
                    existingSessionInstance.setJustifRef(sessionInstance.getJustifRef());
                }
                if (sessionInstance.getIsActive() != null) {
                    existingSessionInstance.setIsActive(sessionInstance.getIsActive());
                }

                return existingSessionInstance;
            })
            .map(sessionInstanceRepository::save)
            .map(savedSessionInstance -> {
                sessionInstanceSearchRepository.index(savedSessionInstance);
                return savedSessionInstance;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionInstance.getId().toString())
        );
    }

    /**
     * {@code GET  /session-instances} : get all the sessionInstances.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionInstances in body.
     */
    @GetMapping("")
    public List<SessionInstance> getAllSessionInstances(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all SessionInstances");
        if (eagerload) {
            return sessionInstanceRepository.findAllWithEagerRelationships();
        } else {
            return sessionInstanceRepository.findAll();
        }
    }

    /**
     * {@code GET  /session-instances/:id} : get the "id" sessionInstance.
     *
     * @param id the id of the sessionInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionInstance> getSessionInstance(@PathVariable("id") Long id) {
        log.debug("REST request to get SessionInstance : {}", id);
        Optional<SessionInstance> sessionInstance = sessionInstanceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sessionInstance);
    }

    /**
     * {@code GET  /session-instances/:id} : get the "id" sessionInstance.
     *
     * @param id the id of the sessionInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}/bySession")
    public List<SessionInstance> getSessionInstanceBySession(
        @PathVariable("id") Long id,
        @RequestParam("month") Long month,
        @RequestParam("year") Long year
    ) {
        log.debug("REST request to get SessionInstance by session : {}", id);
        return sessionInstanceRepository.findOneBySessionId(id, month, year);
    }

    /**
     * {@code GET  /session-instances/:id} : get the "id" sessionInstance.
     *
     * @param id the id of the sessionInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}/bySite")
    public List<SessionInstance> getSessionInstanceBySite(@PathVariable("id") Long id, @RequestParam("sessionDate") LocalDate sessionDate) {
        log.debug("REST request to get SessionInstance by siteId and sessionDate : {}", id);
        return sessionInstanceRepository.findOneBySiteId(id, sessionDate);
    }

    /**
     * {@code DELETE  /session-instances/:id} : delete the "id" sessionInstance.
     *
     * @param id the id of the sessionInstance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionInstance(@PathVariable("id") Long id) {
        log.debug("REST request to delete SessionInstance : {}", id);
        sessionInstanceRepository.deleteById(id);
        sessionInstanceSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /session-instances/_search?query=:query} : search for the sessionInstance corresponding
     * to the query.
     *
     * @param query the query of the sessionInstance search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SessionInstance> searchSessionInstances(@RequestParam("query") String query) {
        log.debug("REST request to search SessionInstances for query {}", query);
        try {
            return StreamSupport.stream(sessionInstanceSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
