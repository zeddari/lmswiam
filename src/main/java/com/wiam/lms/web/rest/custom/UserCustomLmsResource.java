package com.wiam.lms.web.rest.custom;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.Role;
import com.wiam.lms.domain.enumeration.Sex;
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

    public UserCustomLmsResource(UserCustomLmsRepository userCustomLmsRepository, UserCustomSearchRepository userCustomSearchRepository) {
        this.userCustomLmsRepository = userCustomLmsRepository;
        this.userCustomSearchRepository = userCustomSearchRepository;
    }

    @GetMapping("/{role}/role")
    public ResponseEntity<List<UserCustom>> getAllUserCustomsByUserType(
        Pageable pageable,
        @PathVariable Role role,
        @RequestParam Long siteId,
        @RequestParam AccountStatus accountStatus,
        @RequestParam Sex sex
    ) {
        List<UserCustom> users = new ArrayList<UserCustom>();
        users = userCustomLmsRepository.getUsers(pageable, role, siteId, accountStatus, sex).getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + userCustomLmsRepository.getUsers(pageable, role, siteId, accountStatus, sex).getTotalElements());
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @GetMapping("/byRole")
    public List<UserCustom> getAllUserCustomsByUserType(@RequestParam Role role) {
        return userCustomLmsRepository.findByRole(role);
    }

    @GetMapping("/byRoleSite")
    public List<UserCustom> getAllUserCustomsByUserTypeSite(@RequestParam Role role, @RequestParam Long siteId) {
        return userCustomLmsRepository.findByRoleSite(role, siteId);
    }
}
