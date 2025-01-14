package com.wiam.lms.repository;

import com.wiam.lms.domain.Tickets;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.enumeration.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tickets entity.
 */
@Repository
public interface TicketsRepository extends JpaRepository<Tickets, Long> {
    default Optional<Tickets> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tickets> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tickets> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select tickets from Tickets tickets left join fetch tickets.site18 left join fetch tickets.userCustom5",
        countQuery = "select count(tickets) from Tickets tickets"
    )
    Page<Tickets> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tickets from Tickets tickets left join fetch tickets.site18 left join fetch tickets.userCustom5")
    List<Tickets> findAllWithToOneRelationships();

    @Query("select tickets from Tickets tickets left join fetch tickets.site18 left join fetch tickets.userCustom5 where tickets.id =:id")
    Optional<Tickets> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select tickets from Tickets tickets left join fetch tickets.userCustom5 where tickets.userCustom5.id =:id")
    List<Tickets> findByCustomUser(@Param("id") Long id);

    @Query(
        value = "select tickets from Tickets tickets left join fetch tickets.site18 left join fetch tickets.userCustom5 where tickets.userCustom5=:userCustom"
    )
    Page<Tickets> findAllWithEagerRelationshipsByUserCustom(Pageable pageable, @Param("userCustom") UserCustom userCustom);

    Page<Tickets> findBySubjectContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String subject, String description, Pageable pageable);

    @Query(
        value = "select tickets from Tickets tickets left join fetch tickets.site18 left join fetch tickets.userCustom5 where tickets.userCustom5=:userCustom and LOWER(tickets.subject) LIKE LOWER(CONCAT('%', :query, '%'))"
    )
    Page<Tickets> search(@Param("query") String query, @Param("userCustom") UserCustom userCustom, Pageable pageable);

    @Query(
        "SELECT t FROM Tickets t WHERE " +
        "(LOWER(t.subject) LIKE LOWER(:subject) OR :subject IS NULL) AND " +
        "(t.dateTicket >= :dateTicket OR :dateTicket IS NULL) AND " +
        "(t.dateProcess <= :dateProcess OR :dateProcess IS NULL) AND " +
        "(t.processed = :processed OR :processed IS NULL) AND " +
        "(t.userCustom5 = :userCustom OR :userCustom IS NULL)"
    )
    Page<Tickets> findTicketsByFilters(
        @Param("subject") String subject,
        @Param("dateTicket") LocalDateTime dateTicket,
        @Param("dateProcess") LocalDateTime dateProcess,
        @Param("processed") TicketStatus processed,
        @Param("userCustom") UserCustom userCustom,
        Pageable pageable
    );
}
