package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.RemoteSessionDto;
import com.wiam.lms.domain.dto.SessionInstanceUniqueDto;
import com.wiam.lms.domain.dto.custom.ChatMemberDto;
import com.wiam.lms.domain.enumeration.Attendance;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.repository.GroupRepository;
import com.wiam.lms.repository.ProgressionRepository;
import com.wiam.lms.repository.SessionInstanceRepository;
import com.wiam.lms.repository.SessionRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.SessionInstanceSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private final SessionRepository sessionRepository;

    public SessionInstanceResource(
        UserCustomRepository userCustomRepository,
        GroupRepository groupRepository,
        ProgressionRepository progressionRepository,
        SessionInstanceRepository sessionInstanceRepository,
        SessionInstanceSearchRepository sessionInstanceSearchRepository,
        SessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
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

    @GetMapping("{id}/all")
    public List<RemoteSessionDto> getAllSessionsInstances(@PathVariable Long id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        log.debug("REST request to get all Session instances for the given student id");
        // getting the list of the student groups
        UserCustom userCustom = userCustomRepository.findByIdforGroup(id).get();
        List<Group> myGroups = new ArrayList<Group>();
        for (Group group : userCustom.getGroups()) myGroups.add(group);

        List<RemoteSessionDto> allSessionDtos = new ArrayList<RemoteSessionDto>();
        List<SessionInstance> allSessions = sessionInstanceRepository.findRemoteSessionInstances(myGroups);
        for (SessionInstance sessionInstance : allSessions) {
            RemoteSessionDto allSessionDto = new RemoteSessionDto();
            if (sessionInstance.getSession1() != null) {
                allSessionDto.setGender(sessionInstance.getSession1().getTargetedGender().name());
            }
            allSessionDto.setTitle(sessionInstance.getTitle());
            allSessionDto.setSessionDate(sessionInstance.getSessionDate());
            allSessionDto.setDay(sessionInstance.getSessionDate().getDayOfWeek().getValue());
            if (sessionInstance.getGroup() != null) allSessionDto.setGroupName(sessionInstance.getGroup().getNameAr());
            allSessionDto.setIsActive(sessionInstance.getIsActive());
            allSessionDto.setInfo(sessionInstance.getInfo());

            if (sessionInstance.getSessionLink() != null) {
                allSessionDto.setLink(sessionInstance.getSessionLink().getLink());
            }

            allSessionDto.setSessionStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(sessionInstance.getStartTime()));
            allSessionDto.setDuration(sessionInstance.getDuration());
            allSessionDtos.add(allSessionDto);
        }
        return allSessionDtos;
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
        List<SessionInstance> sessionInstances = sessionInstanceRepository.findSessionInstanceByDateGroupProfessorSession(
            sessionInstance.getSessionDate(),
            sessionInstance.getSession1().getId(),
            sessionInstance.getGroup().getId(),
            sessionInstance.getProfessor().getId()
        );
        if (!sessionInstances.isEmpty()) {
            throw new BadRequestAlertException("Already exists", ENTITY_NAME, "idexists");
        }
        SessionInstance result = sessionInstanceRepository.save(sessionInstance);
        //sessionInstanceSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/session-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/initializeSessionInstances")
    public ResponseEntity<List<SessionInstance>> createSessionInstances(
        @RequestParam("sessionId") Long sessionId,
        @RequestParam("groupId") Long groupId,
        @RequestParam("professorId") Long professorId
    ) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<UserCustom> professor = userCustomRepository.findById(professorId);

        if (
            session.isPresent() &&
            group.isPresent() &&
            professor.isPresent() &&
            session.get().getPeriodStartDate() != null &&
            session.get().getPeriodeEndDate() != null
        ) {
            LocalDate startDate = session.get().getPeriodStartDate();
            LocalDate endDate = session.get().getPeriodeEndDate();
            List<SessionInstance> createdInstances = new ArrayList<>();

            // Iterate through all dates between start and end
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // Check if the session is programmed for the current day of the week
                DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

                boolean isValidDay = false;
                switch (currentDayOfWeek) {
                    case MONDAY:
                        isValidDay = (session.get().getMonday() != null) ? session.get().getMonday() : false;
                        break;
                    case TUESDAY:
                        isValidDay = (session.get().getTuesday() != null) ? session.get().getTuesday() : false;
                        break;
                    case WEDNESDAY:
                        isValidDay = (session.get().getWednesday() != null) ? session.get().getWednesday() : false;
                        break;
                    case THURSDAY:
                        isValidDay = (session.get().getThursday() != null) ? session.get().getThursday() : false;
                        break;
                    case FRIDAY:
                        isValidDay = (session.get().getFriday() != null) ? session.get().getFriday() : false;
                        break;
                    case SATURDAY:
                        isValidDay = (session.get().getSaturday() != null) ? session.get().getSaturday() : false;
                        break;
                    case SUNDAY:
                        isValidDay = (session.get().getSunday() != null) ? session.get().getSunday() : false;
                        break;
                    default:
                        break;
                }

                // If the session is programmed for the current day, create a session instance
                if (isValidDay) {
                    SessionInstance si = new SessionInstance();
                    si.setSession1(session.get());
                    si.setGroup(group.get());
                    si.setProfessor(professor.get());
                    si.setSessionDate(currentDate); // Assuming you have a date field in SessionInstance
                    si.setTitle(
                        group.get().getNameAr() +
                        '_' +
                        professor.get().getFirstName() +
                        '_' +
                        professor.get().getLastName() +
                        '_' +
                        currentDate
                    );
                    si.setSite16(session.get().getSite14());
                    si.setDuration(0);
                    si.attendance(Attendance.ABSENT);
                    si.setStartTime(null);
                    si.isActive(false);
                    si.startTime(ZonedDateTime.now());

                    // Check if a session instance already exists for the given date, group, professor, and session
                    List<SessionInstance> sessionInstances = sessionInstanceRepository.findSessionInstanceByDateGroupProfessorSession(
                        currentDate,
                        session.get().getId(),
                        group.get().getId(),
                        professor.get().getId()
                    );
                    if (sessionInstances.isEmpty()) {
                        SessionInstance savedInstance = sessionInstanceRepository.save(si);
                        createdInstances.add(savedInstance);
                    }
                }

                // Move to the next day
                currentDate = currentDate.plusDays(1);
            }

            return ResponseEntity.ok(createdInstances);
        }

        return ResponseEntity.badRequest().build();
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
        //sessionInstanceSearchRepository.index(result);
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
                //sessionInstanceSearchRepository.index(savedSessionInstance);
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

    @GetMapping("/byRole")
    public ResponseEntity<List<SessionInstance>> getAllSessionInstancesByRole(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        Pageable pageable,
        Principal principal
    ) {
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();
        List<SessionInstance> sessioninstanceList = new ArrayList<SessionInstance>();
        long totalElements = 0;
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                List<Group> myGroups = new ArrayList<Group>();
                for (Group group : userCustom.getGroups()) myGroups.add(group);
                System.out.println("HHHHHHHHHHHHHHH:" + myGroups.size());
                Page<SessionInstance> sessioninstance = sessionInstanceRepository.findRemoteSessionInstancesByRole(myGroups, pageable);
                if (sessioninstance != null) {
                    sessioninstanceList = sessioninstance.getContent();
                    totalElements = sessioninstance.getTotalElements();
                }
            } else {
                Page<SessionInstance> sessioninstance = sessionInstanceRepository.findAll(pageable);
                if (sessioninstance != null) {
                    sessioninstanceList = sessioninstance.getContent();
                    totalElements = sessioninstance.getTotalElements();
                }
            }
        }
        log.debug("REST request to get all SessionInstance");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + totalElements);
        return new ResponseEntity<>(sessioninstanceList, headers, HttpStatus.OK);
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
        Optional<SessionInstance> sessionInstance = sessionInstanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sessionInstance);
    }

    /*@GetMapping("/{id}/chatMembers/{userId}")
    public List<ChatMemberDto> getSessionInstanceChatMembers(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.debug("REST request to get SessionInstance Chat Memebers : {}", id);
        Optional<SessionInstance> sessionInstance = sessionInstanceRepository.findOneWithEagerRelationships(id);
        List<ChatMemberDto> chatMembers = new ArrayList<ChatMemberDto>();
        Optional<UserCustom> userCustom = userCustomRepository.findById(userId);
        if (sessionInstance.isPresent() && userCustom.isPresent()) {
            // professor
            ChatMemberDto professor = new ChatMemberDto();
            professor.setId(sessionInstance.get().getProfessor().getId());
            professor.setFirstName(sessionInstance.get().getProfessor().getFirstName());
            professor.setLastName(sessionInstance.get().getProfessor().getLastName());
            professor.setRole(Role.INSTRUCTOR);
            chatMembers.add(professor);
            // students
            if (userCustom.get().getRole() == Role.INSTRUCTOR || userCustom.get().getRole() == Role.SUPERVISOR) {
                for (UserCustom student : sessionInstance.get().getGroup().getElements()) {
                    ChatMemberDto member = new ChatMemberDto();
                    member.setId(student.getId());
                    member.setFirstName(student.getFirstName());
                    member.setLastName(student.getLastName());
                    member.setRole(Role.STUDENT);
                    member.setFather(student.getFather());
                    member.setMother(student.getMother());
                    member.setWali(student.getWali());
                    member.setLogin(student.getLogin());
                    chatMembers.add(member);
                }
            } else if (userCustom.get().getRole() == Role.STUDENT) {
                UserCustom student = sessionInstance
                    .get()
                    .getGroup()
                    .getElements()
                    .stream()
                    .filter(e -> e.getId().equals(userCustom.get().getId()))
                    .findFirst()
                    .orElse(null); // Returns null if no match is found

                ChatMemberDto member = new ChatMemberDto();
                member.setId(student.getId());
                member.setFirstName(student.getFirstName());
                member.setLastName(student.getLastName());
                member.setRole(Role.STUDENT);
                member.setFather(student.getFather());
                member.setMother(student.getMother());
                member.setWali(student.getWali());
                member.setLogin(student.getLogin());
                chatMembers.add(member);
            } else if (userCustom.get().getRole() == Role.PARENT) {
                List<UserCustom> students = sessionInstance
                    .get()
                    .getGroup()
                    .getElements()
                    .stream()
                    .filter(e ->
                        (e.getFather() != null && userCustom.get().getId().equals(e.getFather().getId())) ||
                        (e.getMother() != null && userCustom.get().getId().equals(e.getMother().getId())) ||
                        (e.getWali() != null && userCustom.get().getId().equals(e.getWali().getId()))
                    )
                    .collect(Collectors.toList());

                if (students.size() > 0) {
                    for (UserCustom student : students) {
                        ChatMemberDto member = new ChatMemberDto();
                        member.setId(student.getId());
                        member.setFirstName(student.getFirstName());
                        member.setLastName(student.getLastName());
                        member.setRole(Role.STUDENT);
                        member.setFather(student.getFather());
                        member.setMother(student.getMother());
                        member.setWali(student.getWali());
                        member.setLogin(student.getLogin());
                        chatMembers.add(member);
                    }
                }
            }
            // Supervisors
            for (UserCustom supervisor : sessionInstance.get().getSession1().getEmployees()) {
                ChatMemberDto member = new ChatMemberDto();
                member.setId(supervisor.getId());
                member.setFirstName(supervisor.getFirstName());
                member.setLastName(supervisor.getLastName());
                member.setRole(Role.SUPERVISOR);
                chatMembers.add(member);
            }
        }
        return chatMembers;
    }*/

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

    @GetMapping("/multicriteria")
    public ResponseEntity<List<SessionInstance>> getSessionInstanceByMulticriteria(
        Pageable pageable,
        @RequestParam(required = false) Long siteId,
        @RequestParam(required = false, defaultValue = "") String gender,
        @RequestParam(required = false) String sessionDate,
        @RequestParam(required = false, defaultValue = "") String sessionType,
        @RequestParam(required = false) Long sessionId,
        @RequestParam(required = false) Long userId, // Added userId as an optional parameter
        @RequestParam(required = false, defaultValue = "false") boolean isForAttendance, // Added the isForAttendance parameter
        Principal principal // Principal added to get authenticated user info
    ) {
        // Validate principal
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Fetch the authenticated user
            UserCustom userCustom = userCustomRepository
                .findUserCustomByLogin(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            // Check if user is an admin
            boolean isAdmin = userCustom.getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getName()));

            // Convert gender string to enum if present
            TargetedGender genderEnum = null;
            if (gender != null && !gender.isEmpty()) {
                try {
                    genderEnum = TargetedGender.valueOf(gender);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

            // Convert sessionType string to enum if present
            SessionType sessionTypeEnum = null;
            if (sessionType != null && !sessionType.isEmpty()) {
                try {
                    sessionTypeEnum = SessionType.valueOf(sessionType);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

            // Parse date if present
            Integer year = null;
            Integer month = null;
            if (sessionDate != null && !sessionDate.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(sessionDate);
                    year = date.getYear();
                    month = date.getMonthValue();
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

            // If the user is not an admin, use the Principal's user ID
            if (!isAdmin) {
                userId = userCustom.getId(); // Use the authenticated user's ID
                siteId = userCustom.getSite13().getId();
            }

            // Call the repository method, including the isForAttendance filter
            List<SessionInstance> result = sessionInstanceRepository.findSessionInstanceMulticreteria(
                siteId,
                genderEnum,
                year,
                month,
                sessionTypeEnum,
                sessionId,
                userId,
                isForAttendance // Include the isForAttendance parameter in the repository query
            );

            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*@GetMapping("/unique-for-date-group-professor")
    public List<SessionInstance> getSessionInstanceByCriteria(
        @RequestParam("sessionDate") LocalDate sessionDate,
        @RequestParam("sessionId") Long sessionId,
        @RequestParam("groupId") Long groupId,
        @RequestParam("professorId") Long professorId
    ) {
        return sessionInstanceRepository.findSessionInstanceByDateGroupProfessorSession(
            sessionDate,
            sessionId,
            groupId,
            professorId
        );
    }*/

    @GetMapping("/unique-for-date-group-professor")
    public boolean checkIfSessionInstanceExists(
        @RequestParam("sessionDate") LocalDate sessionDate,
        @RequestParam("sessionId") Long sessionId,
        @RequestParam("groupId") Long groupId,
        @RequestParam("professorId") Long professorId
    ) {
        List<SessionInstance> sessionInstances = sessionInstanceRepository.findSessionInstanceByDateGroupProfessorSession(
            sessionDate,
            sessionId,
            groupId,
            professorId
        );

        return !sessionInstances.isEmpty(); // Return true if sessionInstance exists, otherwise false
    }

    /**
     * {@code GET  /session-instances/:id} : get the "id" sessionInstance.
     *
     * @param id the id of the sessionInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}/byStudent")
    public List<SessionInstance> getSessionInstanceByStudent(@PathVariable("id") Long id) {
        log.debug("REST request to get all SessionsInstances for the given student id: {}", id);
        // getting the list of the student groups
        UserCustom userCustom = userCustomRepository.findByIdforGroup(id).get();
        List<Group> myGroups = new ArrayList<Group>();
        List<SessionInstance> sessionInstancesUser = new ArrayList<>();
        for (Group group : userCustom.getGroups()) myGroups.add(group);
        myGroups.forEach(group -> {
            List<SessionInstance> sessionInstances = sessionInstanceRepository.findByGroupId(group.getId());
            sessionInstancesUser.addAll(sessionInstances);
        });

        return sessionInstancesUser;
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
        //sessionInstanceSearchRepository.deleteFromIndexById(id);
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
