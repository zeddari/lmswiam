package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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
        value = "select session from Session session left join fetch session.site14 left join fetch session.groups",
        countQuery = "select count(session) from Session session"
    )
    Page<Session> findAllWithToOneRelationships(Pageable pageable);

    @Query("select session from Session session left join fetch session.site14 left join fetch session.groups")
    List<Session> findAllWithToOneRelationships();

    @Query("select session from Session session left join fetch session.site14 left join fetch session.groups where session.id =:id")
    Optional<Session> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select session from Session session left join fetch session.site14 left join fetch session.groups left join fetch session.employees left join fetch session.professors where session.site14.id =:siteId and session.sessionType=:sessionType and session.targetedGender=:gender"
    )
    List<Session> findFilteredSessions(
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender
    );

    /*@Query(
        "select session from Session session where session.site14.id =:siteId and session.sessionType=:sessionType and session.targetedGender=:gender"
    )
    Page<Session> findFilteredSessions(
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender
    );*/

    @Query("select session from Session session where session.site14.id =:id")
    List<Session> findBySite(Long id);

    @Query(
        "select session from Session session left join fetch session.groups r  where r in (:mygroups) and session.sessionType=SessionType.HALAQA and session.sessionMode=SessionMode.ONLINE"
    )
    List<Session> findSessions(@Param("mygroups") List<Group> mygroups);

    @Query(
        "select session from Session session" +
        " left join fetch session.groups" +
        " left join fetch session.professors" +
        " left join fetch session.employees" +
        " where session.periodStartDate <= :sessionDate" +
        " and session.periodeEndDate >= :sessionDate" +
        " and session.site14.id=:siteId" +
        " and session.sessionType=:sessionType" +
        " and session.targetedGender=:gender"
    )
    List<Session> findSessionInstanceMulticreteria(
        @Param("siteId") Long siteId,
        @Param("gender") TargetedGender gender,
        @Param("sessionDate") LocalDate sessionDate,
        @Param("sessionType") SessionType sessionType
    );
}
