package com.wiam.lms.repository;

import com.wiam.lms.domain.Depense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Depense entity.
 */
@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    default Optional<Depense> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Depense> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Depense> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select depense from Depense depense left join fetch depense.resource",
        countQuery = "select count(depense) from Depense depense"
    )
    Page<Depense> findAllWithToOneRelationships(Pageable pageable);

    @Query("select depense from Depense depense left join fetch depense.resource")
    List<Depense> findAllWithToOneRelationships();

    @Query("select depense from Depense depense left join fetch depense.resource where depense.id =:id")
    Optional<Depense> findOneWithToOneRelationships(@Param("id") Long id);
}
