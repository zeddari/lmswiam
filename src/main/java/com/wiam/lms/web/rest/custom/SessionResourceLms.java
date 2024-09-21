package com.wiam.lms.web.rest.custom;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.repository.SessionRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.SessionSearchRepository;
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
import org.bouncycastle.asn1.x509.Target;
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
 * REST controller for managing {@link com.wiam.lms.domain.Session}.
 */
@RestController
@RequestMapping("/api/sessions/lms")
@Transactional
public class SessionResourceLms {

    private final Logger log = LoggerFactory.getLogger(SessionResourceLms.class);

    private static final String ENTITY_NAME = "session";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionRepository sessionRepository;
    private final UserCustomRepository userCustomRepository;

    public SessionResourceLms(
        SessionRepository sessionRepository,
        SessionSearchRepository sessionSearchRepository,
        UserCustomRepository userCustomRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.userCustomRepository = userCustomRepository;
    }

    @GetMapping("")
    public List<Session> getAllSessions(
        @RequestParam Long siteId,
        @RequestParam SessionType sessionType,
        @RequestParam TargetedGender gender
    ) {
        log.debug("REST request and filter to get all Sessions");
        List<Session> sessions = new ArrayList<Session>();
        sessions = sessionRepository.findFilteredSessions(siteId, sessionType, gender);
        return sessions;
    }

    @GetMapping("/mySessions")
    public ResponseEntity<List<Session>> getSessions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        Pageable pageable,
        Principal principal,
        @RequestParam Long siteId,
        @RequestParam SessionType sessionType,
        @RequestParam TargetedGender gender
    ) {
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();
        List<Session> sessionList = new ArrayList<Session>();
        long totalElements = 0;
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                //Page<Session> sessions = sessionRepository.findAllWithEagerRelationshipsByUserCustom(pageable, userCustom);
                /*if(sessions!=null)
                {
                    sessionList = tickets.getContent();
                    totalElements = tickets.getTotalElements();
                }*/
            } else {
                List<Session> sessions = sessionRepository.findFilteredSessions(siteId, sessionType, gender);
                if (sessions != null) {
                    sessionList = sessions;
                    totalElements = sessions.size();
                }
            }
        }
        log.debug("REST request to get all Sessions");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + totalElements);
        return new ResponseEntity<>(sessionList, headers, HttpStatus.OK);
    }
}
