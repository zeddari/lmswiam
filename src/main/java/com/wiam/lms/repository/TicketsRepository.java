package com.wiam.lms.repository;

import com.wiam.lms.domain.Tickets;
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
}
