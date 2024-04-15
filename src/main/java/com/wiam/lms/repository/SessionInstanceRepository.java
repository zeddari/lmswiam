package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.SessionInstance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionInstance entity.
 *
 * When extending this class, extend SessionInstanceRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SessionInstanceRepository extends SessionInstanceRepositoryWithBagRelationships, JpaRepository<SessionInstance, Long> {
    default Optional<SessionInstance> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<SessionInstance> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<SessionInstance> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.professor left join fetch sessionInstance.group left join fetch sessionInstance.site16 left join fetch sessionInstance.session1",
        countQuery = "select count(sessionInstance) from SessionInstance sessionInstance"
    )
    Page<SessionInstance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.professor left join fetch sessionInstance.group left join fetch sessionInstance.site16 left join fetch sessionInstance.session1"
    )
    List<SessionInstance> findAllWithToOneRelationships();

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.professor left join fetch sessionInstance.group left join fetch sessionInstance.site16 left join fetch sessionInstance.session1 where sessionInstance.id =:id"
    )
    Optional<SessionInstance> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance  where sessionInstance.group in (:mygroups) and sessionInstance.session1.sessionType=SessionType.HALAQA and sessionInstance.session1.sessionMode=SessionMode.ONLINE"
    )
    List<SessionInstance> findRemoteSessionInstances(@Param("mygroups") List<Group> mygroups);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.professor where sessionInstance.session1.id=:sessionId and sessionInstance.sessionDate=:sessionDate and sessionInstance.group.id=:groupId"
    )
    SessionInstance findOne(
        @Param("sessionId") Long sessionId,
        @Param("sessionDate") LocalDate sessionDate,
        @Param("groupId") Long groupId
    );
}
