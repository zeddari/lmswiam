package com.wiam.lms.repository;

import com.wiam.lms.domain.SessionLink;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionLink entity.
 */
@Repository
public interface SessionLinkRepository extends JpaRepository<SessionLink, Long> {
    default Optional<SessionLink> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SessionLink> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SessionLink> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sessionLink from SessionLink sessionLink left join fetch sessionLink.site15",
        countQuery = "select count(sessionLink) from SessionLink sessionLink"
    )
    Page<SessionLink> findAllWithToOneRelationships(Pageable pageable);

    @Query("select sessionLink from SessionLink sessionLink left join fetch sessionLink.site15")
    List<SessionLink> findAllWithToOneRelationships();

    @Query("select sessionLink from SessionLink sessionLink left join fetch sessionLink.site15 where sessionLink.id =:id")
    Optional<SessionLink> findOneWithToOneRelationships(@Param("id") Long id);
}
