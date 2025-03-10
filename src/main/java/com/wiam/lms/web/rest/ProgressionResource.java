package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.Surahs;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.AyahDto;
import com.wiam.lms.domain.dto.ExamDto;
import com.wiam.lms.domain.dto.InstanceProgressionDTO;
import com.wiam.lms.domain.dto.ProgressionDetailsDTO;
import com.wiam.lms.domain.dto.ProgressionDto;
import com.wiam.lms.domain.dto.RemoteSessionDto;
import com.wiam.lms.domain.dto.SessionInstanceUniqueDto;
import com.wiam.lms.domain.dto.SurahDto;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Riwayats;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.domain.enumeration.Tilawa;
import com.wiam.lms.repository.GroupRepository;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.ProgressionSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wiam.lms.domain.Progression}.
 */
@RestController
@RequestMapping("/api/progressions")
@Transactional
public class ProgressionResource {

    private final Logger log = LoggerFactory.getLogger(ProgressionResource.class);

    private static final String ENTITY_NAME = "progression";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgressionRepository progressionRepository;

    private final SessionInstanceRepository sessionInstanceRepository;

    private final ProgressionSearchRepository progressionSearchRepository;

    private final GroupRepository groupRepository;

    private final UserCustomRepository userCustomRepository;

    public ProgressionResource(
        UserCustomRepository userCustomRepository,
        GroupRepository groupRepository,
        SessionInstanceRepository sessionInstanceRepository,
        ProgressionRepository progressionRepository,
        ProgressionSearchRepository progressionSearchRepository
    ) {
        this.progressionRepository = progressionRepository;
        this.progressionSearchRepository = progressionSearchRepository;
        this.sessionInstanceRepository = sessionInstanceRepository;
        this.groupRepository = groupRepository;
        this.userCustomRepository = userCustomRepository;
    }

    @GetMapping("/{id}/myProgressions")
    public List<Progression> getmyProgressions(@PathVariable Long id) {
        log.debug("REST request to get all progressions for the given student id");
        List<Progression> progressions = progressionRepository.findProgressions(id);
        return progressions;
    }

    @GetMapping("/{id}/UserProgressions")
    public List<Progression> getProgressionsForUserAndDate(
        @PathVariable("id") Long id,
        @RequestParam("fromSessionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromSessionDate,
        @RequestParam("toSessionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toSessionDate,
        @RequestParam("examType") String examType
    ) {
        log.debug(
            "REST request to get all progressions for user with id {} on date {}, examType: {}",
            id,
            fromSessionDate,
            toSessionDate,
            examType
        );
        ExamType examType1 = ExamType.NONE;
        if (!examType.equals("NONE")) {
            examType1 = ExamType.MANDATORY;
        }
        return progressionRepository.findUserProgressions(id, fromSessionDate, toSessionDate, examType1);
    }

    @GetMapping("/{id}/myAttendance")
    public List<Progression> getmyAttendance(@PathVariable Long id) {
        log.debug("REST request to get all progressions for the given student id");
        List<Progression> progressions = progressionRepository.findAttendanceProgressions(id);
        return progressions;
    }

    @GetMapping("/attendance")
    public List<Progression> getAttendance(
        @RequestParam Long siteId,
        @RequestParam Long sessionId,
        @RequestParam Long groupId,
        @RequestParam LocalDate sessionDate
    ) {
        log.debug("REST request to get all progressions by site, session, group and date");
        List<Progression> progressions = progressionRepository.findAttendanceAllProgressions(siteId, sessionId, groupId, sessionDate);
        return progressions;
    }

    @GetMapping("/progressionStats")
    public List<ProgressionDto> getProgressionStats() {
        List<ProgressionDto> progressionDtos = new ArrayList<>();
        List<Progression> progressions = progressionRepository.findAll();
        for (Progression progression : progressions) {
            ProgressionDto p = new ProgressionDto();
            p.setId(progression.getId());
            p.setAdaeScore(progression.getAdaeScore());
            p.setHifdScore(progression.getHifdScore());
            p.setTajweedScore(progression.getTajweedScore());
            p.setExamType(progression.getExamType());
            p.setTilawaType(progression.getTilawaType());
            p.setRiwaya(progression.getRiwaya());
            if (progression.getToAyahs() != null && progression.getFromAyahs() != null) p.setAyahsCount(
                progression.getToAyahs().getId() - progression.getFromAyahs().getId() + 1
            ); else p.setAyahsCount(0);
            if (progression.getSessionInstance() != null) p.setSessionDate(progression.getSessionInstance().getSessionDate());
            p.setForAttendance(progression.getIsForAttendance());
            p.setAttendance(progression.getAttendance());
            if (progression.getStudent() != null) p.setStudentId(progression.getStudent().getId());
            p.setEarlyDeparture(progression.getEarlyDeparture());
            p.setLateArrival(progression.getLateArrival());
            progressionDtos.add(p);
        }
        return progressionDtos;
    }

    /**
     * {@code POST  /progressions} : Create a new progression.
     *
     * @param progression the progression to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new progression, or with status {@code 400 (Bad Request)} if the progression has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Progression> createProgression(@Valid @RequestBody Progression progression) throws URISyntaxException {
        log.debug("REST request to save Progression : {}", progression);
        if (progression.getId() != null) {
            throw new BadRequestAlertException("A new progression cannot already have an ID", ENTITY_NAME, "idexists");
        } /*if (progressionRepository.isAlreadyExists(progression.getSessionInstance().getId(), progression.getStudent().getId()) != null) {
            throw new BadRequestAlertException("A progression exists already for the student in this session", ENTITY_NAME, "");
        } */else {
            progression.setCreatedAt(new Date());
            Progression result = progressionRepository.save(progression);
            progressionSearchRepository.index(result);
            return ResponseEntity
                .created(new URI("/api/progressions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        }
    }

    /**
     * {@code PUT  /progressions/:id} : Updates an existing progression.
     *
     * @param id the id of the progression to save.
     * @param progression the progression to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progression,
     * or with status {@code 400 (Bad Request)} if the progression is not valid,
     * or with status {@code 500 (Internal Server Error)} if the progression couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Progression> updateProgression(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Progression progression
    ) throws URISyntaxException {
        log.debug("REST request to update Progression : {}, {}", id, progression);
        if (progression.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progression.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        progression.setUpdatedAt(new Date());
        Progression result = progressionRepository.save(progression);
        progressionSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progression.getId().toString()))
            .body(result);
    }

    @GetMapping("{id}/exams")
    public List<ExamDto> geAlltExams(@PathVariable Long id) {
        log.debug("REST request to get all Progressions whish are exams");
        List<ExamDto> exams = new ArrayList<ExamDto>();
        List<Progression> progressions = progressionRepository.findExams(id);
        for (Progression progression : progressions) {
            ExamDto exam = new ExamDto();
            // score info
            exam.setAdaeScore(progression.getAdaeScore());
            exam.setTajweedScore(progression.getTajweedScore());
            exam.setHifdScore(progression.getHifdScore());
            exam.setObservation(progression.getObservation());
            // exam info
            if (progression.getSessionInstance() != null) exam.setSessionName(progression.getSessionInstance().getTitle());
            exam.setExamType(progression.getExamType());
            exam.setRiwaya(progression.getRiwaya());
            //exam.setStartTime(progression.getStartTime());
            exam.setFromAyaNum(progression.getFromAyaNum());
            exam.setToAyaNum(progression.getToAyaNum());
            //if (progression.getFromSourate() != null) exam.setFromSourate(progression.getFromSourate().getNameAr());
            //if (progression.getToSourate() != null) exam.setToSourate(progression.getToSourate().getNameAr());
            // attendance infp
            exam.setAttendance(progression.getAttendance());
            exams.add(exam);
        }
        return exams;
    }

    /**
     * {@code PATCH  /progressions/:id} : Partial updates given fields of an existing progression, field will ignore if it is null
     *
     * @param id the id of the progression to save.
     * @param progression the progression to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progression,
     * or with status {@code 400 (Bad Request)} if the progression is not valid,
     * or with status {@code 404 (Not Found)} if the progression is not found,
     * or with status {@code 500 (Internal Server Error)} if the progression couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Progression> partialUpdateProgression(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Progression progression
    ) throws URISyntaxException {
        log.debug("REST request to partial update Progression partially : {}, {}", id, progression);
        if (progression.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progression.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Progression> result = progressionRepository
            .findById(progression.getId())
            .map(existingProgression -> {
                if (progression.getAttendance() != null) {
                    existingProgression.setAttendance(progression.getAttendance());
                }
                if (progression.getJustifRef() != null) {
                    existingProgression.setJustifRef(progression.getJustifRef());
                }
                if (progression.getLateArrival() != null) {
                    existingProgression.setLateArrival(progression.getLateArrival());
                }
                if (progression.getEarlyDeparture() != null) {
                    existingProgression.setEarlyDeparture(progression.getEarlyDeparture());
                }
                if (progression.getProgressionMode() != null) {
                    existingProgression.setProgressionMode(progression.getProgressionMode());
                }
                if (progression.getExamType() != null) {
                    existingProgression.setExamType(progression.getExamType());
                }
                if (progression.getRiwaya() != null) {
                    existingProgression.setRiwaya(progression.getRiwaya());
                }
                if (progression.getFromSourate() != null) {
                    existingProgression.setFromSourate(progression.getFromSourate());
                }
                if (progression.getToSourate() != null) {
                    existingProgression.setToSourate(progression.getToSourate());
                }
                if (progression.getFromAyaNum() != null) {
                    existingProgression.setFromAyaNum(progression.getFromAyaNum());
                }
                if (progression.getToAyaNum() != null) {
                    existingProgression.setToAyaNum(progression.getToAyaNum());
                }
                if (progression.getFromAyaVerset() != null) {
                    existingProgression.setFromAyaVerset(progression.getFromAyaVerset());
                }
                if (progression.getToAyaVerset() != null) {
                    existingProgression.setToAyaVerset(progression.getToAyaVerset());
                }
                if (progression.getTilawaType() != null) {
                    existingProgression.setTilawaType(progression.getTilawaType());
                }
                if (progression.getTaskDone() != null) {
                    existingProgression.setTaskDone(progression.getTaskDone());
                }
                if (progression.getTajweedScore() != null) {
                    existingProgression.setTajweedScore(progression.getTajweedScore());
                }
                if (progression.getHifdScore() != null) {
                    existingProgression.setHifdScore(progression.getHifdScore());
                }
                if (progression.getAdaeScore() != null) {
                    existingProgression.setAdaeScore(progression.getAdaeScore());
                }
                if (progression.getObservation() != null) {
                    existingProgression.setObservation(progression.getObservation());
                }
                existingProgression.setUpdatedAt(new Date());
                return existingProgression;
            })
            .map(progressionRepository::save)
            .map(savedProgression -> {
                progressionSearchRepository.index(savedProgression);
                return savedProgression;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progression.getId().toString())
        );
    }

    /**
     * {@code GET  /progressions} : get all the progressions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of progressions in body.
     */
    @GetMapping("")
    public List<Progression> getAllProgressions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all Progressions");
        if (eagerload) {
            return progressionRepository.findAllWithEagerRelationships();
        } else {
            return progressionRepository.findAll();
        }
    }

    /**
     * {@code GET  /progressions/:id} : get the "id" progression.
     *
     * @param id the id of the progression to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the progression, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Progression> getProgression(@PathVariable("id") Long id) {
        log.debug("REST request to get Progression : {}", id);
        Optional<Progression> progression = progressionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(progression);
    }

    @GetMapping("/{id}/updateAttendance")
    public ResponseEntity<Progression> UpdateAttendanceProgression(@PathVariable Long id) {
        log.debug("REST request to update attendance of the Progression : {}", id);
        if (!progressionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Progression progression = progressionRepository.findById(id).get();
        if (progression.getAttendance().equals(Attendance.PRESENT)) progression.setAttendance(Attendance.ABSENT); else if (
            progression.getAttendance().equals(Attendance.ABSENT)
        ) progression.setAttendance(Attendance.PRESENT);

        Progression result = progressionRepository.save(progression);
        progressionSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progression.getId().toString()))
            .body(result);
    }

    @GetMapping("/{id}/updateAttendance/{status}")
    public ResponseEntity<?> updateAttendance(@PathVariable Long id, @PathVariable Attendance status) {
        log.debug("REST request to update attendance of the Progression : {}", id);

        try {
            // Validate ID exists
            Progression progression = progressionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progression not found with id: " + id));

            // Validate status is not null
            if (status == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid attendance status"));
            }

            // Update attendance
            progression.setAttendance(status);
            Progression result = progressionRepository.save(progression);

            // Index for search if needed
            progressionSearchRepository.index(result);

            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, progression.getId().toString()))
                .body(result);
        } catch (ResourceNotFoundException e) {
            log.error("Progression not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Unable to update attendance due to data conflicts"));
        } catch (Exception e) {
            log.error("Unexpected error updating attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred"));
        }
    }

    // Error response helper class
    public class ErrorResponse {

        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        // Getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @GetMapping("/{id}/forgroup")
    public ResponseEntity<SessionInstance> createProgressionsforGroup(@PathVariable Long id) {
        log.debug("REST request to create the progressions of the group elements given the sessionInstanceId : {}", id);
        if (!sessionInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", "SessionInstance", "idnotfound");
        } else {
            SessionInstance instance = sessionInstanceRepository.findById(id).get();
            if (instance.getGroup() != null) {
                Group group = groupRepository.findById(instance.getGroup().getId()).get();
                if (group.getElements() != null && group.getElements().size() > 0) {
                    for (UserCustom student : group.getElements()) {
                        if (progressionRepository.isAlreadyExists(id, student.getId()) == null) {
                            Progression progression = new Progression();
                            progression.setLateArrival(false);
                            progression.setEarlyDeparture(false);
                            progression.setIsForAttendance(true);
                            progression.setTaskDone(true);
                            progression.setHifdScore(1);
                            progression.setTajweedScore(1);
                            progression.setAdaeScore(1);
                            progression.setAttendance(Attendance.NONE);
                            progression.setExamType(ExamType.NONE);
                            progression.setRiwaya(Riwayats.WARSHS_NARRATION_ON_THE_AUTHORITY_OF_NAFI_THROUGH_TAYYIBAH);
                            progression.setTilawaType(Tilawa.TILAWA);
                            progression.setSessionInstance(instance);
                            progression.setStudent(student);
                            progression.setSite17(instance.getSite16());
                            //progression.setStartTime(ZonedDateTime.now());
                            progressionRepository.save(progression);
                        }
                    }
                }
            }
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createAlert(applicationName, "Progressions table created with success", ENTITY_NAME))
                .body(instance);
        }
    }

    @GetMapping("/{id}/bysessioninstance")
    public List<Progression> getAllProgressionsBySessionInstance(@PathVariable Long id) {
        log.debug("REST request to get all Progressions for a given sessionInstance id");
        return progressionRepository.findAllBySessionInstance(id);
    }

    @GetMapping("/{id}/byRole")
    public List<Progression> getAllProgressionsByRole(@PathVariable Long id, Principal principal) {
        log.debug("REST request to get all Progressions for a given sessionInstance id and role");
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        List<Progression> progressions = new ArrayList<Progression>();
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                progressions = progressionRepository.findAllByRole(id, userCustom.getId());
            } else {
                progressions = progressionRepository.findAllBySessionInstance(id);
            }
        }
        return progressions;
    }

    @GetMapping("/{id}/byStudent")
    public List<Progression> getProgressionsByStudent(@PathVariable Long id) {
        log.debug("REST request to get the Progressions by student : {}", id);
        return progressionRepository.findAllByStudent(id);
    }

    /**
     * {@code GET  /progressions/:id} : get the "id" progression.
     *
     * @param the id of the progression to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the progression, or with status {@code 404 (Not Found)}.
     */

    /*@GetMapping("/studentAttendanceCount")
    List<Progression> getForAttendanceProgressions(@RequestParam LocalDate fromDate,
    @RequestParam LocalDate toDate) {
        log.debug("REST request to get the Progressions used to store the attendance od students : {}");
        return progressionRepository.findAllByAttendanceCount(fromDate,toDate);
    } */

    /**
     * {@code DELETE  /progressions/:id} : delete the "id" progression.
     *
     * @param id the id of the progression to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgression(@PathVariable("id") Long id) {
        log.debug("REST request to delete Progression : {}", id);
        progressionRepository.deleteById(id);
        progressionSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /progressions/_search?query=:query} : search for the progression corresponding
     * to the query.
     *
     * @param query the query of the progression search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Progression> searchProgressions(@RequestParam("query") String query) {
        log.debug("REST request to search Progressions for query {}", query);
        try {
            return StreamSupport.stream(progressionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    /*  @GetMapping("/multicriteriaForProgressions")
    public ResponseEntity<List<ProgressionDetailsDTO>> getSessionInstanceByMulticriteriaForProgressions(
        Pageable pageable,
        @RequestParam(required = false) Long siteId,
        @RequestParam(required = false, defaultValue = "") String gender,
        @RequestParam(required = false) String sessionDate,
        @RequestParam(required = false, defaultValue = "") String sessionType,
        @RequestParam(required = false) Long sessionId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long sessionInstanceId,
        @RequestParam(required = false) Boolean isForAttendance,
        Principal principal
    ) {
        // Validate principal
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        // Find user with optional null check
        UserCustom userCustom = userCustomRepository
            .findUserCustomByLogin(principal.getName())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    
        try {
            // Check if user is an admin
            boolean isAdmin = userCustom.getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getName()));
    
            // If the user is not an admin and userId is not provided, use the authenticated user's ID
            if (!isAdmin) {
                userId = userCustom.getId();
                siteId = userCustom.getSite13().getId();
            }
    
            // Convert gender string to enum if present
            TargetedGender genderEnum = gender != null && !gender.isEmpty() ? TargetedGender.valueOf(gender) : null;
    
            // Convert sessionType string to enum if present
            SessionType sessionTypeEnum = sessionType != null && !sessionType.isEmpty() ? SessionType.valueOf(sessionType) : null;
    
            // Parse date if present
            Integer year = null;
            Integer month = null;
            if (sessionDate != null && !sessionDate.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(sessionDate);
                    year = date.getYear();
                    month = date.getMonthValue();
                } catch (DateTimeParseException e) {
                    log.error("Invalid date format: {}", sessionDate);
                    return ResponseEntity.badRequest().build();
                }
            }
    
            // Use the findProgressions method to get ProgressionDetailsDTO list directly
            List<ProgressionDetailsDTO> result = progressionRepository.findProgressions(
                siteId,
                userId,
                isForAttendance,
                null, // tilawaType
                null, // examType
                null, // fromSourate
                null, // toSourate
                sessionInstanceId
            );
    
            // Log the results
            if (result.isEmpty()) {
                log.info("No session instance progressions found.");
            } else {
                log.info(
                    "Query results - Parameters: siteId={}, gender={}, year={}, month={}, sessionType={}, " +
                    "sessionId={}, userId={}, isForAttendance={}, resultSize={} ",
                    siteId,
                    genderEnum,
                    year,
                    month,
                    sessionTypeEnum,
                    sessionId,
                    userId,
                    isForAttendance,
                    result.size()
                );
            }
    
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            log.error("Entity not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching session instances", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
*/

    @GetMapping("/multicriteriaForProgressions")
    public ResponseEntity<List<ProgressionDetailsDTO>> getSessionInstanceByMulticriteriaForProgressions(
        Pageable pageable,
        @RequestParam(required = false) Long siteId,
        @RequestParam(required = false, defaultValue = "") String gender,
        @RequestParam(required = false) String sessionDate,
        @RequestParam(required = false, defaultValue = "") String sessionType,
        @RequestParam(required = false) Long sessionId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long sessionInstanceId,
        @RequestParam(required = false) Boolean isForAttendance
    ) {
        try {
            // Convert gender string to enum if present
            TargetedGender genderEnum = gender != null && !gender.isEmpty() ? TargetedGender.valueOf(gender) : null;

            // Convert sessionType string to enum if present
            SessionType sessionTypeEnum = sessionType != null && !sessionType.isEmpty() ? SessionType.valueOf(sessionType) : null;

            // Parse date if present
            Integer year = null;
            Integer month = null;
            if (sessionDate != null && !sessionDate.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(sessionDate);
                    year = date.getYear();
                    month = date.getMonthValue();
                } catch (DateTimeParseException e) {
                    log.error("Invalid date format: {}", sessionDate);
                    return ResponseEntity.badRequest().build();
                }
            }

            // Use the findProgressions method to get ProgressionDetailsDTO list directly
            List<ProgressionDetailsDTO> result = progressionRepository.findProgressions(
                siteId,
                userId,
                isForAttendance,
                null, // tilawaType
                null, // examType
                null, // fromSourate
                null, // toSourate
                sessionInstanceId
            );

            // Log the results
            if (result.isEmpty()) {
                log.info("No session instance progressions found.");
            } else {
                log.info(
                    "Query results - Parameters: siteId={}, gender={}, year={}, month={}, sessionType={}, " +
                    "sessionId={}, userId={}, isForAttendance={}, resultSize={} ",
                    siteId,
                    genderEnum,
                    year,
                    month,
                    sessionTypeEnum,
                    sessionId,
                    userId,
                    isForAttendance,
                    result.size()
                );
            }

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching session instances", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
