package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.Tickets;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.GroupType;
import com.wiam.lms.repository.GroupRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.GroupSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.wiam.lms.domain.Group}.
 */
@RestController
@RequestMapping("/api/groups")
@Transactional
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupRepository groupRepository;
    private final UserCustomRepository userCustomRepository;

    private final GroupSearchRepository groupSearchRepository;

    public GroupResource(
        UserCustomRepository userCustomRepository,
        GroupRepository groupRepository,
        GroupSearchRepository groupSearchRepository
    ) {
        this.userCustomRepository = userCustomRepository;
        this.groupRepository = groupRepository;
        this.groupSearchRepository = groupSearchRepository;
    }

    /**
     * {@code POST  /groups} : Create a new group.
     *
     * @param group the group to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new group, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to save Group : {}", group);
        if (group.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Group result = groupRepository.save(group);
        //groupSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups/:id} : Updates an existing group.
     *
     * @param id the id of the group to save.
     * @param group the group to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated group,
     * or with status {@code 400 (Bad Request)} if the group is not valid,
     * or with status {@code 500 (Internal Server Error)} if the group couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Group group)
        throws URISyntaxException {
        log.debug("REST request to update Group : {}, {}", id, group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, group.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Group result = groupRepository.save(group);
        //groupSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, group.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing group, field will ignore if it is null
     *
     * @param id the id of the group to save.
     * @param group the group to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated group,
     * or with status {@code 400 (Bad Request)} if the group is not valid,
     * or with status {@code 404 (Not Found)} if the group is not found,
     * or with status {@code 500 (Internal Server Error)} if the group couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Group> partialUpdateGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Group group
    ) throws URISyntaxException {
        log.debug("REST request to partial update Group partially : {}, {}", id, group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, group.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Group> result = groupRepository
            .findById(group.getId())
            .map(existingGroup -> {
                if (group.getGroupType() != null) {
                    existingGroup.setGroupType(group.getGroupType());
                }
                if (group.getNameAr() != null) {
                    existingGroup.setNameAr(group.getNameAr());
                }
                if (group.getNameLat() != null) {
                    existingGroup.setNameLat(group.getNameLat());
                }
                if (group.getDescription() != null) {
                    existingGroup.setDescription(group.getDescription());
                }

                return existingGroup;
            })
            .map(groupRepository::save)
            .map(savedGroup -> {
                groupSearchRepository.index(savedGroup);
                return savedGroup;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, group.getId().toString())
        );
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("")
    public List<Group> getAllGroups(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Groups");
        /*if (eagerload) {
            return groupRepository.findAllWithEagerRelationships();
        } else {
            return groupRepository.findAll();
        }*/
        return groupRepository.findAll();
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/{siteId}/abstract")
    public List<Group> getAllGroupsAbstract(@PathVariable Long siteId) {
        log.debug("REST request to get all Groups abstract");
        return groupRepository.findAllAbstract(siteId);
    }

    @GetMapping("/myGroups")
    public ResponseEntity<List<Group>> getmyGroups(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        Pageable pageable,
        Principal principal,
        @RequestParam(name = "siteId", required = false) Long siteId,
        @RequestParam(name = "groupType", required = false) GroupType groupType
    ) {
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();
        List<Group> groupList = new ArrayList<Group>();
        long totalElements = 0;
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                if (userCustom.getGroups() != null) {
                    for (Group group : userCustom.getGroups()) {
                        if (
                            group.getSite11() != null && group.getSite11().getId() == siteId && group.getGroupType() == groupType
                        ) groupList.add(group);
                    }
                    totalElements = groupList.size();
                }
            } else {
                Page<Group> groups = groupRepository.findAllByGroupTypeAndSite(pageable, siteId, groupType);
                if (groups != null) {
                    groupList = groups.getContent();
                    totalElements = groups.getTotalElements();
                }
            }
        }
        log.debug("REST request to get all groups by siteId and groupType");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + totalElements);
        return new ResponseEntity<>(groupList, headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /groups/:id} : get the "id" group.
     *
     * @param id the id of the group to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the group, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<Group> group = groupRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(group);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" group.
     *
     * @param id the id of the group to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete Group : {}", id);
        groupRepository.deleteById(id);
        groupSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /groups/_search?query=:query} : search for the group corresponding
     * to the query.
     *
     * @param query the query of the group search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Group> searchGroups(@RequestParam("query") String query) {
        log.debug("REST request to search Groups for query {}", query);
        try {
            return StreamSupport.stream(groupSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
