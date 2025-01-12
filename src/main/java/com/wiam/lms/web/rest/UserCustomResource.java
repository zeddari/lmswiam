package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.custom.ChatMemberDto;
import com.wiam.lms.domain.dto.custom.ChatRoomDto;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.GroupType;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.repository.SessionRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.UserCustomSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wiam.lms.domain.UserCustom}.
 */
@RestController
@RequestMapping("/api/user-customs")
@Transactional
public class UserCustomResource {

    private final Logger log = LoggerFactory.getLogger(UserCustomResource.class);

    private static final String ENTITY_NAME = "userCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCustomRepository userCustomRepository;

    private final UserCustomSearchRepository userCustomSearchRepository;
    private final SessionRepository sessionRepository;

    public UserCustomResource(
        UserCustomRepository userCustomRepository,
        UserCustomSearchRepository userCustomSearchRepository,
        SessionRepository sessionRepository
    ) {
        this.userCustomRepository = userCustomRepository;
        this.userCustomSearchRepository = userCustomSearchRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * {@code POST  /user-customs} : Create a new userCustom.
     *
     * @param userCustom the userCustom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCustom, or with status {@code 400 (Bad Request)} if the userCustom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserCustom> createUserCustom(@Valid @RequestBody UserCustom userCustom) throws URISyntaxException {
        log.debug("REST request to save UserCustom : {}", userCustom);
        if (userCustom.getId() != null) {
            throw new BadRequestAlertException("A new userCustom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserCustom result = userCustomRepository.save(userCustom);
        // // userCustomSearchRepository.index(result);

        return ResponseEntity
            .created(new URI("/api/user-customs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-customs/:id} : Updates an existing userCustom.
     *
     * @param id the id of the userCustom to save.
     * @param userCustom the userCustom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustom,
     * or with status {@code 400 (Bad Request)} if the userCustom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCustom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserCustom> updateUserCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserCustom userCustom
    ) throws URISyntaxException {
        log.debug("REST request to update UserCustom : {}, {}", id, userCustom);
        if (userCustom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCustom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserCustom result = userCustomRepository.save(userCustom);

        // // userCustomSearchRepository.index(result);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userCustom.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<UserCustom> updateActivationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @PathVariable(value = "status", required = false) final boolean status
    ) throws URISyntaxException {
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!userCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<UserCustom> uc = userCustomRepository.findById(id);
        uc.get().setActivated(status);
        UserCustom result = userCustomRepository.save(uc.get());
        // // userCustomSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-customs/:id} : Partial updates given fields of an existing userCustom, field will ignore if it is null
     *
     * @param id the id of the userCustom to save.
     * @param userCustom the userCustom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCustom,
     * or with status {@code 400 (Bad Request)} if the userCustom is not valid,
     * or with status {@code 404 (Not Found)} if the userCustom is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCustom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserCustom> partialUpdateUserCustom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserCustom userCustom
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserCustom partially : {}, {}", id, userCustom);
        if (userCustom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCustom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCustomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserCustom> result = userCustomRepository
            .findById(userCustom.getId())
            .map(existingUserCustom -> {
                if (userCustom.getFirstName() != null) {
                    existingUserCustom.setFirstName(userCustom.getFirstName());
                }
                if (userCustom.getLastName() != null) {
                    existingUserCustom.setLastName(userCustom.getLastName());
                }
                if (userCustom.getCode() != null) {
                    existingUserCustom.setCode(userCustom.getCode());
                }

                if (userCustom.getRole() != null) {
                    existingUserCustom.setRole(userCustom.getRole());
                }
                if (userCustom.getAccountStatus() != null) {
                    existingUserCustom.setAccountStatus(userCustom.getAccountStatus());
                }
                if (userCustom.getPhoneNumber1() != null) {
                    existingUserCustom.setPhoneNumber1(userCustom.getPhoneNumber1());
                }
                if (userCustom.getPhoneNumver2() != null) {
                    existingUserCustom.setPhoneNumver2(userCustom.getPhoneNumver2());
                }
                if (userCustom.getSex() != null) {
                    existingUserCustom.setSex(userCustom.getSex());
                }
                if (userCustom.getBirthdate() != null) {
                    existingUserCustom.setBirthdate(userCustom.getBirthdate());
                }
                if (userCustom.getPhoto() != null) {
                    existingUserCustom.setPhoto(userCustom.getPhoto());
                }
                if (userCustom.getPhotoContentType() != null) {
                    existingUserCustom.setPhotoContentType(userCustom.getPhotoContentType());
                }
                if (userCustom.getAddress() != null) {
                    existingUserCustom.setAddress(userCustom.getAddress());
                }
                if (userCustom.getFacebook() != null) {
                    existingUserCustom.setFacebook(userCustom.getFacebook());
                }
                if (userCustom.getTelegramUserCustomId() != null) {
                    existingUserCustom.setTelegramUserCustomId(userCustom.getTelegramUserCustomId());
                }
                if (userCustom.getTelegramUserCustomName() != null) {
                    existingUserCustom.setTelegramUserCustomName(userCustom.getTelegramUserCustomName());
                }
                if (userCustom.getBiography() != null) {
                    existingUserCustom.setBiography(userCustom.getBiography());
                }
                if (userCustom.getBankAccountDetails() != null) {
                    existingUserCustom.setBankAccountDetails(userCustom.getBankAccountDetails());
                }

                if (userCustom.getSite13() != null) {
                    existingUserCustom.setSite13(userCustom.getSite13());
                }
                if (userCustom.getLanguages() != null) {
                    existingUserCustom.setLanguages(userCustom.getLanguages());
                }
                if (userCustom.getCity() != null) {
                    existingUserCustom.setCity(userCustom.getCity());
                }
                if (userCustom.getNationality() != null) {
                    existingUserCustom.setNationality(userCustom.getNationality());
                }
                if (userCustom.getCountry() != null) {
                    existingUserCustom.setCountry(userCustom.getCountry());
                }
                if (userCustom.getJob() != null) {
                    existingUserCustom.setJob(userCustom.getJob());
                }
                if (userCustom.getDepartement2() != null) {
                    existingUserCustom.setDepartement2(userCustom.getDepartement2());
                }
                if (userCustom.getEmail() != null) {
                    existingUserCustom.setEmail(userCustom.getEmail());
                }
                if (userCustom.getLangKey() != null) {
                    existingUserCustom.setLangKey(userCustom.getLangKey());
                }
                if (userCustom.getLogin() != null) {
                    existingUserCustom.setLogin(userCustom.getLogin());
                }
                if (userCustom.getAuthorities() != null) {
                    existingUserCustom.setAuthorities(userCustom.getAuthorities());
                }

                existingUserCustom.setActivated(userCustom.isActivated());

                return existingUserCustom;
            })
            .map(userCustomRepository::save)
            .map(savedUserCustom -> {
                // userCustomSearchRepository.index(savedUserCustom);
                return savedUserCustom;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userCustom.getId().toString())
        );
    }

    /*@GetMapping("/{userId}/chatMembers")
    public List<ChatMemberDto> getSessionInstanceChatMembers(@PathVariable("userId") Long userId) {
        List<ChatMemberDto> chatMembers = new ArrayList<>();

        // Fetch the user by ID
        Optional<UserCustom> userCustomOpt = userCustomRepository.findById(userId);

        if (userCustomOpt.isPresent()) {
            UserCustom userCustom = userCustomOpt.get();
            Set<Session> uniqueUserSessions = new HashSet<>();

            // Determine user role and fetch relevant sessions
            switch (userCustom.getRole()) {
                case INSTRUCTOR:
                    uniqueUserSessions.addAll(sessionRepository.findSessionsByProfessor(userCustom));
                    break;

                case STUDENT:
                    Set<Group> studentGroups = userCustom.getGroups();
                    if (studentGroups != null && !studentGroups.isEmpty()) {
                        List<Long> sessionGroupIds = studentGroups.stream()
                            .map(Group::getId)
                            .toList();
                        uniqueUserSessions.addAll(sessionRepository.findSessionsByUserGroups(sessionGroupIds));
                    }
                    break;

                case SUPERVISOR:
                    uniqueUserSessions.addAll(sessionRepository.findSessionsByEmployee(userCustom));
                    break;

                case PARENT:
                    List<UserCustom> childrenList = userCustomRepository.findChildrenList(userCustom);
                    if (!childrenList.isEmpty()) {
                        for (UserCustom child : childrenList) {
                            Set<Group> childrenGroups = child.getGroups();
                            if (childrenGroups != null && !childrenGroups.isEmpty()) {
                                List<Long> sessionGroupIds = childrenGroups.stream()
                                    .map(Group::getId)
                                    .toList();
                                uniqueUserSessions.addAll(sessionRepository.findSessionsByUserGroups(sessionGroupIds));
                            }
                        }
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported role: " + userCustom.getRole());
            }

            // Fetch chat members for the sessions
            chatMembers = getSessionChatMembers(new ArrayList<>(uniqueUserSessions));
        }

        return chatMembers;
    }

    
    private List<ChatMemberDto> getSessionChatMembers(List<Session> userSessions) {
        Set<ChatMemberDto> uniqueChatMembers = new HashSet<>();
    
        for (Session session : userSessions) {
            // Add students from session groups
            Set<Group> studentGroups = session.getGroups();
            if (studentGroups != null && !studentGroups.isEmpty()) {
                for (Group group : studentGroups) {
                    if (group.getGroupType().equals(GroupType.STUDENT)) {
                        Set<UserCustom> students = group.getElements();
                        if (students != null && !students.isEmpty()) {
                            for (UserCustom student : students) {
                                ChatMemberDto chatMemberDto = new ChatMemberDto();
                                chatMemberDto.setId(student.getId());
                                chatMemberDto.setFirstName(student.getFirstName());
                                chatMemberDto.setLastName(student.getLastName());
                                chatMemberDto.setRole(Role.STUDENT);
                                chatMemberDto.setFather(student.getFather());
                                chatMemberDto.setMother(student.getMother());
                                chatMemberDto.setWali(student.getWali());
                                uniqueChatMembers.add(chatMemberDto);
                            }
                        }
                    }
                }
            }
    
            // Add employees
            Set<UserCustom> employees = session.getEmployees();
            if (employees != null && !employees.isEmpty()) {
                for (UserCustom employee : employees) {
                    ChatMemberDto chatMemberDto = new ChatMemberDto();
                    chatMemberDto.setId(employee.getId());
                    chatMemberDto.setFirstName(employee.getFirstName());
                    chatMemberDto.setLastName(employee.getLastName());
                    chatMemberDto.setRole(Role.SUPERVISOR); // Adjust role as needed
                    uniqueChatMembers.add(chatMemberDto);
                }
            }
    
            // Add professors
            Set<UserCustom> professors = session.getProfessors();
            if (professors != null && !professors.isEmpty()) {
                for (UserCustom professor : professors) {
                    ChatMemberDto chatMemberDto = new ChatMemberDto();
                    chatMemberDto.setId(professor.getId());
                    chatMemberDto.setFirstName(professor.getFirstName());
                    chatMemberDto.setLastName(professor.getLastName());
                    chatMemberDto.setRole(Role.INSTRUCTOR); // Adjust role as needed
                    uniqueChatMembers.add(chatMemberDto);
                }
            }
        }
    
        // Convert the set to a list before returning
        return new ArrayList<>(uniqueChatMembers);
    }
    */

    @GetMapping("/{userId}/chatRooms")
    public List<ChatRoomDto> getSessionInstanceChatRooms(@PathVariable("userId") Long userId) {
        List<ChatRoomDto> chatRooms = new ArrayList<>();

        // Fetch the user by ID
        Optional<UserCustom> userCustomOpt = userCustomRepository.findById(userId);

        if (userCustomOpt.isPresent()) {
            UserCustom userCustom = userCustomOpt.get();
            Set<Session> uniqueUserSessions = new HashSet<>();

            // Determine user role and fetch relevant sessions
            switch (userCustom.getRole()) {
                case INSTRUCTOR:
                    uniqueUserSessions.addAll(sessionRepository.findSessionsByProfessor(userCustom));
                    break;
                case STUDENT:
                    Set<Group> studentGroups = userCustom.getGroups();
                    if (studentGroups != null && !studentGroups.isEmpty()) {
                        List<Long> sessionGroupIds = studentGroups.stream().map(Group::getId).toList();
                        uniqueUserSessions.addAll(sessionRepository.findSessionsByUserGroups(sessionGroupIds));
                    }
                    break;
                case SUPERVISOR:
                    uniqueUserSessions.addAll(sessionRepository.findSessionsByEmployee(userCustom));
                    break;
                case PARENT:
                    List<UserCustom> childrenList = userCustomRepository.findChildrenList(userCustom);
                    if (!childrenList.isEmpty()) {
                        for (UserCustom child : childrenList) {
                            Set<Group> childrenGroups = child.getGroups();
                            if (childrenGroups != null && !childrenGroups.isEmpty()) {
                                List<Long> sessionGroupIds = childrenGroups.stream().map(Group::getId).toList();
                                uniqueUserSessions.addAll(sessionRepository.findSessionsByUserGroups(sessionGroupIds));
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported role: " + userCustom.getRole());
            }

            // Fetch chatrooms grouped by group name
            chatRooms = getSessionChatRooms(new ArrayList<>(uniqueUserSessions), userCustom);
        }

        return chatRooms;
    }

    private List<ChatRoomDto> getSessionChatRooms(List<Session> userSessions, UserCustom userCustom) {
        List<ChatRoomDto> chatRooms = new ArrayList<>();

        // Process each session
        for (Session session : userSessions) {
            String sessionName = session.getTitle(); // Assuming Session has a `getName()` method

            // Process groups in the session
            Set<Group> sessionGroups = session.getGroups();
            if (sessionGroups != null && !sessionGroups.isEmpty()) {
                for (Group group : sessionGroups) {
                    String groupName = group.getNameAr(); // Assuming Group has a `getName()` method

                    // Map group members to ChatMemberDto
                    Set<UserCustom> groupMembers = group.getElements();
                    List<ChatMemberDto> chatMembers = new ArrayList<>();
                    if (groupMembers != null && !groupMembers.isEmpty()) {
                        for (UserCustom member : groupMembers) {
                            ChatMemberDto chatMemberDto = new ChatMemberDto();
                            chatMemberDto.setId(member.getId());
                            chatMemberDto.setFirstName(member.getFirstName());
                            chatMemberDto.setLastName(member.getLastName());
                            chatMemberDto.setRole(member.getRole());

                            chatMemberDto.setFather(member.getFather());
                            chatMemberDto.setMother(member.getMother());
                            chatMemberDto.setWali(member.getWali());
                            chatMembers.add(chatMemberDto);
                        }
                    }

                    if (userCustom.getRole().equals(Role.PARENT)) {
                        // Retrieve the parent's children
                        List<UserCustom> children = userCustomRepository.findChildrenList(userCustom);

                        // Extract the IDs of the parent's children for filtering
                        Set<Long> childrenIds = children.stream().map(UserCustom::getId).collect(Collectors.toSet());

                        // Filter chat members to include only those that are the parent's children
                        chatMembers =
                            chatMembers.stream().filter(member -> childrenIds.contains(member.getId())).collect(Collectors.toList());
                    }

                    // Add employees to chat members
                    Set<UserCustom> employees = session.getEmployees();
                    if (employees != null && !employees.isEmpty()) {
                        for (UserCustom employee : employees) {
                            ChatMemberDto chatMemberDto = new ChatMemberDto();
                            chatMemberDto.setId(employee.getId());
                            chatMemberDto.setFirstName(employee.getFirstName());
                            chatMemberDto.setLastName(employee.getLastName());
                            chatMemberDto.setRole(Role.SUPERVISOR); // Assuming employees are supervisors
                            chatMembers.add(chatMemberDto);
                        }
                    }

                    // Add professors to chat members
                    Set<UserCustom> professors = session.getProfessors();
                    if (professors != null && !professors.isEmpty()) {
                        for (UserCustom professor : professors) {
                            ChatMemberDto chatMemberDto = new ChatMemberDto();
                            chatMemberDto.setId(professor.getId());
                            chatMemberDto.setFirstName(professor.getFirstName());
                            chatMemberDto.setLastName(professor.getLastName());
                            chatMemberDto.setRole(Role.INSTRUCTOR);
                            chatMembers.add(chatMemberDto);
                        }
                    }

                    // Construct a ChatRoomDto
                    ChatRoomDto chatRoomDto = new ChatRoomDto();
                    chatRoomDto.setSessionName(sessionName);
                    chatRoomDto.setGroupName(groupName);
                    chatRoomDto.setChatMembers(chatMembers);

                    // Add to the chat rooms list
                    chatRooms.add(chatRoomDto);
                }
            }
        }

        return chatRooms;
    }

    /**
     * {@code GET  /user-customs} : get all the userCustoms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userCustoms in body.
     */
    @GetMapping("")
    public List<UserCustom> getAllUserCustoms(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all UserCustoms");
        if (eagerload) {
            return userCustomRepository.findAllWithEagerRelationships();
        } else {
            return userCustomRepository.findAll();
        }
    }

    /**
     * {@code GET  /user-customs/:id} : get the "id" userCustom.
     *
     * @param id the id of the userCustom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCustom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserCustom> getUserCustom(@PathVariable("id") Long id) {
        log.debug("REST request to get UserCustom : {}", id);
        Optional<UserCustom> userCustom = userCustomRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(userCustom);
    }

    @GetMapping("/check-unique/{field}")
    public ResponseEntity<?> checkUnique(@PathVariable String field, @RequestParam String value) {
        boolean isUnique = false;

        // Check if the field is "login" or "email" and call the corresponding method in the repository
        if ("login".equals(field)) {
            isUnique = !userCustomRepository.existsByLogin(value); // check if login is unique
        } else if ("email".equals(field)) {
            isUnique = !userCustomRepository.existsByEmail(value); // check if email is unique
        } else {
            return ResponseEntity.badRequest().body("Invalid field name");
        }

        // Return the appropriate response based on the uniqueness check
        return ResponseEntity.ok().body(isUnique ? "Unique" : "Not Unique");
    }

    @GetMapping("/account/{login}")
    public ResponseEntity<UserCustom> getUserCustomByLogin(@PathVariable("login") String login) {
        log.debug("REST request to get UserCustom by its Login: {}", login);
        Optional<UserCustom> userCustom = userCustomRepository.findUserCustomByLogin(login);
        return ResponseUtil.wrapOrNotFound(userCustom);
    }

    /**
     * {@code DELETE  /user-customs/:id} : delete the "id" userCustom.
     *
     * @param id the id of the userCustom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserCustom(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserCustom : {}", id);
        userCustomRepository.deleteById(id);
        // userCustomSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-customs/_search?query=:query} : search for the userCustom corresponding
     * to the query.
     *
     * @param query the query of the userCustom search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<UserCustom> searchUserCustoms(@RequestParam("query") String query) {
        log.debug("REST request to search UserCustoms for query {}", query);
        try {
            return StreamSupport.stream(userCustomSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/_ssearch")
    public ResponseEntity<List<UserCustom>> simpleSearchUserCustoms(
        Pageable pageable,
        @RequestParam(value = "firstName", required = false) String firstName,
        @RequestParam(value = "lastName", required = false) String lastName,
        @RequestParam(value = "role", required = false) Role role,
        @RequestParam(value = "siteId", required = false) Long siteId,
        @RequestParam(value = "accountStatus", required = false) boolean accountStatus,
        @RequestParam(value = "sex", required = false) Sex sex
    ) {
        log.debug("REST request to search UserCustoms for query {}");
        List<UserCustom> users = userCustomRepository
            .searchUsersWithNullFields(pageable, firstName, lastName, role, siteId, accountStatus, sex)
            .getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "X-Total-Count",
            "" +
            userCustomRepository
                .searchUsersWithNullFields(pageable, firstName, lastName, role, siteId, accountStatus, sex)
                .getTotalElements()
        );
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }
}
