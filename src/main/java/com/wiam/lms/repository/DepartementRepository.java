package com.wiam.lms.repository;

import com.wiam.lms.domain.Departement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Departement entity.
 */
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    default Optional<Departement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Departement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Departement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select departement from Departement departement left join fetch departement.departement1",
        countQuery = "select count(departement) from Departement departement"
    )
    Page<Departement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select departement from Departement departement left join fetch departement.departement1")
    List<Departement> findAllWithToOneRelationships();

    @Query("select departement from Departement departement left join fetch departement.departement1 where departement.id =:id")
    Optional<Departement> findOneWithToOneRelationships(@Param("id") Long id);
}
