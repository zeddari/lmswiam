package com.wiam.lms.repository;

import com.wiam.lms.domain.Sponsoring;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sponsoring entity.
 */
@Repository
public interface SponsoringRepository extends JpaRepository<Sponsoring, Long> {
    default Optional<Sponsoring> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sponsoring> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sponsoring> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sponsoring from Sponsoring sponsoring left join fetch sponsoring.sponsor left join fetch sponsoring.project left join fetch sponsoring.currency",
        countQuery = "select count(sponsoring) from Sponsoring sponsoring"
    )
    Page<Sponsoring> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select sponsoring from Sponsoring sponsoring left join fetch sponsoring.sponsor left join fetch sponsoring.project left join fetch sponsoring.currency"
    )
    List<Sponsoring> findAllWithToOneRelationships();

    @Query(
        "select sponsoring from Sponsoring sponsoring left join fetch sponsoring.sponsor left join fetch sponsoring.project left join fetch sponsoring.currency where sponsoring.id =:id"
    )
    Optional<Sponsoring> findOneWithToOneRelationships(@Param("id") Long id);
}
