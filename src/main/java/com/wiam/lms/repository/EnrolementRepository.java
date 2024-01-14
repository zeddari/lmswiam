package com.wiam.lms.repository;

import com.wiam.lms.domain.Enrolement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enrolement entity.
 */
@Repository
public interface EnrolementRepository extends JpaRepository<Enrolement, Long> {
    default Optional<Enrolement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Enrolement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Enrolement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select enrolement from Enrolement enrolement left join fetch enrolement.course",
        countQuery = "select count(enrolement) from Enrolement enrolement"
    )
    Page<Enrolement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select enrolement from Enrolement enrolement left join fetch enrolement.course")
    List<Enrolement> findAllWithToOneRelationships();

    @Query("select enrolement from Enrolement enrolement left join fetch enrolement.course where enrolement.id =:id")
    Optional<Enrolement> findOneWithToOneRelationships(@Param("id") Long id);
}
