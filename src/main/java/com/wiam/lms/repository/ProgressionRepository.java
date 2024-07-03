package com.wiam.lms.repository;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesWithLabelData;
import com.wiam.lms.service.dto.FollowupAvgDTO;
import com.wiam.lms.service.dto.FollowupListDTO;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Progression entity.
 */
@Repository
public interface ProgressionRepository extends JpaRepository<Progression, Long> {
    default Optional<Progression> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Progression> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Progression> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select progression from Progression progression left join fetch progression.site17 left join fetch progression.fromAyahs left join fetch progression.toAyahs left join fetch progression.sessionInstance left join fetch progression.student",
        countQuery = "select count(progression) from Progression progression"
    )
    Page<Progression> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select progression from Progression progression left join fetch progression.fromAyahs left join fetch progression.toAyahs left join fetch progression.site17 left join fetch progression.sessionInstance left join fetch progression.student"
    )
    List<Progression> findAllWithToOneRelationships();

    @Query(
        "select progression from Progression progression left join fetch progression.site17 left join fetch progression.sessionInstance left join fetch progression.student where progression.id =:id"
    )
    Optional<Progression> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select progression from Progression progression where progression.student.id=:id")
    List<Progression> findAllByStudent(@Param("id") Long id);

    @Query(
        "select Progression from Progression progression where progression.tilawaType = 'HIFD' and progression.student.id = :studentId and progression.sessionInstance.startTime = (select max(progression1.sessionInstance.startTime) from Progression progression1 where progression1.tilawaType = 'HIFD' and progression1.student.id = :studentId) union select Progression from Progression progression where progression.tilawaType = 'TILAWA' and progression.student.id = :studentId and progression.sessionInstance.startTime = (select max(progression.sessionInstance.startTime) from Progression progression2 where progression2.tilawaType = 'TILAWA' and progression2.student.id = :studentId) union select Progression from Progression progression where progression.tilawaType = 'MORAJA3A' and progression.student.id = :studentId and progression.sessionInstance.startTime = (select max(progression.sessionInstance.startTime) from Progression progression3 where progression3.tilawaType = 'MORAJA3A' and progression3.student.id = :studentId)"
    )
    List<Progression> findAllLastByStudent(@Param("studentId") Long id);

    @Query(
        "select progression from Progression progression where progression.examType <> ExamType.NONE and progression.isForAttendance = false and progression.student.id=:id"
    )
    List<Progression> findExams(@Param("id") Long id);

    @Query(
        "select progression from Progression progression where progression.sessionInstance.id=:id1 and progression.student.id=:id2 and progression.isForAttendance=true"
    )
    Progression isAlreadyExists(@Param("id1") Long id1, @Param("id2") Long id2);

    @Query(
        "select progression from Progression progression left join fetch progression.fromAyahs left join fetch progression.toAyahs  left join fetch progression.student where progression.sessionInstance.id=:id"
    )
    List<Progression> findAllBySessionInstance(@Param("id") Long id);

    @Query(
        "select new com.wiam.lms.service.dto.FollowupAvgDTO(progression.tilawaType,AVG(progression.hifdScore)) from Progression progression where progression.student.id=:id and progression.sessionInstance.sessionDate BETWEEN :startDate AND :endDate group by progression.tilawaType"
    )
    List<FollowupAvgDTO> followupStudentScores(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query(
        "select new com.wiam.lms.service.dto.FollowupListDTO(progression.tilawaType,GROUP_CONCAT(progression.sessionInstance.startTime,'#',progression.hifdScore)) from Progression progression where progression.student.id=:id and progression.sessionInstance.sessionDate BETWEEN :startDate AND :endDate group by progression.tilawaType"
    )
    List<FollowupListDTO> followupStudentList(
        @Param("id") Long id,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query(
        "select progression from Progression progression where progression.site17.id=:siteId and progression.student.id=:studentId and progression.sessionInstance.sessionDate BETWEEN :fromDate AND :toDate and progression.isForAttendance=true group by progression.student"
    )
    List<Progression> findAllByStudentIdAndSessionInstanceBetween(
        @Param("siteId") Long siteId,
        @Param("studentId") Long studentId,
        @Param("fromDate") Date fromDate,
        @Param("toDate") Date toDate
    );

    @Query(
        "select attendance attendanceLabel, count(*) attendanceCount from Progression progression where progression.student.id = :id and progression.sessionInstance.sessionDate BETWEEN :fromDate AND :toDate group by progression.attendance"
    )
    List<RowSeriesWithLabelData> getStudentAbsenceData(
        @Param("id") Long id,
        @Param("fromDate") Date startDate,
        @Param("toDate") Date endDate
    );

    @Query("select progression from Progression progression where progression.isForAttendance = false and progression.student.id=:id")
    List<Progression> findProgressions(Long id);

    @Query(
        "select progression from Progression progression left join fetch progression.sessionInstance where progression.isForAttendance = true and progression.student.id=:id"
    )
    List<Progression> findAttendanceProgressions(Long id);

    @Query(
        "select progression from Progression progression left join fetch progression.sessionInstance left join fetch progression.student left join fetch progression.site17 where progression.isForAttendance = true and progression.site17.id=:siteId and progression.sessionInstance.session1.id=:sessionId and progression.sessionInstance.group.id=:groupId and progression.sessionInstance.sessionDate=:sessionDate"
    )
    List<Progression> findAttendanceAllProgressions(Long siteId, Long sessionId, Long groupId, LocalDate sessionDate);
}
