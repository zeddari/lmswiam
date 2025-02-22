package com.wiam.lms.repository;

import com.wiam.lms.domain.Progression;
import com.wiam.lms.domain.UserCustom;
import com.wiam.lms.domain.custom.projection.interfaces.Row3SeriesWithLabelData;
import com.wiam.lms.domain.custom.projection.interfaces.RowSeriesWithLabelData;
import com.wiam.lms.domain.dto.ProgressionDetailsDTO;
import com.wiam.lms.domain.dto.ProgressionDto;
import com.wiam.lms.domain.enumeration.ExamType;
import com.wiam.lms.domain.enumeration.Tilawa;
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
        "select progression from Progression progression where progression.tilawaType = 'HIFD' and progression.student.id = :studentId and progression.createdAt = (select max(progression1.createdAt) from Progression progression1 where progression1.tilawaType = 'HIFD' and progression1.student.id = :studentId) union select progression from Progression progression where progression.tilawaType = 'TILAWA' and progression.student.id = :studentId and progression.createdAt = (select max(progression.createdAt) from Progression progression2 where progression2.tilawaType = 'TILAWA' and progression2.student.id = :studentId) union select progression from Progression progression where progression.tilawaType = 'MORAJA3A' and progression.student.id = :studentId and progression.createdAt = (select max(progression.createdAt) from Progression progression3 where progression3.tilawaType = 'MORAJA3A' and progression3.student.id = :studentId)"
    )
    List<Progression> findAllLastByStudent(@Param("studentId") Long id);

    @Query(
        "select progression from Progression progression where progression.tilawaType = 'HIFD' and progression.createdAt = (select max(progression1.createdAt) from Progression progression1 where progression1.tilawaType = 'HIFD') union select progression from Progression progression where progression.tilawaType = 'TILAWA' and progression.createdAt = (select max(progression.createdAt) from Progression progression2 where progression2.tilawaType = 'TILAWA' ) union select progression from Progression progression where progression.tilawaType = 'MORAJA3A' and progression.createdAt = (select max(progression.createdAt) from Progression progression3 where progression3.tilawaType = 'MORAJA3A')"
    )
    List<Progression> findAllLastAllStudent();

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
        "select progression from Progression progression left join fetch progression.fromAyahs left join fetch progression.toAyahs  left join fetch progression.student where progression.sessionInstance.id=:id and progression.student.id=:userCustomId"
    )
    List<Progression> findAllByRole(@Param("id") Long id, @Param("userCustomId") Long userCustomId);

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
        "select progression from Progression progression where progression.student.id=:studentId and progression.sessionInstance.sessionDate BETWEEN :fromDate AND :toDate and progression.isForAttendance=false group by progression.id"
    )
    List<Progression> findAllByStudentIdAndSessionInstanceBetween(
        @Param("studentId") Long studentId,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate
    );

    @Query(
        "select progression from Progression progression where progression.sessionInstance.id=:sessionInstanceId and progression.createdAt BETWEEN :fromDate AND :toDate and progression.isForAttendance=false group by progression.id"
    )
    List<Progression> findAllStudentIdAndSessionInstanceBetween(
        @Param("sessionInstanceId") Long sessionInstanceId,
        @Param("fromDate") Date fromDate,
        @Param("toDate") Date toDate
    );

    @Query(
        "select progression from Progression progression where progression.sessionInstance.sessionDate=:sessionDate and progression.sessionInstance.session1.id=:id order by progression.student.login"
    )
    List<Progression> findAllBySessionIdAndDate(@Param("sessionDate") LocalDate sessionDate, @Param("id") Integer id);

    @Query(
        "select progression from Progression progression where progression.sessionInstance.sessionDate=:sessionDate and progression.sessionInstance.session1.id=:id and progression.examType=:examType order by progression.student.login"
    )
    List<Progression> findAllBySessionIdAndDateAndExamType(
        @Param("sessionDate") LocalDate sessionDate,
        @Param("id") Integer id,
        @Param("examType") ExamType examType
    );

    @Query(
        "select attendance attendanceLabel, count(*) attendanceCount from Progression progression where progression.student.id = :id and progression.createdAt BETWEEN :fromDate AND :toDate AND isForAttendance = true group by progression.attendance"
    )
    List<RowSeriesWithLabelData> getStudentAbsenceData(
        @Param("id") Long id,
        @Param("fromDate") LocalDate startDate,
        @Param("toDate") LocalDate endDate
    );

    @Query(
        "select attendance xaxis, count(*) yaxis from Progression progression where progression.student.id = :id and progression.createdAt BETWEEN :fromDate AND :toDate AND isForAttendance = true group by progression.attendance"
    )
    List<RowSeriesWithLabelData> getStudentAbsenceDataDate(
        @Param("id") Long id,
        @Param("fromDate") Date startDate,
        @Param("toDate") Date endDate
    );

    @Query(
        "select attendance xaxis, count(*) yaxis, CONCAT(progression.student.firstName, ' ' , progression.student.lastName) zaxis from Progression progression where progression.sessionInstance.id = :sessionInstanceId and progression.createdAt BETWEEN :fromDate AND :toDate AND isForAttendance = true group by progression.attendance, progression.student.firstName, progression.student.lastName"
    )
    List<Row3SeriesWithLabelData> getAllStudentAbsenceDataDate(
        @Param("sessionInstanceId") Long sessionInstanceId,
        @Param("fromDate") Date startDate,
        @Param("toDate") Date endDate
    );

    @Query("select progression from Progression progression where progression.isForAttendance = false and progression.student.id=:id")
    List<Progression> findProgressions(Long id);

    @Query(
        "SELECT progression FROM Progression progression " +
        "WHERE progression.isForAttendance = false " +
        "AND progression.student.id = :id " +
        "AND progression.sessionInstance.sessionDate BETWEEN :fromSessionDate AND :toSessionDate " +
        "AND progression.examType=:examType"
    )
    List<Progression> findUserProgressions(
        @Param("id") Long id,
        @Param("fromSessionDate") LocalDate fromSessionDate,
        @Param("toSessionDate") LocalDate toSessionDate,
        @Param("examType") ExamType examType
    );

    @Query(
        "select progression from Progression progression left join fetch progression.sessionInstance where progression.isForAttendance = true and progression.student.id=:id"
    )
    List<Progression> findAttendanceProgressions(Long id);

    @Query(
        "select progression from Progression progression left join fetch progression.sessionInstance left join fetch progression.student left join fetch progression.site17 where progression.isForAttendance = true and progression.site17.id=:siteId and progression.sessionInstance.session1.id=:sessionId and progression.sessionInstance.group.id=:groupId and progression.sessionInstance.sessionDate=:sessionDate"
    )
    List<Progression> findAttendanceAllProgressions(Long siteId, Long sessionId, Long groupId, LocalDate sessionDate);

    @Query(
        "SELECT new com.wiam.lms.domain.dto.ProgressionDetailsDTO(" +
        "p.id, " +
        "p.isForAttendance, " +
        "p.attendance, " +
        "new com.wiam.lms.domain.dto.SurahDto(p.fromSourate.id, p.fromSourate.nameAr, p.fromSourate.nameEn, p.fromSourate.ayahsCount), " +
        "new com.wiam.lms.domain.dto.SurahDto(p.toSourate.id, p.toSourate.nameAr, p.toSourate.nameEn, p.toSourate.ayahsCount), " +
        "new com.wiam.lms.domain.dto.AyahDto(p.fromAyahs.id, p.fromAyahs.numberInSurah), " +
        "new com.wiam.lms.domain.dto.AyahDto(p.toAyahs.id, p.toAyahs.numberInSurah), " +
        "p.tilawaType, " +
        "p.student.id, " +
        "p.student.firstName, " +
        "p.sessionInstance.id, " +
        "p.site17.id, " +
        "p.riwaya, " +
        "p.examType) " +
        "FROM Progression p " +
        "LEFT JOIN p.fromSourate " +
        "LEFT JOIN p.toSourate " +
        "LEFT JOIN p.fromAyahs " +
        "LEFT JOIN p.toAyahs " +
        "WHERE (:siteId IS NULL OR p.site17.id = :siteId) " +
        "AND (:userId IS NULL OR p.student.id = :userId) " +
        "AND (:isForAttendance IS NULL OR p.isForAttendance = :isForAttendance) " +
        "AND (:tilawaType IS NULL OR p.tilawaType = :tilawaType) " +
        "AND (:examType IS NULL OR p.examType = :examType) " +
        "AND (:fromSourate IS NULL OR p.fromSourate.id = :fromSourate) " +
        "AND (:toSourate IS NULL OR p.toSourate.id = :toSourate) " +
        "AND (:sessionInstanceId IS NULL OR p.sessionInstance.id = :sessionInstanceId)"
    )
    List<ProgressionDetailsDTO> findProgressions(
        @Param("siteId") Long siteId,
        @Param("userId") Long userId,
        @Param("isForAttendance") Boolean isForAttendance,
        @Param("tilawaType") Tilawa tilawaType,
        @Param("examType") ExamType examType,
        @Param("fromSourate") Long fromSourate,
        @Param("toSourate") Long toSourate,
        @Param("sessionInstanceId") Long sessionInstanceId
    );
}
