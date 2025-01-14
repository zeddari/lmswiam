package com.wiam.lms.web.rest;

import com.wiam.lms.domain.Authority;
import com.wiam.lms.domain.Ayahs;
import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.Tickets;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.TicketStatus;
import com.wiam.lms.repository.TicketsRepository;
import com.wiam.lms.repository.UserCustomRepository;
import com.wiam.lms.repository.search.TicketsSearchRepository;
import com.wiam.lms.web.rest.errors.BadRequestAlertException;
import com.wiam.lms.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wiam.lms.domain.Tickets}.
 */
@RestController
@RequestMapping("/api/tickets")
@Transactional
public class TicketsResource {

    private final Logger log = LoggerFactory.getLogger(TicketsResource.class);

    private static final String ENTITY_NAME = "tickets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketsRepository ticketsRepository;

    private final TicketsSearchRepository ticketsSearchRepository;
    private final UserCustomRepository userCustomRepository;

    public TicketsResource(
        TicketsRepository ticketsRepository,
        TicketsSearchRepository ticketsSearchRepository,
        UserCustomRepository userCustomRepository
    ) {
        this.ticketsRepository = ticketsRepository;
        this.ticketsSearchRepository = ticketsSearchRepository;
        this.userCustomRepository = userCustomRepository;
    }

    /**
     * {@code POST  /tickets} : Create a new tickets.
     *
     * @param tickets the tickets to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tickets, or with status {@code 400 (Bad Request)} if the tickets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Tickets> createTickets(@Valid @RequestBody Tickets tickets, Principal principal) throws URISyntaxException {
        log.debug("REST request to save Tickets : {}", tickets);
        if (tickets.getId() != null) {
            throw new BadRequestAlertException("A new tickets cannot already have an ID", ENTITY_NAME, "idexists");
        }

        UUID uuid = UUID.randomUUID();
        tickets.setReference(uuid.toString());
        tickets.setDateTicket(LocalDateTime.now());
        tickets.setProcessed(TicketStatus.PENDING);

        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();

        if (userCustom != null) {
            tickets.setUserCustom5(userCustom);
            tickets.setSite18(userCustom.getSite13());
        }
        Tickets result = ticketsRepository.save(tickets);
        ticketsSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets/:id} : Updates an existing tickets.
     *
     * @param id the id of the tickets to save.
     * @param tickets the tickets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickets,
     * or with status {@code 400 (Bad Request)} if the tickets is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tickets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tickets> updateTickets(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tickets tickets
    ) throws URISyntaxException {
        log.debug("REST request to update Tickets : {}, {}", id, tickets);
        if (tickets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (
            tickets.getProcessed() == TicketStatus.PROCESSED &&
            tickets.getProcessed() != ticketsRepository.findById(id).get().getProcessed()
        ) tickets.setDateProcess(LocalDateTime.now());
        Tickets result = ticketsRepository.save(tickets);
        ticketsSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickets.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickets/:id} : Partial updates given fields of an existing tickets, field will ignore if it is null
     *
     * @param id the id of the tickets to save.
     * @param tickets the tickets to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tickets,
     * or with status {@code 400 (Bad Request)} if the tickets is not valid,
     * or with status {@code 404 (Not Found)} if the tickets is not found,
     * or with status {@code 500 (Internal Server Error)} if the tickets couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tickets> partialUpdateTickets(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tickets tickets
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tickets partially : {}, {}", id, tickets);
        if (tickets.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tickets.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tickets> result = ticketsRepository
            .findById(tickets.getId())
            .map(existingTickets -> {
                if (tickets.getSubject() != null) {
                    existingTickets.setSubject(tickets.getSubject());
                }
                if (tickets.getTitle() != null) {
                    existingTickets.setTitle(tickets.getTitle());
                }
                if (tickets.getReference() != null) {
                    existingTickets.setReference(tickets.getReference());
                }
                if (tickets.getDescription() != null) {
                    existingTickets.setDescription(tickets.getDescription());
                }
                if (tickets.getJustifDoc() != null) {
                    existingTickets.setJustifDoc(tickets.getJustifDoc());
                }
                if (tickets.getJustifDocContentType() != null) {
                    existingTickets.setJustifDocContentType(tickets.getJustifDocContentType());
                }
                if (tickets.getDateTicket() != null) {
                    existingTickets.setDateTicket(tickets.getDateTicket());
                }
                if (tickets.getDateProcess() != null) {
                    existingTickets.setDateProcess(tickets.getDateProcess());
                }
                if (tickets.getProcessed() != null) {
                    existingTickets.setProcessed(tickets.getProcessed());
                }
                if (tickets.getFrom() != null) {
                    existingTickets.setFrom(tickets.getFrom());
                }
                if (tickets.getToDate() != null) {
                    existingTickets.setToDate(tickets.getToDate());
                }
                if (tickets.getDecisionDetail() != null) {
                    existingTickets.setDecisionDetail(tickets.getDecisionDetail());
                }

                return existingTickets;
            })
            .map(ticketsRepository::save)
            .map(savedTickets -> {
                ticketsSearchRepository.index(savedTickets);
                return savedTickets;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tickets.getId().toString())
        );
    }

    @GetMapping("myTickets")
    public ResponseEntity<List<Tickets>> getAllTickets(
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateTicket,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateProcess,
        @RequestParam(required = false) TicketStatus processed,
        Pageable pageable,
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

            // Call the repository method to get the filtered and paginated tickets
            Page<Tickets> ticketsPage = ticketsRepository.findTicketsByFilters(
                subject,
                dateTicket,
                dateProcess,
                processed,
                isAdmin ? null : userCustom, // Only filter by user if the user is not an admin
                pageable
            );

            // Get content and pagination information
            List<Tickets> ticketsList = ticketsPage.getContent();
            long totalElements = ticketsPage.getTotalElements();
            int totalPages = ticketsPage.getTotalPages();
            int currentPage = ticketsPage.getNumber();

            // Create response headers for pagination
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalElements));
            headers.add("X-Total-Pages", String.valueOf(totalPages));
            headers.add("X-Page", String.valueOf(currentPage + 1)); // to return 1-indexed page
            headers.add("X-Page-Size", String.valueOf(pageable.getPageSize()));

            log.debug("REST request to get filtered Tickets");
            return ResponseEntity.ok().headers(headers).body(ticketsList);
        } catch (EntityNotFoundException e) {
            log.error("User not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * {@code GET  /tickets} : get all the tickets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Tickets>> getAllTickets(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        Pageable pageable,
        Principal principal
    ) {
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin(principal.getName()).get();
        List<Tickets> ticketsList = new ArrayList<Tickets>();
        long totalElements = 0;
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                Page<Tickets> tickets = ticketsRepository.findAllWithEagerRelationshipsByUserCustom(pageable, userCustom);
                if (tickets != null) {
                    ticketsList = tickets.getContent();
                    totalElements = tickets.getTotalElements();
                }
            } else {
                Page<Tickets> tickets = ticketsRepository.findAllWithEagerRelationships(pageable);
                if (tickets != null) {
                    ticketsList = tickets.getContent();
                    totalElements = tickets.getTotalElements();
                }
            }
        }
        log.debug("REST request to get all Tickets");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + totalElements);
        return new ResponseEntity<>(ticketsList, headers, HttpStatus.OK);
    }

    @GetMapping("{id}/myTickets")
    public List<Tickets> getRemoteSessions(@PathVariable Long id) {
        log.debug("REST request to get all tickets for the given user id");
        // getting the list of the student groups
        UserCustom userCustom = userCustomRepository.findById(id).get();
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom.getAuthorities().contains(a)) return ticketsRepository.findAll(); else return ticketsRepository.findByCustomUser(id);
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" tickets.
     *
     * @param id the id of the tickets to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tickets, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tickets> getTickets(@PathVariable("id") Long id) {
        log.debug("REST request to get Tickets : {}", id);
        Optional<Tickets> tickets = ticketsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(tickets);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" tickets.
     *
     * @param id the id of the tickets to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTickets(@PathVariable("id") Long id) {
        log.debug("REST request to delete Tickets : {}", id);
        ticketsRepository.deleteById(id);
        ticketsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tickets/_search?query=:query} : search for the tickets corresponding
     * to the query.
     *
     * @param query the query of the tickets search.
     * @return the result of the search.
     */
    /*@GetMapping("/_search")
    public List<Tickets> searchTickets(@RequestParam("query") String query) {
        log.debug("REST request to search Tickets for query {}", query);
        try {
            return StreamSupport.stream(ticketsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }*/

    @GetMapping(value = "/_search", produces = "application/json;charset=utf-8")
    public ResponseEntity<List<Tickets>> searchTickets(@RequestParam("query") String query, Pageable pageable) {
        query = "%" + query + "%"; // if we use like
        log.debug("REST request to search Tickets for query {}", query);
        UserCustom userCustom = userCustomRepository.findUserCustomByLogin("admin").get();
        List<Tickets> ticketsList = new ArrayList<Tickets>();
        long totalElements = 0;
        Authority a = new Authority();
        a.setName("ROLE_ADMIN");
        if (userCustom != null) {
            if (!userCustom.getAuthorities().contains(a)) {
                Page<Tickets> tickets = ticketsRepository.search(query, userCustom, pageable);
                if (tickets != null) {
                    ticketsList = tickets.getContent();
                    totalElements = tickets.getTotalElements();
                }
            } else {
                Page<Tickets> tickets = ticketsRepository.findAllWithEagerRelationships(pageable);
                if (tickets != null) {
                    ticketsList = tickets.getContent();
                    totalElements = tickets.getTotalElements();
                }
            }
        }
        log.debug("REST request to get all Tickets");
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + totalElements);
        return new ResponseEntity<>(ticketsList, headers, HttpStatus.OK);
    }
}
