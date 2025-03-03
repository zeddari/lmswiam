package com.wiam.lms.web.rest.custom;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.custom.ElementDto;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.repository.AuthorityRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.custom.UserCustomLmsRepository;
import com.wiam.lms.repository.search.UserCustomSearchRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link UserCustom}.
 */
@RestController
@RequestMapping("/api/user-customs/lms")
@Transactional
public class UserCustomLmsResource {

    private final Logger log = LoggerFactory.getLogger(UserCustomLmsResource.class);

    private static final String ENTITY_NAME = "userCustom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCustomLmsRepository userCustomLmsRepository;

    private final UserCustomSearchRepository userCustomSearchRepository;
    private final AuthorityRepository authorityRepository;

    public UserCustomLmsResource(
        UserCustomLmsRepository userCustomLmsRepository,
        UserCustomSearchRepository userCustomSearchRepository,
        AuthorityRepository authorityRepository
    ) {
        this.userCustomLmsRepository = userCustomLmsRepository;
        this.userCustomSearchRepository = userCustomSearchRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/{role}/role")
    public ResponseEntity<List<UserCustom>> getAllUserCustomsByUserType(
        Pageable pageable,
        @RequestParam Long siteId,
        @RequestParam Sex sex
    ) {
        List<UserCustom> users = new ArrayList<UserCustom>();
        users = userCustomLmsRepository.getUsers(pageable, siteId, sex).getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + userCustomLmsRepository.getUsers(pageable, siteId, sex).getTotalElements());
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @GetMapping("/byRoleSite")
    public List<ElementDto> getAllUserCustomsByUserTypeSite(@RequestParam Role role, @RequestParam Long siteId) {
        // Convert Role to corresponding Authority
        Authority authority = roleToAuthority(role);

        // Use the Authority entity in your repository query
        return userCustomLmsRepository.findByRoleSite(authority, siteId);
    }

    // Map Role to corresponding Authority
    Authority roleToAuthority(Role role) {
        switch (role) {
            case INSTRUCTOR:
                return authorityRepository
                    .findByName("ROLE_INSTRUCTOR")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_INSTRUCTOR"));
            case STUDENT:
                return authorityRepository
                    .findByName("ROLE_STUDENT")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_STUDENT"));
            case PARENT:
                return authorityRepository
                    .findByName("ROLE_PARENT")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_PARENT"));
            case MANAGEMENT:
                return authorityRepository
                    .findByName("ROLE_MANAGEMENT")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_MANAGEMENT"));
            case SUPERVISOR:
                return authorityRepository
                    .findByName("ROLE_SUPERVISOR")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_SUPERVISOR"));
            case MANAGER:
                return authorityRepository
                    .findByName("ROLE_MANAGER")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_MANAGER"));
            case SUPER_MANAGER:
                return authorityRepository
                    .findByName("ROLE_SUPER_MANAGER")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_SUPER_MANAGER"));
            case SPONSOR:
                return authorityRepository
                    .findByName("ROLE_SPONSOR")
                    .orElseThrow(() -> new IllegalArgumentException("Authority not found for role: ROLE_SPONSOR"));
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    /*@GetMapping("/byRoleAndSite")
    public List<ElementDto> getAllUserCustomsByUserRoleAndSite(@RequestParam Role role, @RequestParam Long siteId) {
        return userCustomLmsRepository.findByRoleAndSite(role, siteId);
    }*/
}
