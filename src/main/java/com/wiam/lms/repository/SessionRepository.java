package com.wiam.lms.repository;

import com.wiam.lms.domain.Group;
import com.wiam.lms.domain.Session;
import com.wiam.lms.domain.SessionInstance;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.dto.SessionDTO;
import com.wiam.lms.domain.enumeration.SessionType;
import com.wiam.lms.domain.enumeration.TargetedGender;
import com.wiam.lms.domain.statistics.SessionTypeCountDTO;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
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
        "select new Session(session.id, session.title, session.site14, session.sessionType, session.targetedGender) " +
        "from Session session " +
        "join session.site14 site " +
        "where (:siteId is null or session.site14.id = :siteId) " +
        "and (:sessionType is null or session.sessionType = :sessionType) " +
        "and (:gender is null or session.targetedGender = :gender) " +
        "and (:query is null or session.title like CONCAT('%', :query, '%'))"
    )
    Page<Session> findFilteredSessions(
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender,
        @Param("query") String query,
        Pageable pageable
    );

    @Query(
        "select session " +
        "from Session session " +
        "join session.site14 site " +
        "left join session.professors professor " +
        "left join session.employees employee " +
        "left join session.groups group " +
        "left join group.elements element " +
        "where (:siteId is null or session.site14.id = :siteId) " +
        "and (:sessionType is null or session.sessionType = :sessionType) " +
        "and (:gender is null or session.targetedGender = :gender) " +
        "and (:query is null or session.title like CONCAT('%', :query, '%')) " +
        "and (" +
        "(professor.id = :userId) or " +
        "(employee.id = :userId) or " +
        "(element.id = :userId)" + // Removed the extra comma here
        ")"
    )
    Page<Session> findFilteredSessionsForUser(
        @Param("userId") Long userId, // user ID to filter the sessions the user is involved with
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender,
        @Param("query") String query,
        Pageable pageable
    );

    @Query(
        "select distinct session from Session session " +
        "join fetch session.site14 " +
        "left join fetch session.groups " +
        "left join fetch session.employees " +
        "left join fetch session.professors " +
        "where session in :sessions"
    )
    List<Session> findBySessionsIn(@Param("sessions") Collection<Session> sessions);

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
        " where (:sessionDate is null or session.periodStartDate <= :sessionDate) " +
        " and (:sessionDate is null or session.periodeEndDate >= :sessionDate) " +
        " and (:siteId is null or session.site14.id = :siteId) " +
        " and (:sessionType is null or session.sessionType = :sessionType) " +
        " and (:gender is null or session.targetedGender = :gender)"
    )
    List<Session> findSessionInstanceMulticriteria(
        @Param("siteId") Long siteId,
        @Param("gender") TargetedGender gender,
        @Param("sessionDate") LocalDate sessionDate,
        @Param("sessionType") SessionType sessionType
    );

    @Query("SELECT DISTINCT s FROM Session s JOIN s.groups g WHERE g.id IN :groupIds")
    List<Session> findSessionsByUserGroups(@Param("groupIds") List<Long> groupIds);

    @Query("SELECT DISTINCT s FROM Session s JOIN s.employees e WHERE e = :userCustom")
    List<Session> findSessionsByEmployee(@Param("userCustom") UserCustom userCustom);

    @Query("SELECT DISTINCT s FROM Session s JOIN s.professors e WHERE e = :userCustom")
    List<Session> findSessionsByProfessor(@Param("userCustom") UserCustom userCustom);

    @Query(
        "SELECT NEW com.wiam.lms.domain.statistics.SessionTypeCountDTO(s.sessionType, COUNT(s)) " +
        "FROM Session s " +
        "left join s.site14 site " +
        "WHERE (:siteId is null or (site is not null and site14.id = :siteId)) " +
        "GROUP BY s.sessionType"
    )
    List<SessionTypeCountDTO> countBySessionTypeAndSiteId(@Param("siteId") Long siteId);

    @Query(
        """
            SELECT new com.wiam.lms.domain.dto.SessionDTO(
                s.id,
                COALESCE(s.title, ''),
                s.site14.id,
                COALESCE(s.site14.nameAr, ''),
                COALESCE(s.site14.nameLat, ''),
                s.sessionType,
                s.targetedGender,
                s.periodStartDate,
                s.periodeEndDate,
                COALESCE(s.sunday, false),
                COALESCE(s.monday, false),
                COALESCE(s.tuesday, false),
                COALESCE(s.wednesday, false),
                COALESCE(s.thursday, false),
                COALESCE(s.friday, false),
                COALESCE(s.saturday, false)
            )
            FROM Session s
            LEFT JOIN s.site14 site
            WHERE (:siteId IS NULL OR s.site14.id = :siteId)
            AND (:sessionType IS NULL OR s.sessionType = :sessionType)
            AND (:gender IS NULL OR s.targetedGender = :gender)
            AND (:query IS NULL OR
                 LOWER(COALESCE(s.title, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
                 LOWER(COALESCE(s.site14.nameAr, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR
                 LOWER(COALESCE(s.site14.nameLat, '')) LIKE LOWER(CONCAT('%', :query, '%')))
        """
    )
    Page<SessionDTO> findFilteredSessionsDTO(
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender,
        @Param("query") String query,
        Pageable pageable
    );

    @Query(
        """
            SELECT new com.wiam.lms.domain.dto.SessionDTO(
                s.id,
                COALESCE(s.title, ''),
                s.site14.id,
                COALESCE(s.site14.nameAr, ''),
                COALESCE(s.site14.nameLat, ''),
                s.sessionType,
                s.targetedGender,
                s.periodStartDate,
                s.periodeEndDate,
                COALESCE(s.sunday, false),
                COALESCE(s.monday, false),
                COALESCE(s.tuesday, false),
                COALESCE(s.wednesday, false),
                COALESCE(s.thursday, false),
                COALESCE(s.friday, false),
                COALESCE(s.saturday, false)
            )
            FROM Session s
            LEFT JOIN s.professors p
            LEFT JOIN s.employees e
            LEFT JOIN s.groups g
            LEFT JOIN g.elements el
            WHERE (:siteId IS NULL OR s.site14.id = :siteId)
            AND (:sessionType IS NULL OR s.sessionType = :sessionType)
            AND (:gender IS NULL OR s.targetedGender = :gender)
            AND (:query IS NULL OR LOWER(COALESCE(s.title, '')) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (p.id = :userId OR e.id = :userId OR el.id = :userId)
        """
    )
    Page<SessionDTO> findFilteredSessionsDTOForUser(
        @Param("userId") Long userId,
        @Param("siteId") Long siteId,
        @Param("sessionType") SessionType sessionType,
        @Param("gender") TargetedGender gender,
        @Param("query") String query,
        Pageable pageable
    );
}
