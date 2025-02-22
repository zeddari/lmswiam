package com.wiam.lms.web.rest.custom;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.SessionDTO;
import com.wiam.lms.domain.enumeration.AccountStatus;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.Sex;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.repository.SessionRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.SessionSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
        // sessions = sessionRepository.findFilteredSessions(siteId, sessionType, gender);
        return sessions;
    }

    @GetMapping("/mySessions")
    public ResponseEntity<List<Session>> getSessions(
        Pageable pageable,
        @RequestParam(required = false) Long siteId,
        @RequestParam(required = false) SessionType sessionType,
        @RequestParam(required = false) TargetedGender gender,
        @RequestParam(required = false) String query,
        Principal principal
    ) {
        // Validate principal
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Find user with optional null check
            UserCustom userCustom = userCustomRepository
                .findUserCustomByLogin(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            // Check if user is an admin
            boolean isAdmin = userCustom.getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getName()));

            // Fetch the sessions based on admin or user role
            Page<Session> sessionPage;
            if (isAdmin) {
                // Admin logic: Fetch sessions with optional filters
                sessionPage = sessionRepository.findFilteredSessions(siteId, sessionType, gender, query, pageable);
            } else {
                // Non-admin logic: Filter sessions based on user’s group or custom criteria (modify as needed)
                sessionPage =
                    sessionRepository.findFilteredSessionsForUser(userCustom.getId(), siteId, sessionType, gender, query, pageable);
            }

            List<Session> sessionList = sessionRepository.findBySessionsIn(sessionPage.getContent());

            // Create response headers for pagination
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(sessionPage.getTotalElements()));

            // Return response with paginated session data
            return ResponseEntity.ok().headers(headers).body(sessionList);
        } catch (EntityNotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/activities/halaqa")
    public ResponseEntity<List<String>> getHalaqaActivities() {
        List<String> halaqaActivities = new ArrayList<>();
        halaqaActivities.add("التفسير");
        halaqaActivities.add("العبادات");

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(halaqaActivities, headers, HttpStatus.OK);
    }

    @GetMapping("/sessionsDto")
    public ResponseEntity<List<SessionDTO>> getSessionsDto(
        Pageable pageable,
        @RequestParam(required = false) Long siteId,
        @RequestParam(required = false) SessionType sessionType,
        @RequestParam(required = false) TargetedGender gender,
        @RequestParam(required = false) String query,
        Principal principal
    ) {
        log.debug("REST request to get filtered Sessions");

        if (principal == null) {
            return ResponseEntity.status(401).build(); // Unauthorized if no principal is found
        }

        try {
            UserCustom user = userCustomRepository
                .findUserCustomByLogin(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            boolean isAdmin = user.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getName()));

            // Fetch data based on user role (admin or not)
            Page<SessionDTO> page;
            if (isAdmin) {
                page = sessionRepository.findFilteredSessionsDTO(siteId, sessionType, gender, query, pageable);
            } else {
                page = sessionRepository.findFilteredSessionsDTOForUser(user.getId(), siteId, sessionType, gender, query, pageable);
            }

            // Option 1: Using UriComponentsBuilder for pagination headers
            HttpHeaders headers = generatePaginationHeaders(page);

            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (EntityNotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.status(404).build(); // Not found if user is not found
        } catch (Exception e) {
            log.error("Error retrieving sessions", e);
            return ResponseEntity.status(500).build(); // Internal server error
        }
    }

    /**
     * Generate pagination headers for a Spring Data Page object.
     */
    private HttpHeaders generatePaginationHeaders(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));

        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page").replaceQueryParam("size").toUriString();

        StringBuilder link = new StringBuilder();
        if (page.hasNext()) {
            link.append(String.format("<%s?page=%d&size=%d>; rel=\"next\"", baseUrl, page.getNumber() + 1, page.getSize()));
        }
        if (page.hasPrevious()) {
            if (link.length() > 0) {
                link.append(", ");
            }
            link.append(String.format("<%s?page=%d&size=%d>; rel=\"prev\"", baseUrl, page.getNumber() - 1, page.getSize()));
        }
        if (link.length() > 0) {
            headers.add("Link", link.toString());
        }

        return headers;
    }
}
