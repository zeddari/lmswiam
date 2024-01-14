package com.wiam.lms.repository;

import com.wiam.lms.domain.Part;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Part entity.
 */
@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    default Optional<Part> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Part> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Part> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select part from Part part left join fetch part.course left join fetch part.part1",
        countQuery = "select count(part) from Part part"
    )
    Page<Part> findAllWithToOneRelationships(Pageable pageable);

    @Query("select part from Part part left join fetch part.course left join fetch part.part1")
    List<Part> findAllWithToOneRelationships();

    @Query("select part from Part part left join fetch part.course left join fetch part.part1 where part.id =:id")
    Optional<Part> findOneWithToOneRelationships(@Param("id") Long id);
}
