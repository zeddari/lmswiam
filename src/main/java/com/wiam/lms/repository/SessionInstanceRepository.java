package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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

    List<SessionInstance> findByIdInAndStartTimeBetween(
        List<Long> ids,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    default List<SessionInstance> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<SessionInstance> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.site16 left join fetch sessionInstance.session1",
        countQuery = "select count(sessionInstance) from SessionInstance sessionInstance"
    )
    Page<SessionInstance> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.site16 left join fetch sessionInstance.session1"
    )
    List<SessionInstance> findAllWithToOneRelationships();

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.site16 left join fetch sessionInstance.session1 where sessionInstance.id =:id"
    )
    Optional<SessionInstance> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance where sessionInstance.session1.id =:id and  EXTRACT(MONTH FROM sessionInstance.sessionDate) =:month and  EXTRACT(YEAR FROM sessionInstance.sessionDate) =:year"
    )
    List<SessionInstance> findOneBySessionId(@Param("id") Long id, @Param("month") Long month, @Param("year") Long year);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance  where sessionInstance.session1.id=:sessionId and sessionInstance.sessionDate=:sessionDate and sessionInstance.group.id=:groupId"
    )
    Optional<SessionInstance> findOne(
        @Param("sessionId") Long sessionId,
        @Param("sessionDate") LocalDate sessionDate,
        @Param("groupId") Long groupId
    );

    @Query(
        "select sessionInstance from SessionInstance sessionInstance  where sessionInstance.group in (:mygroups) and sessionInstance.session1.sessionType=SessionType.HALAQA and sessionInstance.session1.sessionMode=SessionMode.ONLINE"
    )
    List<SessionInstance> findRemoteSessionInstances(@Param("mygroups") List<Group> mygroups);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance  where sessionInstance.group in (:mygroups) and sessionInstance.session1.sessionType=SessionType.HALAQA"
    )
    List<SessionInstance> findAllSessionInstances(@Param("mygroups") List<Group> mygroups);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance left join fetch sessionInstance.professor where sessionInstance.sessionDate=:sessionDate and sessionInstance.site16.id=:id"
    )
    List<SessionInstance> findOneBySiteId(@Param("id") Long id, @Param("sessionDate") LocalDate sessionDate);

    @Query(
        "select distinct sessionInstance from SessionInstance sessionInstance " +
        "left join fetch sessionInstance.session1 session " +
        "left join fetch sessionInstance.progressions progression " +
        "left join fetch progression.fromSourate fromSourate " +
        "left join fetch progression.toSourate toSourate " +
        "left join fetch progression.fromAyahs fromAyahs " +
        "left join fetch progression.toAyahs toAyahs " +
        "where (:siteId is null or sessionInstance.site16.id = :siteId) " +
        "and (:gender is null or session.targetedGender = :gender) " +
        "and (:year is null or EXTRACT(YEAR FROM sessionInstance.sessionDate) = :year) " +
        "and (:month is null or EXTRACT(MONTH FROM sessionInstance.sessionDate) = :month) " +
        "and (:sessionType is null or session.sessionType = :sessionType) " +
        "and (:sessionId is null or session.id = :sessionId) " +
        "and (progression.isForAttendance = :isForAttendance) " +
        "and (:userId is null or sessionInstance.professor.id = :userId " +
        "or (progression.student.id = :userId))"
    )
    List<SessionInstance> findSessionInstanceMulticreteria(
        @Param("siteId") Long siteId,
        @Param("gender") TargetedGender gender,
        @Param("year") Integer year,
        @Param("month") Integer month,
        @Param("sessionType") SessionType sessionType,
        @Param("sessionId") Long sessionId,
        @Param("userId") Long userId,
        @Param("isForAttendance") boolean isForAttendance
    );

    List<SessionInstance> findByGroupId(Long id);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance  where sessionInstance.group in (:mygroups) and sessionInstance.session1.sessionType=SessionType.HALAQA and sessionInstance.session1.sessionMode=SessionMode.ONLINE"
    )
    Page<SessionInstance> findRemoteSessionInstancesByRole(@Param("mygroups") List<Group> mygroups, Pageable pageable);

    @Query(
        "select sessionInstance from SessionInstance sessionInstance " +
        "where sessionInstance.sessionDate = :sessionDate " +
        "and sessionInstance.session1.id = :sessionId " +
        "and sessionInstance.group.id = :groupId " +
        "and sessionInstance.professor.id = :professorId"
    )
    List<SessionInstance> findSessionInstanceByDateGroupProfessorSession(
        @Param("sessionDate") LocalDate sessionDate,
        @Param("sessionId") Long sessionId,
        @Param("groupId") Long groupId,
        @Param("professorId") Long professorId
    );
}
