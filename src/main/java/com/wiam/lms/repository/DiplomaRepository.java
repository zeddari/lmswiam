package com.wiam.lms.repository;

import com.wiam.lms.domain.Diploma;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Diploma entity.
 */
@Repository
public interface DiplomaRepository extends JpaRepository<Diploma, Long> {
    default Optional<Diploma> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Diploma> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Diploma> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select diploma from Diploma diploma left join fetch diploma.site20",
        countQuery = "select count(diploma) from Diploma diploma"
    )
    Page<Diploma> findAllWithToOneRelationships(Pageable pageable);

    @Query("select diploma from Diploma diploma left join fetch diploma.site20")
    List<Diploma> findAllWithToOneRelationships();

    @Query("select diploma from Diploma diploma left join fetch diploma.site20 where diploma.id =:id")
    Optional<Diploma> findOneWithToOneRelationships(@Param("id") Long id);
}
