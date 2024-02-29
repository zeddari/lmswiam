package com.wiam.lms.repository;

import com.wiam.lms.domain.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Session entity.
 *
 * When extending this class, extend SessionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SessionRepository extends SessionRepositoryWithBagRelationships, JpaRepository<Session, Long> {
    default Optional<Session> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Session> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Session> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select session from Session session left join fetch session.site14",
        countQuery = "select count(session) from Session session"
    )
    Page<Session> findAllWithToOneRelationships(Pageable pageable);

    @Query("select session from Session session left join fetch session.site14")
    List<Session> findAllWithToOneRelationships();

    @Query("select session from Session session left join fetch session.site14 where session.id =:id")
    Optional<Session> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select session from Session session  where session.site14.id =:id")
    List<Session> findAllBySite(@Param("id") Long id);
}
